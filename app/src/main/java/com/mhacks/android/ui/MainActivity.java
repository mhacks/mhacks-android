package com.mhacks.android.ui;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import com.mhacks.android.data.model.Event;
import com.mhacks.android.ui.nav.AnnouncementsFragment;
import com.mhacks.android.ui.nav.AwardsFragment;
import com.mhacks.android.ui.nav.CountdownFragment;
import com.mhacks.android.ui.nav.EventDetailsFragment;
import com.mhacks.android.ui.nav.MapFragment;
import com.mhacks.android.ui.nav.NavigationDrawerFragment;
import com.mhacks.android.ui.nav.ScheduleFragment;
import com.mhacks.android.ui.nav.SponsorsFragment;
import org.mhacks.android.R;
import com.parse.ParseObject;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONException;
import org.json.JSONObject;

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

    private boolean pushEDF = false; //True if EventDetailsFragment was opened from a push notif.

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

        //Subscribe to Parse push notifications.
        ParsePush.subscribeInBackground("", new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d("com.parse.push", "successfully subscribed to the broadcast channel.");
                }
                else {
                    Log.e("com.parse.push", "failed to subscribe for push", e);
                }
            }
        });

        //Instantiate fragments
        countdownFragment = new CountdownFragment();
        announcementsFragment = new AnnouncementsFragment();
        scheduleFragment = new ScheduleFragment();
        sponsorsFragment = new SponsorsFragment();
        awardsFragment = new AwardsFragment();
        mapFragment = new MapFragment();

        setDefaultFragment();

        //Push notification intent check.
        checkIntent();
    }

    /**
     * Method to check whether the MainActivity was started from a push notification intent or not.
     * If it was, the method switched to the correct view based on the JSON payload from the push
     * notification.
     */
    public void checkIntent() {
        Intent intent = getIntent();
        if (intent.getExtras() != null && intent.getExtras().getString("com.parse.Data") != null) {
            try {
                JSONObject payload = new JSONObject(intent.getExtras().getString("com.parse.Data"));
                Log.d(TAG, payload.toString(4));
                if (payload.has("announcementID")) {
                    Log.d(TAG, payload.getString("announcementID"));
                    getFragmentManager().beginTransaction()
                                        .replace(R.id.main_container, announcementsFragment)
                                        .addToBackStack(null)
                                        .commit();
                    setToolbarTitle("Announcements");
                }
                else if (payload.has("eventID")) {
                    Log.d(TAG, payload.getString("eventID"));
                    String eventID = payload.getString("eventID");
                    ParseQuery<Event> query = ParseQuery.getQuery("Event");
                    query.include("category");
                    query.include("locations");
                    query.include("host");
                    query.getInBackground(eventID, new GetCallback<Event>() {
                        public void done(Event object, ParseException e) {
                            if (e == null) {
                                int color = getEventColor(object.getCategory().getColor());
                                EventDetailsFragment eventDetailsFragment =
                                        EventDetailsFragment.newInstance(object, color);
                                getFragmentManager().beginTransaction()
                                                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                                    .addToBackStack(null)
                                                    .add(R.id.drawer_layout, eventDetailsFragment)
                                                    .commit();

                                //Let's other methods know that this EventDetailsFragment was opened
                                //from a push notification.
                                pushEDF = true;
                            } else {
                                Toast.makeText(getApplicationContext(),
                                               "Unable to retrieve event. Check the schedule for updates.",
                                               Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Log.d(TAG, "neither string found");
                }
            }
            catch (JSONException e) {
                Log.e(TAG, "Fuck you JSON", e);
            }
        }
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
                fragmentTransaction.replace(R.id.main_container, countdownFragment)
                                   .addToBackStack(null).commit();
                setToolbarTitle("Countdown Timer");
                break;
            case 1:
                fragmentTransaction.replace(R.id.main_container, announcementsFragment)
                                   .addToBackStack(null).commit();
                setToolbarTitle("Announcements");
                break;
            case 2:
                fragmentTransaction.replace(R.id.main_container, scheduleFragment)
                                   .addToBackStack(null).commit();
                setToolbarTitle("Schedule");
                break;
            case 3:
                fragmentTransaction.replace(R.id.main_container, sponsorsFragment)
                                   .addToBackStack(null).commit();
                setToolbarTitle("Sponsors");
                break;
            case 4:
                fragmentTransaction.replace(R.id.main_container, awardsFragment)
                                   .addToBackStack(null).commit();
                setToolbarTitle("Awards");
                break;
            case 5:
                fragmentTransaction.replace(R.id.main_container, mapFragment)
                                   .addToBackStack(null).commit();
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
        } else if (getFragmentManager().getBackStackEntryCount() != 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    /**
     * Sets the default fragment to the CountdownFragment.
     */
    public void setDefaultFragment() {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_container, countdownFragment);
        fragmentTransaction.commit();

        setToolbarTitle("Countdown Timer");
    }

    /**
     * Handles all the clicks for the ScheduleFragment and it's fragments.
     * @param v clicked View
     */
    public void scheduleFragmentClick(View v) {
        if (v.getId() == R.id.event_close_button && pushEDF) {
            getFragmentManager().beginTransaction()
                                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                .remove(getFragmentManager().findFragmentById(R.id.drawer_layout))
                                .commit();
            pushEDF = false;
        } else scheduleFragment.scheduleFragmentClick(v);
    }

    /**
     * Takes the event type based on the EventType class in Parse and returns the corresponding
     * color of the event.
     * @param eventType Event type/category.
     * @return color of the event.
     */
    public int getEventColor(int eventType) {
        switch (eventType) {
            case 0: //Red
                return getResources().getColor(R.color.event_red);
            case 1: //Orange
                return getResources().getColor(R.color.event_orange);
            case 2: //Yellow
                return getResources().getColor(R.color.event_yellow);
            case 3: //Green
                return getResources().getColor(R.color.event_green);
            case 4: //Blue
                return getResources().getColor(R.color.event_blue);
            case 5: //Purple
                return getResources().getColor(R.color.event_purple);
            default:
                return getResources().getColor(R.color.mh_purple);
        }
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
