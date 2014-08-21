package com.mhacks.android.ui.awards;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.mhacks.android.R;
import com.mhacks.android.data.model.Award;
import com.mhacks.android.data.model.User;
import com.mhacks.android.ui.MainActivity;
import com.mhacks.android.ui.common.parse.ParseAdapter;
import com.mhacks.android.ui.common.parse.ViewHolder;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

/**
 * Created by Damian Wieczorek <damianw@umich.edu> on 7/24/14.
 */
public class AwardsFragment extends Fragment implements ParseAdapter.ListCallbacks<Award> {
  public static final String TAG = "AwardsFragment";

  private ListView mListView;
  private SwipeRefreshLayout mLayout;
  private ParseAdapter<Award> mAdapter;

  public AwardsFragment() {
    super();
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    ParseQueryAdapter.QueryFactory<Award> factory = new ParseQueryAdapter.QueryFactory<Award>() {
      @Override
      public ParseQuery<Award> create() {
        return Award.query().orderByDescending(Award.CREATED_AT);
      }
    };
    mAdapter = new ParseAdapter<>(getActivity(), R.layout.adapter_award, this, factory);

    setHasOptionsMenu(true);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    mLayout = (SwipeRefreshLayout) inflater.inflate(R.layout.fragment_award, null);

    mListView = (ListView) mLayout.findViewById(R.id.awards_list);
    mListView.setAdapter(mAdapter);

    mAdapter.bindSync(mLayout);
    if (getArguments().getBoolean(MainActivity.SHOULD_SYNC, false)) mAdapter.onRefresh();

    return mLayout;
  }

  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    if (User.getCurrentUser().isAdmin()) {
      inflater.inflate(R.menu.fragment_awards, menu);
    }
    super.onCreateOptionsMenu(menu, inflater);
  }

  @Override
  public void populateView(ViewHolder holder, Award award, boolean hasSectionHeader, boolean hasSectionFooter) {
    TextView title = holder.get(R.id.award_title);
    TextView details = holder.get(R.id.award_details);
    TextView sponsor = holder.get(R.id.award_sponsor);
    TextView prize = holder.get(R.id.award_prize);

    title.setText(award.getTitle());
    details.setText(award.getDetails());
    sponsor.setText(award.getSponsor().getTitle());
    prize.setText(award.getPrize());
  }

}
