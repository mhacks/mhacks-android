package com.mhacks.android.ui.hackers;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.base.Strings;
import com.mhacks.android.ui.MainActivity;
import com.mhacks.android.ui.common.parse.ParseAdapter;
import com.mhacks.android.ui.common.parse.ViewHolder;
import com.mhacks.android.ui.concierge.UserEditDialogFragment;
import com.mhacks.iv.android.R;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

/**
 * Created by Damian Wieczorek <damianw@umich.edu> on 7/24/14.
 */
public class HackersFragment extends Fragment implements
  ParseAdapter.ListCallbacks<User>,
  AdapterView.OnItemClickListener,
  AdapterView.OnItemLongClickListener {
  public static final String TAG = "ConciergeFragment";

  private ListView mListView;
  private SwipeRefreshLayout mLayout;
  private ParseAdapter<User> mAdapter;

  public HackersFragment() {
    super();
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    ParseQueryAdapter.QueryFactory<User> factory = new ParseQueryAdapter.QueryFactory<User>() {
      @Override
      public ParseQuery<User> create() {
        return User.remoteQuery().addAscendingOrder(User.SCHOOL).addAscendingOrder(User.NAME);
      }
    };
    mAdapter = new ParseAdapter<>(getActivity(), R.layout.adapter_hacker, this, factory)
      .enablePagination(50, 10)
      .setSectioning(DataClass.equivalentOn(User.SCHOOL))
      .load();

    setHasOptionsMenu(true);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    mLayout = (SwipeRefreshLayout) inflater.inflate(R.layout.fragment_hackers, null);

    mListView = (ListView) mLayout.findViewById(R.id.hacker_list);
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
    inflater.inflate(R.menu.fragment_hackers, menu);
    mAdapter.prepareFilter(menu, R.id.menu_search, true, User.NAME, User.SCHOOL, User.MAJOR);

    super.onCreateOptionsMenu(menu, inflater);
  }

  @Override
  public void populateView(ViewHolder holder, User hacker, boolean hasSectionHeader, boolean hasSectionFooter) {
    View header = holder.get(R.id.hacker_card_header);
    View footer = holder.get(R.id.hacker_card_footer);
    TextView school = holder.get(R.id.hacker_school);
    TextView major = holder.get(R.id.hacker_major);
    TextView name = holder.get(R.id.hacker_name);

    header.setVisibility(hasSectionHeader ? View.VISIBLE : View.GONE);
    footer.setVisibility(hasSectionFooter ? View.VISIBLE : View.GONE);

    String s = hacker.getSchool();
    school.setText(Strings.isNullOrEmpty(s) ? getString(R.string.school_unknown) : s);
    major.setText(hacker.getMajor());
    name.setText(hacker.getName());
  }

  @Override
  public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
    Toast.makeText(getActivity(), R.string.not_implemented_yet, Toast.LENGTH_SHORT).show();
  }

  @Override
  public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long l) {
    UserEditDialogFragment.newInstance(mAdapter.getItem(position)).show(getFragmentManager(), UserEditDialogFragment.TAG);
    return true;
  }
}
