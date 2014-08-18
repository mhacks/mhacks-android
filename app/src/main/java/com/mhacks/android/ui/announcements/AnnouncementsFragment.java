package com.mhacks.android.ui.announcements;

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

import com.mhacks.android.R;
import com.mhacks.android.data.model.Announcement;
import com.mhacks.android.data.model.User;
import com.mhacks.android.ui.MainActivity;
import com.mhacks.android.ui.common.ParseAdapter;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

/**
 * Created by Damian Wieczorek <damianw@umich.edu> on 7/24/14.
 */
public class AnnouncementsFragment extends Fragment implements
  ParseAdapter.ListCallbacks<Announcement>,
  AdapterView.OnItemClickListener,
  AdapterView.OnItemLongClickListener {
  public static final String TAG = "AnnouncementsFragment";

  private ListView mListView;
  private SwipeRefreshLayout mLayout;
  private ParseAdapter<Announcement> mAdapter;

  private int[] mColors = new int[20];

  public AnnouncementsFragment() {
    super();
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    ParseQueryAdapter.QueryFactory<Announcement> factory = new ParseQueryAdapter.QueryFactory<Announcement>() {
      @Override
      public ParseQuery<Announcement> create() {
        return Announcement.query().orderByDescending(Announcement.CREATED_AT);
      }
    };
    mAdapter = new ParseAdapter<>(getActivity(), R.layout.adapter_announcement, this, factory);

    setHasOptionsMenu(true);

    mColors[0] = getResources().getColor(R.color.palette_0);
    mColors[1] = getResources().getColor(R.color.palette_1);
    mColors[2] = getResources().getColor(R.color.palette_2);
    mColors[3] = getResources().getColor(R.color.palette_3);
    mColors[4] = getResources().getColor(R.color.palette_4);
    mColors[5] = getResources().getColor(R.color.palette_5);
    mColors[6] = getResources().getColor(R.color.palette_6);
    mColors[7] = getResources().getColor(R.color.palette_7);
    mColors[8] = getResources().getColor(R.color.palette_8);
    mColors[9] = getResources().getColor(R.color.palette_9);
    mColors[10] = getResources().getColor(R.color.palette_10);
    mColors[11] = getResources().getColor(R.color.palette_11);
    mColors[12] = getResources().getColor(R.color.palette_12);
    mColors[13] = getResources().getColor(R.color.palette_13);
    mColors[14] = getResources().getColor(R.color.palette_14);
    mColors[15] = getResources().getColor(R.color.palette_15);
    mColors[16] = getResources().getColor(R.color.palette_16);
    mColors[17] = getResources().getColor(R.color.palette_17);
    mColors[18] = getResources().getColor(R.color.palette_18);
    mColors[19] = getResources().getColor(R.color.palette_19);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    mLayout = (SwipeRefreshLayout) inflater.inflate(R.layout.fragment_announcements, null);

    mListView = (ListView) mLayout.findViewById(R.id.announcements_list);
    mListView.setAdapter(mAdapter);

    if (User.canAdmin()) {
      mListView.setOnItemClickListener(this);
      mListView.setOnItemLongClickListener(this);
    }

    mAdapter.bindSync(mLayout);
    if (getArguments().getBoolean(MainActivity.SHOULD_SYNC, false)) mAdapter.onRefresh();

    return mLayout;
  }

  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    if (User.canAdmin()) {
      inflater.inflate(R.menu.fragment_announcements, menu);
    }
    super.onCreateOptionsMenu(menu, inflater);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.announcements_new:
        AnnouncementEditDialogFragment.newInstance(null).show(getFragmentManager(), AnnouncementEditDialogFragment.TAG);
        return true;
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  public void fillView(ParseAdapter.ViewHolder holder, Announcement announcement) {
    TextView title = holder.get(R.id.announcement_title);
    TextView details = holder.get(R.id.announcement_details);
    TextView poster = holder.get(R.id.announcement_poster);
    ImageView pinned = holder.get(R.id.announcement_pinned_icon);

    title.setText(announcement.getTitle());
    details.setText(announcement.getDetails());
    poster.setText(announcement.getPoster().getTitle());
    pinned.setVisibility(announcement.isPinned() ? View.VISIBLE : View.GONE);

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
