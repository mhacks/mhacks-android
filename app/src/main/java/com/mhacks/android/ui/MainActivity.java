package com.mhacks.android.ui;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;

import com.bugsnag.android.Bugsnag;
import com.mhacks.android.GeofenceBroadcastReceiver;
import com.mhacks.android.R;
import com.mhacks.android.data.model.User;
import com.mhacks.android.ui.nav.NavItem;
import com.mhacks.android.ui.nav.NavigationDrawerFragment;

import java.util.Date;


public class MainActivity extends FragmentActivity
  implements NavigationDrawerFragment.NavigationDrawerCallbacks {
  public static final String TAG = "MainActivity";

  public static final String SHOULD_SYNC = "sync";
  public static final String TIME_SAVED = "time_saved";

  private User mUser;
  private NavigationDrawerFragment mNavigationDrawerFragment;
  private CharSequence mTitle;

  private boolean mShouldSync = true;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    mUser = User.getCurrentUser();
    if (mUser == null) {
      startActivity(new Intent(this, LoginActivity.class));
      finish();
      return;
    } else {
      Bugsnag.setUser(mUser.getObjectId(), mUser.getEmail(), mUser.getName());
      mUser.setHasAndroid(true);
      mUser.saveEventually();
      GeofenceBroadcastReceiver.initialize(this);
    }

    mNavigationDrawerFragment = (NavigationDrawerFragment)
      getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
    mTitle = getTitle();

    // Set up the drawer.
    mNavigationDrawerFragment.setUp(
      R.id.navigation_drawer,
      (DrawerLayout) findViewById(R.id.drawer_layout));
  }

  @Override
  protected void onRestoreInstanceState(Bundle savedInstanceState) {
    super.onRestoreInstanceState(savedInstanceState);
    mShouldSync = savedInstanceState.getBoolean(SHOULD_SYNC, false);
  }

  @Override
  public void onNavigationDrawerItemSelected(NavItem item, Bundle args) {
    if (args == null) args = new Bundle();
    args.putBoolean(SHOULD_SYNC, mShouldSync);
    mTitle = item.getTitle();
    restoreActionBar();
    item.replace(R.id.container, args);
    invalidateOptionsMenu();
    mShouldSync = false;
  }

  public void restoreActionBar() {
    final ActionBar actionBar = getActionBar();
    actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
    actionBar.setDisplayShowTitleEnabled(true);
    actionBar.setTitle(mTitle);
  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putBoolean(SHOULD_SYNC, mShouldSync);
    outState.putLong(TIME_SAVED, new Date().getTime());
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    if (mUser == null) return false;
    if (!mNavigationDrawerFragment.isDrawerOpen()) {
      getMenuInflater().inflate(R.menu.main, menu);
      restoreActionBar();
      return true;
    }
    return super.onCreateOptionsMenu(menu);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();
    return id == R.id.action_settings || super.onOptionsItemSelected(item);
  }

}
