package com.mhacks.android.ui;

import android.app.ActionBar;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.ListView;

import com.mhacks.android.ui.nav.NavigationDrawerFragment;
import com.mhacks.iv.android.R;

import java.util.Date;

/**
 * Created by Omkar Moghe on 10/22/2014.
 */
public class MainActivity extends FragmentActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    public static final String TAG = "MainActivity";

    public static final String SHOULD_SYNC = "sync";
    public static final String TIME_SAVED  = "time_saved";

    private NavigationDrawerFragment mNavigationDrawerFragment;
    private CharSequence             mTitle;

    private boolean mShouldSync = true;

    private ListView featuresListView;

    private String[] featuresList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = new NavigationDrawerFragment();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.navigation_drawer, mNavigationDrawerFragment);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mShouldSync = savedInstanceState.getBoolean(SHOULD_SYNC, false);
    }
/*
    @Override
    public void onNavigationDrawerItemSelected(NavItem item, Bundle args) {
        if (args == null) {
            args = new Bundle();
        }
        args.putBoolean(SHOULD_SYNC, mShouldSync);
        mTitle = item.getTitle();
        restoreActionBar();
        item.replace(R.id.container, args);
        invalidateOptionsMenu();
        mShouldSync = false;
    }
*/

    public void restoreActionBar(String title) {
        final ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setTitle(title);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SHOULD_SYNC, mShouldSync);
        outState.putLong(TIME_SAVED, new Date().getTime());
    }
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }
*/

}
