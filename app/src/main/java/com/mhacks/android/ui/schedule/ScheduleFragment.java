package com.mhacks.android.ui.schedule;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.mhacks.android.R;
import com.mhacks.android.data.model.Event;
import com.mhacks.android.data.model.User;
import com.mhacks.android.ui.MainActivity;
import com.mhacks.android.ui.common.ParseAdapter;
import com.mhacks.android.ui.common.Util;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

import java.text.SimpleDateFormat;

/**
 * Created by Damian Wieczorek <damianw@umich.edu> on 7/27/14.
 */
public class ScheduleFragment extends Fragment implements ParseAdapter.ListCallbacks<Event> {
  public static final String TAG = "ScheduleFragment";

  private ListView mListView;
  private SwipeRefreshLayout mLayout;
  private ParseAdapter<Event> mAdapter;

  public ScheduleFragment() {
    super();
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    ParseQueryAdapter.QueryFactory<Event> factory = new ParseQueryAdapter.QueryFactory<Event>() {
      @Override
      public ParseQuery<Event> create() {
        return Event.query().orderByDescending(Event.CREATED_AT);
      }
    };
    mAdapter = new ParseAdapter<>(getActivity(), R.layout.adapter_event, this, factory);

    setHasOptionsMenu(true);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    mLayout = (SwipeRefreshLayout) inflater.inflate(R.layout.fragment_events, null);

    mListView = (ListView) mLayout.findViewById(R.id.events_list);
    mListView.setAdapter(mAdapter);

    mAdapter.bindSync(mLayout);
    if (getArguments().getBoolean(MainActivity.SHOULD_SYNC, false)) mAdapter.onRefresh();

    return mLayout;
  }

  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    if (User.getCurrentUser().isAdmin()) {
      inflater.inflate(R.menu.fragment_schedule, menu);
    }
    super.onCreateOptionsMenu(menu, inflater);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.events_new:
        EventEditDialogFragment.newInstance(null).show(getFragmentManager(), EventEditDialogFragment.TAG);
        return true;
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  public void fillView(ParseAdapter.ViewHolder holder, Event event) {
    TextView title = holder.get(R.id.event_title);
    TextView details = holder.get(R.id.event_details);
    TextView host = holder.get(R.id.event_host);
    TextView time = holder.get(R.id.event_time);


    title.setText(event.getTitle());
    details.setText(event.getDetails());
    host.setText(event.getHost().getTitle());
    time.setText(new SimpleDateFormat("EEEE").format(event.getTime()) + " " + Util.Time.roundTimeAndFormat(event.getTime(), 2));
  }

}

