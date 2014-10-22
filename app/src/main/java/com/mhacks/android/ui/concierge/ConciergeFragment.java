package com.mhacks.android.ui.concierge;

import android.content.Intent;
import android.graphics.LightingColorFilter;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.mhacks.android.ui.MainActivity;
import com.mhacks.android.ui.common.parse.ParseAdapter;
import com.mhacks.android.ui.common.parse.ViewHolder;
import com.mhacks.android.ui.messages.ThreadsFragment;
import com.mhacks.android.ui.nav.NavigationDrawerFragment;
import com.mhacks.iv.android.R;
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
        return User.query().whereExists(User.SPONSOR).addAscendingOrder(User.NAME);
      }
    };
    Ordering<User> ordering = new Ordering<User>() {
      @Override
      public int compare(User left, User right) {
        return left.getSponsor().getOrdering() - right.getSponsor().getOrdering();
      }
    };
    mAdapter = new ParseAdapter<>(getActivity(), R.layout.adapter_contact, this, factory)
      .setOrdering(ordering)
      .setSectioning(DataClass.equivalentOn(User.SPONSOR))
      .load();

    setHasOptionsMenu(true);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_concierge, container, false);
    mLayout = (SwipeRefreshLayout) view.findViewById(R.id.concierge_swipe_container);

    mListView = (ListView) view.findViewById(R.id.contact_list);
    mListView.setAdapter(mAdapter);

    mListView.setOnItemClickListener(this);

    if (User.canAdmin()) {
      mListView.setOnItemLongClickListener(this);
    }

    mAdapter.bindSync(mLayout);
    if (getArguments().getBoolean(MainActivity.SHOULD_SYNC, false)) mAdapter.onRefresh();

    return view;
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
  public void populateView(ViewHolder holder, User contact, boolean hasSectionHeader, boolean hasSectionFooter) {
    View header = holder.get(R.id.contact_card_header);
    View footer = holder.get(R.id.contact_card_footer);
    ImageView icon = holder.get(R.id.contact_icon);
    TextView sponsorName = holder.get(R.id.contact_sponsor_name);
    TextView name = holder.get(R.id.contact_name);
    TextView position = holder.get(R.id.contact_specialty);

    if (contact.hasAndroid()) {
      icon.setImageResource(R.drawable.ic_phone);
      icon.setVisibility(View.VISIBLE);
    }
    else if (contact.has(User.EMAIL)) {
      icon.setImageResource(R.drawable.ic_email);
      icon.setVisibility(View.VISIBLE);
    }
    else if (contact.has(User.TWITTER_HANDLE)) {
      icon.setImageResource(R.drawable.ic_twitter);
      icon.setVisibility(View.VISIBLE);
    }
    else {
      icon.setVisibility(View.GONE);
    }

    header.setVisibility(hasSectionHeader ? View.VISIBLE : View.GONE);
    footer.setVisibility(hasSectionFooter ? View.VISIBLE : View.GONE);

    sponsorName.setText(contact.getSponsor().getTitle());
    name.setText(contact.getName());
    position.setText(contact.getSpecialty());

    LayerDrawable background = ((LayerDrawable) holder.get(R.id.contact_card_header).getBackground());
    background.findDrawableByLayerId(R.id.adapter_card_header_shape).setColorFilter(new LightingColorFilter(0, contact.getSponsor().getColor()));
  }

  @Override
  public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
    User user = mAdapter.getItem(i);

    if (user.hasAndroid()) {
      Bundle args = new Bundle();
      args.putParcelable(ThreadsFragment.PARTNER, user);
      NavigationDrawerFragment.navigateTo(ThreadsFragment.TAG, args, getActivity());
    }
    else if (user.has(User.TWITTER_HANDLE)) {
      String tweet = getString(R.string.concierge_tweet, user.getTwitterHandle());
      String tweetUrl = getString(R.string.tweet_url, tweet);
      Uri uri = Uri.parse(tweetUrl);
      startActivity(new Intent(Intent.ACTION_VIEW, uri));
    }
    else if (user.has(User.EMAIL)) {
      startActivity(new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", user.getEmail(), null))
        .putExtra(Intent.EXTRA_SUBJECT, getString(R.string.mhacks_concierge)));
    }
  }

  @Override
  public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long l) {
    UserEditDialogFragment.newInstance(mAdapter.getItem(position)).show(getFragmentManager(), UserEditDialogFragment.TAG);
    return true;
  }
}
