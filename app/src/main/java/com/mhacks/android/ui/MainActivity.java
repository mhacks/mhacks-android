package com.mhacks.android.ui;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Toast;

import com.mhacks.android.data.model.Announcement;
import com.mhacks.android.data.model.User;
import com.mhacks.android.data.network.HackathonCallback;
import com.mhacks.android.data.network.NetworkManager;
import com.mhacks.android.data_old.model.Event;
import com.mhacks.android.ui.announcements.AnnouncementsFragment;
import com.mhacks.android.ui.awards.AwardsFragment;
import com.mhacks.android.ui.countdown.CountdownFragment;
import com.mhacks.android.ui.events.EventDetailsFragment;
import com.mhacks.android.ui.map.MapFragment;
import com.mhacks.android.ui.events.ScheduleFragment;
import com.mhacks.android.ui.sponsors.SponsorsFragment;
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
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONException;
import org.json.JSONObject;
import org.mhacks.android.R;

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

    private ParseUser mUser;

    // Toolbar
    private Toolbar mToolbar;

    // Navigation Drawer
    private Drawer                mDrawer;

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

        updateFragment(countdownFragment);

        //Push notification intent check.
        checkIntent();

        // testing the network manager
        final NetworkManager networkManager = NetworkManager.getInstance();
        networkManager.logUserIn("admin@admin.com", "admin", new HackathonCallback<User>() {
            @Override
            public void success(User response) {
                Log.d(TAG, "log in successful");

                networkManager.getAnnouncements(new HackathonCallback<List<Announcement>>() {
                    @Override
                    public void success(List<Announcement> response) {
                        Toast.makeText(getApplicationContext(), response.size() + " events retrieved.", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, response.size() + " events retrieved.");
                    }

                    @Override
                    public void failure(Throwable error) {
                        Log.d(TAG, "fuck all of this");
                    }
                });
            }

            @Override
            public void failure(Throwable error) {
                Log.d(TAG, "dishonor whole family");
            }
        });

        buildNavigationDrawer();
    }

    /**
     * Method to build the navigation drawer and set up it's behaviors and styling.
     * Interface callbacks for the navigation drawer are within this method.
     */
    private void buildNavigationDrawer() {
        // Drawer items
        PrimaryDrawerItem countdown = new PrimaryDrawerItem().withName("Countdown")
                                                             .withIcon(R.drawable.ic_time)
                                                             .withSelectedColorRes(R.color.primary_dark);
        PrimaryDrawerItem announcements = new PrimaryDrawerItem().withName("Announcements")
                                                                 .withIcon(R.drawable.ic_announcement)
                                                                 .withSelectedTextColorRes(R.color.primary_dark);
        PrimaryDrawerItem events = new PrimaryDrawerItem().withName("Events")
                                                          .withIcon(R.drawable.ic_event)
                                                          .withSelectedTextColorRes(R.color.primary_dark);
        PrimaryDrawerItem contacts = new PrimaryDrawerItem().withName("Contacts")
                                                            .withIcon(R.drawable.ic_contact)
                                                            .withSelectedTextColorRes(R.color.primary_dark);
        PrimaryDrawerItem awards = new PrimaryDrawerItem().withName("Awards")
                                                          .withIcon(R.drawable.ic_award)
                                                          .withSelectedTextColorRes(R.color.primary_dark);
        PrimaryDrawerItem map = new PrimaryDrawerItem().withName("Map")
                                                       .withIcon(R.drawable.ic_location)
                                                       .withSelectedTextColorRes(R.color.primary_dark);
        SecondaryDrawerItem settings = new SecondaryDrawerItem().withName("Settings")
                                                                .withIcon(R.drawable.ic_settings)
                                                                .withSelectedTextColorRes(R.color.primary_dark);

        // User profile
        ProfileDrawerItem userProfile = new ProfileDrawerItem().withName("User_Name")
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
                .addDrawerItems(countdown, announcements, events, contacts, awards, map,
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
                        updateFragment(sponsorsFragment);
                        break;
                    case 5:
                        updateFragment(awardsFragment);
                        break;
                    case 6:
                        updateFragment(mapFragment);
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
