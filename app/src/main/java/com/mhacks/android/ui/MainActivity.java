package com.mhacks.android.ui;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import com.mhacks.android.ui.nav.AnnouncementsFragment;
import com.mhacks.android.ui.nav.AwardsFragment;
import com.mhacks.android.ui.nav.CountdownFragment;
import com.mhacks.android.ui.nav.MapFragment;
import com.mhacks.android.ui.nav.NavigationDrawerFragment;
import com.mhacks.android.ui.nav.ScheduleFragment;
import com.mhacks.android.ui.nav.SponsorsFragment;
import com.mhacks.iv.android.R;
import com.parse.ParseUser;

import java.util.Date;

/**
 * Created by Omkar Moghe on 10/22/2014.
 */
public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    public static final String TAG = "MainActivity";

    public static final String SHOULD_SYNC = "sync";
    public static final String TIME_SAVED  = "time_saved";

    private static final int OVERLAY_FADE_DURATION = 400;

    private NavigationDrawerFragment mNavigationDrawerFragment;

    private ParseUser mUser;

    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    private boolean mShouldSync = true;

    //Fragments
    private CountdownFragment countdownFragment;
    private AnnouncementsFragment announcementsFragment;
    private ScheduleFragment scheduleFragment;
    private SponsorsFragment sponsorsFragment;
    private AwardsFragment awardsFragment;
    private MapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Add the toolbar
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }

        // Get the DrawerLayout to set up the drawer
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        // Set a drawerToggle to link the toolbar with the drawer
        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                mToolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        //Creating navigation drawer from fragment.
        mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager().findFragmentById(R.id.navigation_drawer);

        mUser = ParseUser.getCurrentUser();

        //Instantiate fragments
        countdownFragment = new CountdownFragment();
        announcementsFragment = new AnnouncementsFragment();
        scheduleFragment = new ScheduleFragment();
        sponsorsFragment = new SponsorsFragment();
        awardsFragment = new AwardsFragment();
        mapFragment = new MapFragment();

        setDefaultFragment();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mShouldSync = savedInstanceState.getBoolean(SHOULD_SYNC, false);
    }

    public void setToolbarTitle(String title) {
        mToolbar.setTitle(title);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SHOULD_SYNC, mShouldSync);
        outState.putLong(TIME_SAVED, new Date().getTime());
    }

    // After this are functions for the Drawer
    @Override
    public void onNavigationDrawerItemSelected(int position) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        switch (position) {
            case 0:
                countdownFragment = new CountdownFragment();
                fragmentTransaction.replace(R.id.main_container, countdownFragment).commit();
                setToolbarTitle("Countdown Timer");
                break;
            case 1:
                announcementsFragment = new AnnouncementsFragment();
                fragmentTransaction.replace(R.id.main_container, announcementsFragment);
                fragmentTransaction.commit();
                setToolbarTitle("Announcements");
                break;
            case 2:
                scheduleFragment = new ScheduleFragment();
                fragmentTransaction.replace(R.id.main_container, scheduleFragment).commit();
                setToolbarTitle("Schedule");
                break;
            case 3:
                sponsorsFragment = new SponsorsFragment();
                fragmentTransaction.replace(R.id.main_container, sponsorsFragment).commit();
                setToolbarTitle("Sponsors");
                break;
            case 4:
                awardsFragment = new AwardsFragment();
                fragmentTransaction.replace(R.id.main_container, awardsFragment).commit();
                setToolbarTitle("Awards");
                break;
            case 5:
                mapFragment = new MapFragment();
                fragmentTransaction.replace(R.id.main_container, mapFragment).commit();
                setToolbarTitle("Map");
        }

        if (mDrawerLayout != null){
            mDrawerLayout.closeDrawer(findViewById(R.id.navigation_drawer));
        }
    }

    public void closeDrawer() {
        if (mDrawerLayout != null){
            mDrawerLayout.closeDrawer(findViewById(R.id.navigation_drawer));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(Gravity.START | Gravity.LEFT)) {
            mDrawerLayout.closeDrawers();
            return;
        }
        super.onBackPressed();
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

        setToolbarTitle("Countdown Timer");
    }

    /**
     * Handles all the clicks for the ScheduleFragment and it's fragments.
     * @param v clicked View
     */
    public void scheduleFragmentClick(View v) {
        scheduleFragment.scheduleFragmentClick(v);
    }

    /**
     * These are for when the remote data can't be fetched
     * and there's nothing in the local cache
     */

    public void showNoInternetOverlay() {
        final View noInternetOverlay = findViewById(R.id.no_internet_overlay);
        if (noInternetOverlay.getVisibility() == View.VISIBLE) {
            return;
        }

        noInternetOverlay.setAlpha(1.0f);
        noInternetOverlay.setVisibility(View.VISIBLE);
        noInternetOverlay.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        Animation fadeIn = new AlphaAnimation(0.0f, 1.0f);
        fadeIn.setDuration(OVERLAY_FADE_DURATION);
        noInternetOverlay.startAnimation(fadeIn);
    }

    public void hideNoInternetOverlay() {
        final View noInternetOverlay = findViewById(R.id.no_internet_overlay);
        if (noInternetOverlay.getVisibility() == View.GONE) {
            return;
        }

        noInternetOverlay.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });

        Animation fadeOut = new AlphaAnimation(1.0f, 0.0f);
        fadeOut.setDuration(OVERLAY_FADE_DURATION);
        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                noInternetOverlay.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        noInternetOverlay.startAnimation(fadeOut);
    }
}
