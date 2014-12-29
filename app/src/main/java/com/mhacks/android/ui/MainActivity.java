package com.mhacks.android.ui;

import android.app.ActionBar;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.bugsnag.android.Bugsnag;
import com.mhacks.android.data.model.Announcement;
import com.mhacks.android.data.model.Award;
import com.mhacks.android.data.model.CountdownItem;
import com.mhacks.android.data.model.Event;
import com.mhacks.android.data.model.EventType;
import com.mhacks.android.data.model.Location;
import com.mhacks.android.data.model.Sponsor;
import com.mhacks.android.data.model.SponsorTier;
import com.mhacks.android.ui.nav.AnnouncementsFragment;
import com.mhacks.android.ui.nav.AwardsFragment;
import com.mhacks.android.ui.nav.CountdownFragment;
import com.mhacks.android.ui.nav.NavigationDrawerFragment;
import com.mhacks.android.ui.nav.ScheduleFragment;
import com.mhacks.android.ui.nav.SponsorsFragment;
import com.mhacks.iv.android.R;
import com.parse.Parse;
import com.parse.ParseBroadcastReceiver;
import com.parse.ParseFacebookUtils;
import com.parse.ParseObject;
import com.parse.ParseTwitterUtils;
import com.parse.ParseUser;
import com.parse.PushService;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Omkar Moghe on 10/22/2014.
 */
public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    public static final String TAG = "MainActivity";

    public static final String SHOULD_SYNC = "sync";
    public static final String TIME_SAVED  = "time_saved";

    private NavigationDrawerFragment mNavigationDrawerFragment;
    private CharSequence             mTitle;

    private ParseUser mUser;

    private DrawerLayout mDrawerLayout;

    private boolean mShouldSync = true;

    //Fragments
    private CountdownFragment countdownFragment;
    private AnnouncementsFragment announcementsFragment;
    private ScheduleFragment scheduleFragment;
    private SponsorsFragment sponsorsFragment;
    private AwardsFragment awardsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Add the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        //Creating navigation drawer from fragment.
        mNavigationDrawerFragment = new NavigationDrawerFragment();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.navigation_drawer, mNavigationDrawerFragment);

        mUser = ParseUser.getCurrentUser();

        //Instantiate fragments
        countdownFragment = new CountdownFragment();
        announcementsFragment = new AnnouncementsFragment();
        scheduleFragment = new ScheduleFragment();
        sponsorsFragment = new SponsorsFragment();
        awardsFragment = new AwardsFragment();

        setDefaultFragment();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mShouldSync = savedInstanceState.getBoolean(SHOULD_SYNC, false);
    }

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

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        switch (position) {
            case 0:
                countdownFragment = new CountdownFragment();
                fragmentTransaction.replace(R.id.main_container, countdownFragment);
                fragmentTransaction.commit();
                restoreActionBar("Countdown Timer");
                break;
            case 1:
                announcementsFragment = new AnnouncementsFragment();
                fragmentTransaction.replace(R.id.main_container, announcementsFragment);
                fragmentTransaction.commit();
                restoreActionBar("Announcements");
                break;
            case 2:
                scheduleFragment = new ScheduleFragment();
                fragmentTransaction.replace(R.id.main_container, scheduleFragment);
                fragmentTransaction.commit();
                restoreActionBar("Schedule");
                break;
            case 3:
                sponsorsFragment = new SponsorsFragment();
                fragmentTransaction.replace(R.id.main_container, sponsorsFragment);
                fragmentTransaction.commit();
                restoreActionBar("Sponsors");
                break;
            case 4:
                awardsFragment = new AwardsFragment();
                fragmentTransaction.replace(R.id.main_container, awardsFragment);
                fragmentTransaction.commit();
                restoreActionBar("Awards");
                break;
        }

        if (mDrawerLayout != null){
            mDrawerLayout.closeDrawer(findViewById(R.id.navigation_drawer));
        }
    }

    /**
     * Sets the default fragment to the CountdownFragment.
     */
    public void setDefaultFragment() {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        CountdownFragment countdownFragment = new CountdownFragment();
        fragmentTransaction.replace(R.id.main_container, countdownFragment);
        fragmentTransaction.commit();
        restoreActionBar("Countdown Timer");
    }

    /**
     * Handles all the clicks for the ScheduleFragment and it's fragments.
     * @param v clicked View
     */
    public void scheduleFragmentClick(View v) {
        scheduleFragment.scheduleFragmentClick(v);
    }
}
