package com.mhacks.android.ui.concierge;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.common.collect.Ordering;
import com.mhacks.android.R;
import com.mhacks.android.data.model.User;
import com.mhacks.android.ui.MainActivity;
import com.mhacks.android.ui.announcements.AnnouncementEditDialogFragment;
import com.mhacks.android.ui.common.ParseAdapter;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

/**
 * Created by Damian Wieczorek <damianw@umich.edu> on 7/24/14.
 */
public class ConciergeFragment extends Fragment implements
  ParseAdapter.ListCallbacks<User>,
  AdapterView.OnItemClickListener,
  AdapterView.OnItemLongClickListener {
  public static final String TAG = "ConciergeFragment";

  private ListView mListView;
  private SwipeRefreshLayout mLayout;
  private ParseAdapter<User> mAdapter;

  public ConciergeFragment() {
    super();
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    ParseQueryAdapter.QueryFactory<User> factory = new ParseQueryAdapter.QueryFactory<User>() {
      @Override
      public ParseQuery<User> create() {
        return User.query().whereExists(User.SPONSOR);
      }
    };
    Ordering<User> ordering = new Ordering<User>() {
      @Override
      public int compare(User left, User right) {
        return left.getSponsor().getOrdering() - right.getSponsor().getOrdering();
      }
    };
    mAdapter = new ParseAdapter<>(getActivity(), R.layout.adapter_contact, this, factory, ordering);

    setHasOptionsMenu(true);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    mLayout = (SwipeRefreshLayout) inflater.inflate(R.layout.fragment_concierge, null);

    mListView = (ListView) mLayout.findViewById(R.id.contact_list);
    mListView.setAdapter(mAdapter);

    mListView.setOnItemClickListener(this);

    if (User.canAdmin()) {
      mListView.setOnItemLongClickListener(this);
    }

    mAdapter.bindSync(mLayout);
    if (getArguments().getBoolean(MainActivity.SHOULD_SYNC, false)) mAdapter.onRefresh();

    return mLayout;
  }

  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    if (User.canAdmin()) {
      inflater.inflate(R.menu.fragment_concierge, menu);
    }
    super.onCreateOptionsMenu(menu, inflater);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.contact_new:
        UserEditDialogFragment.newInstance(null).show(getFragmentManager(), UserEditDialogFragment.TAG);
        return true;
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  public void fillView(ParseAdapter.ViewHolder holder, User contact) {
    TextView name = holder.get(R.id.contact_name);

//    LayerDrawable background = ((LayerDrawable) holder.get(R.id.announcement_card_header).getBackground());
//    background.findDrawableByLayerId(R.id.adapter_card_header_shape).setColorFilter(new LightingColorFilter(0, mColors[new Random().nextInt(20)]));
  }

  @Override
  public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
    AnnouncementEditDialogFragment.newInstance(mAdapter.getItem(i)).show(getFragmentManager(), AnnouncementEditDialogFragment.TAG);
  }

  @Override
  public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long l) {
    new AlertDialog.Builder(getActivity())
      .setTitle(R.string.confirm_delete)
      .setMessage(R.string.confirm_delete_message)
      .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
          mAdapter.getItem(position).deleteEventually();
          dialogInterface.dismiss();
        }
      })
      .setNegativeButton(android.R.string.cancel, null)
      .show();
    return true;
  }
}
