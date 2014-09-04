package com.mhacks.android.ui.nav;


import android.app.ActionBar;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.common.base.Function;
import com.google.common.collect.Maps;
import com.mhacks.android.R;
import com.mhacks.android.ui.announcements.AnnouncementsFragment;
import com.mhacks.android.ui.awards.AwardsFragment;
import com.mhacks.android.ui.chat.ChatFragment;
import com.mhacks.android.ui.concierge.ConciergeFragment;
import com.mhacks.android.ui.map.MapFragment;
import com.mhacks.android.ui.messages.ThreadsFragment;
import com.mhacks.android.ui.schedule.ScheduleFragment;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class NavigationDrawerFragment extends Fragment {

  private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";
  private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";

  public static final String ACTION_NAVIGATE = "com.mhacks.android.ACTION_NAVIGATE";
  public static final String NAV_ITEM_TAG = "NavigationDrawerFragment::navItemTag";
  public static final String FRAGMENT_ARGS = "NavigationDrawerFragment::fragmentArgs";

  private int mSlideDuration;
  private Handler mHandler;
  private NavigationDrawerCallbacks mCallbacks;

  private List<NavItem> mItems;
  private Map<String, NavItem> mItemsMap = Maps.newConcurrentMap();
  private ActionBarDrawerToggle mDrawerToggle;
  private NavItemAdapter mAdapter;

  private DrawerLayout mDrawerLayout;
  private ListView mDrawerListView;
  private View mFragmentContainerView;

  private TextView mTitleText;
  private LinearLayout.LayoutParams mTitleTextLayoutParams;
  private int mTitleMargin;

  private int mCurrentSelectedPosition = 0;
  private boolean mFromSavedInstanceState;
  private boolean mUserLearnedDrawer;

  private PendingSelection mPendingSelection;

  public static void navigateTo(String navItemTag, Bundle args, Context context) {
    context.sendBroadcast(new Intent(ACTION_NAVIGATE)
      .putExtra(NAV_ITEM_TAG, navItemTag)
      .putExtra(FRAGMENT_ARGS, args == null ? new Bundle() : args));
  }

  private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
      if (intent == null || !intent.hasExtra(NAV_ITEM_TAG) || !intent.hasExtra(FRAGMENT_ARGS)) return;
      String itemTag = intent.getStringExtra(NAV_ITEM_TAG);
      Bundle args = intent.getBundleExtra(FRAGMENT_ARGS);

      if (!mItemsMap.containsKey(itemTag)) return;
      openAndSelect(mItemsMap.get(itemTag), args);
    }
  };

  public NavigationDrawerFragment() {}

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
    mUserLearnedDrawer = sp.getBoolean(PREF_USER_LEARNED_DRAWER, false);

    if (savedInstanceState != null) {
      mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION);
      mFromSavedInstanceState = true;
    }

    FragmentActivity activity = getActivity();
    mItems = Arrays.asList(
      new NavItem(activity, AnnouncementsFragment.class, getString(R.string.announcements), R.drawable.ic_announcements2, R.color.bg_announcements, AnnouncementsFragment.TAG),
      new NavItem(activity, ConciergeFragment.class, getString(R.string.concierge), R.drawable.ic_concierge2, R.color.bg_concierge, ConciergeFragment.TAG),
      new NavItem(activity, ThreadsFragment.class, getString(R.string.messages), R.drawable.ic_hackers2, R.color.bg_hackers, ThreadsFragment.TAG),
      new NavItem(activity, ScheduleFragment.class, getString(R.string.schedule), R.drawable.ic_schedule2, R.color.bg_schedule, ScheduleFragment.TAG),
//      new NavItem(activity, HackersFragment.class, getString(R.string.hackers), R.drawable.ic_hackers2, R.color.bg_hackers, HackersFragment.TAG),
      new NavItem(activity, AwardsFragment.class, getString(R.string.awards), R.drawable.ic_awards2, R.color.bg_awards, AwardsFragment.TAG),
      new NavItem(activity, ChatFragment.class, getString(R.string.chat), R.drawable.ic_chat2, R.color.bg_chat, ChatFragment.TAG),
      new NavItem(activity, MapFragment.class, getString(R.string.map), R.drawable.ic_map2, R.color.bg_map, MapFragment.TAG)
    );

    mItemsMap = Maps.uniqueIndex(mItems, new Function<NavItem, String>() {
      @Override
      public String apply(NavItem input) {
        return input.getTag();
      }
    });

    activity.registerReceiver(mBroadcastReceiver, new IntentFilter(ACTION_NAVIGATE));

    mAdapter = new NavItemAdapter(activity, mItems);
    mHandler = new Handler();
    mSlideDuration = activity.getResources().getInteger(R.integer.nav_item_bg_slide_duration);

    int titleId = activity.getResources().getIdentifier("action_bar_title", "id", "android");
    mTitleText = (TextView) activity.findViewById(titleId);
    mTitleTextLayoutParams = (LinearLayout.LayoutParams) mTitleText.getLayoutParams();
    DisplayMetrics metrics = new DisplayMetrics();
    activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
    mTitleMargin = (int) (18 * (metrics.densityDpi / 160f));

    selectItem(mCurrentSelectedPosition);
  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    setHasOptionsMenu(true);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    mDrawerListView = (ListView) inflater.inflate(
      R.layout.fragment_navigation_drawer, container, false);
    mDrawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        selectItem(position);
      }
    });

    mDrawerListView.setAdapter(mAdapter);
    mDrawerListView.setItemChecked(mCurrentSelectedPosition, true);
    return mDrawerListView;
  }

  public boolean isDrawerOpen() {
    return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(mFragmentContainerView);
  }

  public void setUp(int fragmentId, DrawerLayout drawerLayout) {
    mFragmentContainerView = getActivity().findViewById(fragmentId);
    mDrawerLayout = drawerLayout;

    mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

    ActionBar actionBar = getActionBar();
    actionBar.setDisplayHomeAsUpEnabled(true);
    actionBar.setHomeButtonEnabled(true);

    mDrawerToggle = new ActionBarDrawerToggle(
      getActivity(),                    /* host Activity */
      mDrawerLayout,                    /* DrawerLayout object */
      R.drawable.ic_drawer,             /* nav drawer image to replace 'Up' caret */
      R.string.navigation_drawer_open,  /* "open drawer" description for accessibility */
      R.string.navigation_drawer_close  /* "close drawer" description for accessibility */
    ) {
      @Override
      public void onDrawerClosed(View drawerView) {
        super.onDrawerClosed(drawerView);
        if (!isAdded()) {
          return;
        }

        getActivity().invalidateOptionsMenu(); // calls onPrepareOptionsMenu()

        mTitleTextLayoutParams.leftMargin = 0;
        mTitleText.setLayoutParams(mTitleTextLayoutParams);
        mTitleText.requestLayout();
      }

      @Override
      public void onDrawerOpened(View drawerView) {
        super.onDrawerOpened(drawerView);
        if (!isAdded()) {
          return;
        }

        if (!mUserLearnedDrawer) {
          // The user manually opened the drawer; store this flag to prevent auto-showing
          // the navigation drawer automatically in the future.
          mUserLearnedDrawer = true;
          SharedPreferences sp = PreferenceManager
            .getDefaultSharedPreferences(getActivity());
          sp.edit().putBoolean(PREF_USER_LEARNED_DRAWER, true).apply();
        }

        getActivity().invalidateOptionsMenu(); // calls onPrepareOptionsMenu()

        mTitleTextLayoutParams.leftMargin = mTitleMargin;
        mTitleText.setLayoutParams(mTitleTextLayoutParams);
        mTitleText.requestLayout();

        if (mPendingSelection != null) {
          selectItem(mItems.indexOf(mPendingSelection.item));
        }
      }
    };

    if (!mUserLearnedDrawer && !mFromSavedInstanceState) {
      mDrawerLayout.openDrawer(mFragmentContainerView);
    }

    // Defer code dependent on restoration of previous instance state.
    mDrawerLayout.post(new Runnable() {
      @Override
      public void run() {
        mDrawerToggle.syncState();
      }
    });

    mDrawerLayout.setDrawerListener(mDrawerToggle);
  }

  private void selectItem(int position) {
    mAdapter.setSelection(position);
    if (mDrawerListView != null) {
      mDrawerListView.setItemChecked(position, true);
    }
    new DelayedSelector(position);
  }

  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);
    try {
      mCallbacks = (NavigationDrawerCallbacks) activity;
    } catch (ClassCastException e) {
      throw new ClassCastException("Activity must implement NavigationDrawerCallbacks.");
    }
  }

  @Override
  public void onDetach() {
    super.onDetach();
    mCallbacks = null;
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    getActivity().unregisterReceiver(mBroadcastReceiver);
  }

  @Override
  public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
  }

  @Override
  public void onConfigurationChanged(Configuration newConfig) {
    super.onConfigurationChanged(newConfig);
    // Forward the new configuration the drawer toggle component.
    mDrawerToggle.onConfigurationChanged(newConfig);
  }

  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    if (mDrawerLayout != null && isDrawerOpen()) {
      inflater.inflate(R.menu.global, menu);
      setHaveOptionsMenu(false);
      showGlobalContextActionBar();
    } else {
      setHaveOptionsMenu(true);
    }
    super.onCreateOptionsMenu(menu, inflater);
  }

  private void setHaveOptionsMenu(boolean haveOptionsMenu) {
    for (int i = 0, len = mAdapter.getCount(); i < len; i++) {
      mAdapter.getItem(i).setHasOptionsMenu(haveOptionsMenu);
    }
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    return mDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
  }

  public NavItem getItem(int position) {
    return mAdapter.getItem(position);
  }

  private void showGlobalContextActionBar() {
    ActionBar actionBar = getActionBar();
    actionBar.setDisplayShowTitleEnabled(true);
    actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
    actionBar.setTitle(R.string.app_name);
  }

  private ActionBar getActionBar() {
    return getActivity().getActionBar();
  }

  private void openAndSelect(NavItem item, Bundle args) {
    mPendingSelection = new PendingSelection(item, args);
    mDrawerLayout.openDrawer(mFragmentContainerView);
  }

  private static final class PendingSelection {
    public final NavItem item;
    public final Bundle args;

    public PendingSelection(NavItem item, Bundle args) {
      this.item = item;
      this.args = args;
    }
  }

  private class DelayedSelector implements Runnable {
    private final int position;
    public DelayedSelector(int position) {
      this.position = position;
      mHandler.postDelayed(this, mSlideDuration);
    }
    @Override
    public void run() {
      mCurrentSelectedPosition = position;
      if (mDrawerLayout != null) {
        mDrawerLayout.closeDrawer(mFragmentContainerView);
      }
      new DelayedNotifier(position);
    }
  }

  private class DelayedNotifier implements Runnable {
    private final int position;
    public DelayedNotifier(int position) {
      this.position = position;
      mHandler.postDelayed(this, mSlideDuration); // that's a total guess
    }

    @Override
    public void run() {
      if (mCallbacks != null) {
        Bundle args = null;
        if (mPendingSelection != null) {
          args = mPendingSelection.args;
          mPendingSelection = null;
        }
        mCallbacks.onNavigationDrawerItemSelected(getItem(position), args);
      }
    }
  }

  public static interface NavigationDrawerCallbacks {
    void onNavigationDrawerItemSelected(NavItem item, Bundle args);
  }
}
