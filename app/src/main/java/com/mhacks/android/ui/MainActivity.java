package com.mhacks.android.ui;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import com.google.android.gms.gcm.GcmPubSub;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.mhacks.android.data.model.Announcement;
import com.mhacks.android.data.model.User;
import com.mhacks.android.data.network.HackathonCallback;
import com.mhacks.android.data.network.NetworkManager;
import com.mhacks.android.data.network.gcm.MyInstanceIDListenerService;
import com.mhacks.android.ui.announcements.AnnouncementsFragment;
import com.mhacks.android.ui.countdown.CountdownFragment;
import com.mhacks.android.ui.events.ScheduleFragment;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.parse.ParseException;
import com.parse.ParsePush;
import com.parse.SaveCallback;

import org.mhacks.android.R;

import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * Created by Omkar Moghe on 10/22/2014.
 */
public class MainActivity extends AppCompatActivity {
    // TODO: REMOVE ALL PARSE STUFF AND MOVE TO NEW BACKEND / GCM
    public static final String TAG = "MainActivity";

    public static final String SHOULD_SYNC = "sync";
    public static final String TIME_SAVED  = "time_saved";

    private static final int OVERLAY_FADE_DURATION = 400;

    // Toolbar
    private Toolbar mToolbar;

    // CurrentUser
    private User mUser;

    // Navigation Drawer
    private Drawer                mDrawer;

    private boolean mShouldSync = true;

    private boolean pushEDF = false; //True if EventDetailsFragment was opened from a push notif.

    //Fragments
    private CountdownFragment countdownFragment;
    private AnnouncementsFragment announcementsFragment;
    private ScheduleFragment scheduleFragment;

    //GCM
    private GoogleCloudMessaging gcm;
    private GcmPubSub pubSub;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Add the toolbar
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }

        //Instantiate fragments
        countdownFragment = new CountdownFragment();
        announcementsFragment = new AnnouncementsFragment();
        scheduleFragment = new ScheduleFragment();

        updateFragment(countdownFragment);

        gcm = GoogleCloudMessaging.getInstance(this);

        final NetworkManager networkManager = NetworkManager.getInstance();
        networkManager.logUserIn("omoghe@umich.edu", "kanye2020", new HackathonCallback<User>() {
            @Override
            public void success(User response) {
                mUser = response;
                buildNavigationDrawer();

                networkManager.getAnnouncements(new HackathonCallback<List<Announcement>>() {
                    @Override
                    public void success(List<Announcement> response) {
                        Log.d(TAG, "" + response.size());
                        for (Announcement a : response) {
                            Log.d(TAG, a.getName());
                        }
                    }

                    @Override
                    public void failure(Throwable error) {

                    }
                });
            }

            @Override
            public void failure(Throwable error) {

            }
        });
    }

    /**
     * Method to build the navigation drawer and set up it's behaviors and styling.
     * Interface callbacks for the navigation drawer are within this method.
     */
    private void buildNavigationDrawer() {
        // Drawer items
        PrimaryDrawerItem countdown = new PrimaryDrawerItem().withName("Countdown")
                                                             .withIcon(R.drawable.ic_time)
                                                             .withSelectedTextColorRes(R.color.primary_dark);
        PrimaryDrawerItem announcements = new PrimaryDrawerItem().withName("Announcements")
                                                                 .withIcon(R.drawable.ic_announcement)
                                                                 .withSelectedTextColorRes(R.color.primary_dark);
        PrimaryDrawerItem events = new PrimaryDrawerItem().withName("Events")
                                                          .withIcon(R.drawable.ic_event)
                                                          .withSelectedTextColorRes(R.color.primary_dark);
        PrimaryDrawerItem map = new PrimaryDrawerItem().withName("Map")
                                                       .withIcon(R.drawable.ic_location)
                                                       .withSelectedTextColorRes(R.color.primary_dark);
        SecondaryDrawerItem settings = new SecondaryDrawerItem().withName("Settings")
                                                                .withIcon(R.drawable.ic_settings)
                                                                .withSelectedTextColorRes(R.color.primary_dark);

        // User profile
        String userName = (mUser != null) ? mUser.firstName + " " + mUser.lastName : "User_Name";
        ProfileDrawerItem userProfile = new ProfileDrawerItem().withName(userName)
                                                               .withTextColorRes(R.color.black);
        userProfile.withSelectedColorRes(R.color.primary_dark);

        // Account Header
        AccountHeader accountHeader = new AccountHeaderBuilder()
                .withActivity(this)
                .addProfiles(userProfile)
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view,
                                                    IProfile profile,
                                                    boolean currentProfile) {
                        return true;
                    }
                })
                .withTextColorRes(R.color.black)
                .build();

        // Build drawer
        mDrawer = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(mToolbar)
                .withAccountHeader(accountHeader)
                .addDrawerItems(countdown, announcements, events, map,
                                new DividerDrawerItem(),
                                settings)
                .build();

        // Configure item selection listener
        mDrawer.setOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
            @Override
            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                Log.d(TAG, "nav position: " + position);

                // switch 'i' aka position of item
                // indexing starts at 1 for some reason... probably because of the account header
                switch (position) {
                    case 1:
                        updateFragment(countdownFragment);
                        break;
                    case 2:
                        updateFragment(announcementsFragment);
                        break;
                    case 3:
                        updateFragment(scheduleFragment);
                        break;
                    case 4:
                        //updateFragment(mapViewFragment);
                        break;
                    default:
                        return false;
                }

                mDrawer.closeDrawer();
                return true;
            }
        });

    }

    /**
     * Updates the main_fragment_container with the given fragment.
     * @param fragment fragment to replace the main container with
     */
    private void updateFragment(Fragment fragment) {
        if (fragment == null) return; // only used for pre-release while fragments are not finalized

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
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

    @Override
    public void onBackPressed() {
        if (mDrawer.isDrawerOpen()) {
            mDrawer.closeDrawer();
        } else if (getFragmentManager().getBackStackEntryCount() != 0) {
            getFragmentManager().popBackStack();
            if (scheduleFragment.getEventDetailsOpened()) scheduleFragment.setEventDetailsOpened(false);
        } else {
            super.onBackPressed();
        }
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
                return getResources().getColor(R.color.mh_yellow);
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
