package com.mhacks.android.ui;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.webkit.WebView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.mhacks.android.data.model.Token;
import com.mhacks.android.data.model.User;
import com.mhacks.android.data.network.HackathonCallback;
import com.mhacks.android.data.network.NetworkManager;
import com.mhacks.android.ui.announcements.AnnouncementsFragment;
import com.mhacks.android.ui.countdown.CountdownFragment;
import com.mhacks.android.ui.events.ScheduleFragment;
import com.mhacks.android.ui.map.MapViewFragment;
import com.mhacks.android.ui.settings.SettingsFragment;
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

import org.mhacks.android.R;

import java.io.IOException;
import java.util.Date;
import java.util.Set;

/**
 * Created by Omkar Moghe on 10/22/2014.
 */
public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";

    public static final String SHOULD_SYNC = "sync";
    public static final String TIME_SAVED  = "time_saved";

    private static final int OVERLAY_FADE_DURATION = 400;

    // Toolbar
    private Toolbar mToolbar;

    // CurrentUser
    private User mUser;

    private boolean val;

    // Navigation Drawer
    private Drawer                mDrawer;

    private boolean mShouldSync = true;

    //Fragments
    private CountdownFragment countdownFragment;
    private AnnouncementsFragment announcementsFragment;
    private ScheduleFragment scheduleFragment;
    private SettingsFragment settingsFragment;
    private MapViewFragment mapViewFragment;

    //GCM
    private GoogleCloudMessaging gcm;
    String regid;
    String PROJECT_NUMBER;
    String notif;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // Add the toolbar
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }

        // If Activity opened from push notification, value will reflect fragment that will initially open
        notif = getIntent().getStringExtra("notif_link");
        PROJECT_NUMBER = getString(R.string.gcm_server_id);

        //Instantiate fragments
        countdownFragment = new CountdownFragment();
        announcementsFragment = new AnnouncementsFragment();
        scheduleFragment = new ScheduleFragment();
        settingsFragment = new SettingsFragment();
        mapViewFragment = new MapViewFragment();

        updateFragment(countdownFragment, false);

        if (notif != null){
            // Opens Announcements
            if (notif.equals("Announcements")){
                updateFragment(announcementsFragment, true);
            }
        }

        if (checkPlayServices()){
            // Grabs the Google Cloud Messager REG ID and sends it to the backend
            getRegId();
        }
        else {
            Log.e(TAG, "No valid Google Play Services APK found.");
        }


    }

    public void getRegId(){
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
                    }
                    // reg_id
                    Bundle data = new Bundle();

                    regid = gcm.register(PROJECT_NUMBER);

                    data.putString("regid",regid);
                    /*InstanceID instanceID = InstanceID.getInstance(getApplicationContext());
                    String token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
                            GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);*/

                    final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    String gcmPush = sharedPref.getString("gcm", "");
                    Set<String> channels =  sharedPref.getStringSet(SettingsFragment.PUSH_NOTIFICATION_CHANNELS, null);

                    int pref = 1;
                    if (channels != null) {
                        String[] channelPrefs = channels.toArray(new String[channels.size()]);
                        for (int i = 0; i < channelPrefs.length; ++i) {
                            pref += Integer.parseInt(channelPrefs[i]);
                        }
                    } else pref = 63;

                    final Token token = new Token(regid, pref);
                    Log.d(TAG, gcmPush);
                    Log.d(TAG, "" + pref);
                    if (!gcmPush.equals(regid)) {
                        NetworkManager networkManager = NetworkManager.getInstance();
                        networkManager.sendToken(token, new HackathonCallback<Token>() {
                            @Override
                            public void success(Token response) {
                                Log.d(TAG, "gcm sent successfully");
                                sharedPref.edit().putString("gcm", regid).apply();
                            }

                            @Override
                            public void failure(Throwable error) {
                                Log.e(TAG, "gcm didnt work", error);
                            }
                        });
                    } else {
                        NetworkManager networkManager = NetworkManager.getInstance();
                        networkManager.updateToken(token, new HackathonCallback<Token>() {
                            @Override
                            public void success(Token response) {
                                Log.d(TAG, "notification preferences updated");
                            }

                            @Override
                            public void failure(Throwable error) {

                            }
                        });
                    }

                    msg = "Device registered, reg id =" + regid;
                    Log.i("GCM",  msg);

                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                    Log.i("GCMError",  msg);

                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {

            }
        }.execute(null, null, null);

        // Log in if possible.
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String username = sharedPref.getString(SettingsFragment.USERNAME_KEY,  "");
        String password = sharedPref.getString(SettingsFragment.PASSWORD_KEY, "");
        final NetworkManager networkManager = NetworkManager.getInstance();
        if (username.length() != 0 && password.length() != 0) {
            networkManager.logUserIn(username, password, new HackathonCallback<User>() {
                @Override
                public void success(User response) {
                    mUser = response;
                    buildNavigationDrawer();
                }

                @Override
                public void failure(Throwable error) {
                    buildNavigationDrawer();
                }
            });
        } else buildNavigationDrawer();
    }

    /**
     * Method to build the navigation drawer and set up it's behaviors and styling.
     * Interface callbacks for the navigation drawer are within this method.
     */
    private void buildNavigationDrawer() {
        // Drawer items
        PrimaryDrawerItem countdown = new PrimaryDrawerItem().withName("Countdown")
                                                             .withIcon(R.drawable.ic_time)
                                                             .withSelectedTextColorRes(R.color.primary);
        PrimaryDrawerItem announcements = new PrimaryDrawerItem().withName("Announcements")
                                                                 .withIcon(R.drawable.ic_announcement)
                                                                 .withSelectedTextColorRes(R.color.primary);
        PrimaryDrawerItem events = new PrimaryDrawerItem().withName("Events")
                                                          .withIcon(R.drawable.ic_event)
                                                          .withSelectedTextColorRes(R.color.primary);
        PrimaryDrawerItem map = new PrimaryDrawerItem().withName("Map")
                                                       .withIcon(R.drawable.ic_location)
                                                       .withSelectedTextColorRes(R.color.primary);
        SecondaryDrawerItem settings = new SecondaryDrawerItem().withName("Settings")
                                                                .withIcon(R.drawable.ic_settings)
                                                                .withSelectedTextColorRes(R.color.primary);

        // User profile
        String userName = (mUser != null) ? mUser.firstName + " " + mUser.lastName : "MHacks: Refactor";
        ProfileDrawerItem userProfile = new ProfileDrawerItem().withName(userName)
                                                               .withTextColorRes(R.color.black)
                                                               .withSelectedColorRes(R.color.primary)
                                                               .withIcon(getResources().getDrawable(R.mipmap.launcher_icon));

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
                        updateFragment(countdownFragment, true);
                        break;
                    case 2:
                        updateFragment(announcementsFragment, true);
                        break;
                    case 3:
                        updateFragment(scheduleFragment, true);
                        break;
                    case 4:
                        updateFragment(mapViewFragment, true);
                        break;
                    case 6:
                        updateFragment(settingsFragment, true);
                    default:
                        return false;
                }

                mDrawer.closeDrawer();
                return true;
            }
        });

    }

    // Checks if the user can obtain the correct Google Play Services number
    private boolean checkPlayServices() {
        val = true;
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());
                if (resultCode != ConnectionResult.SUCCESS)

                {
                    if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                        //GooglePlayServicesUtil.getErrorDialog(resultCode, this ,GooglePlayServicesUtil.GOOGLE_PLAY_SERVICES_VERSION_CODE).show();
                    } else {
                        Log.e(TAG, "This device is not supported.");
                        finish();
                    }

                    return "false";
                }

                return "true";
            }
            @Override
            protected void onPostExecute(String msg) {
                val = (msg=="true");
            }
        }.execute(null, null, null);
        return val;
    }

    /**
     * Updates the main_fragment_container with the given fragment.
     * @param fragment fragment to replace the main container with
     */
    private void updateFragment(Fragment fragment, boolean addToBackStack) {
        if (fragment == null) return; // only used for pre-release while fragments are not finalized

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.replace(R.id.main_container, fragment);
        if (addToBackStack) fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mShouldSync = savedInstanceState.getBoolean(SHOULD_SYNC, false);
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
        if (v.getId() == R.id.event_close_button) scheduleFragment.scheduleFragmentClick(v);
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
