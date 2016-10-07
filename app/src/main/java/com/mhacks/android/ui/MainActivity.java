package com.mhacks.android.ui;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

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
import com.mhacks.android.ui.registration.RegistrationFragment;
import com.mhacks.android.ui.settings.SettingsFragment;
import com.mhacks.android.ui.ticket.TicketFragment;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

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

    private boolean val;

    // Navigation Drawer
    private Drawer mDrawer;
    private ProfileDrawerItem userProfile;

    private boolean mShouldSync = true;

    //Fragments
    private CountdownFragment countdownFragment;
    private AnnouncementsFragment announcementsFragment;
    private ScheduleFragment scheduleFragment;
    private SettingsFragment settingsFragment;
    private MapViewFragment mapViewFragment;
    private RegistrationFragment registrationFragment;
    private TicketFragment ticketFragment;

    //GCM
    private GoogleCloudMessaging gcm;
    String regid;
    String PROJECT_NUMBER;
    String notif;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        View mainView = findViewById(R.id.main_container);

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
        registrationFragment = new RegistrationFragment();
        ticketFragment = new TicketFragment();

        buildNavigationDrawer();
        updateFragment(countdownFragment, false);
        login();
        updateGcm();

        if (notif != null) {
            // Opens Announcements
            if (notif.equals("Announcements")){
                updateFragment(announcementsFragment, true);
            }
        }
    }

    public void login() {
        // Log in if possible.
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        final String username = sharedPref.getString(SettingsFragment.USERNAME_KEY, "");
        String password = sharedPref.getString(SettingsFragment.PASSWORD_KEY, "");

        final NetworkManager networkManager = NetworkManager.getInstance();
        if (username.length() != 0 && password.length() != 0) {
            networkManager.login(username, password, new HackathonCallback<User>() {
                @Override
                public void success(User response) {
                    userProfile.withName(response.getName());
                    mDrawer.updateItem(userProfile);
                }

                @Override
                public void failure(Throwable error) {
                }
            });
        }
    }

    public void updateGcm(){
        if (!checkPlayServices()) {
            Log.e(TAG, "No valid Google Play Services APK found.");
            return;
        }
        // Grabs the Google Cloud Messaging REG ID and sends it to the backend

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

                    final Token token = new Token(regid);
                    token.setName(String.valueOf(pref));
                    // active=true by default

                    final NetworkManager networkManager = NetworkManager.getInstance();
                    networkManager.sendToken(token, new HackathonCallback<Token>() {
                        @Override
                        public void success(Token response) {
                            Log.d(TAG, "gcm sent successfully: " + token.getRegistrationId());
                            sharedPref.edit().putString("gcm", regid).apply();
                        }

                        @Override
                        public void failure(Throwable error) {
                            Log.e(TAG, "gcm didnt work", error);
                        }
                    });

                    msg = "Device registered, reg id =" + regid;
                    Log.i("GCM",  msg);

                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                    Log.e(TAG, "IOException when registering the device", ex);

                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {

            }
        }.execute(null, null, null);
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
                                                       .withIcon(R.drawable.ic_map)
                                                       .withSelectedTextColorRes(R.color.primary);
        PrimaryDrawerItem register = new PrimaryDrawerItem().withName("Scan")
                                                            .withIcon(R.drawable.ic_linked_camera)
                                                            .withSelectedTextColorRes(R.color.primary);
        PrimaryDrawerItem ticket = new PrimaryDrawerItem().withName("Ticket")
                                                          .withIcon(R.drawable.ic_action_ticket)
                                                          .withSelectedTextColorRes(R.color.primary);
        final SecondaryDrawerItem settings = new SecondaryDrawerItem().withName("Settings")
                                                                      .withIcon(R.drawable.ic_settings)
                                                                      .withSelectedTextColorRes(R.color.primary);

        // User profile
        User mUser = NetworkManager.getInstance().getCurrentUser();
        String userName = (mUser != null) ? mUser.getName() : "MHacks 8";
        userProfile = new ProfileDrawerItem().withName(userName)
                                             .withTextColorRes(R.color.black)
                                             .withSelectedColorRes(R.color.primary)
                                             .withSelectedTextColorRes(R.color.accent);

        // Account Header
        AccountHeader accountHeader = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.poly6)
                .addProfiles(userProfile)
                .withTextColorRes(R.color.black)
                .build();

        // Build drawer
        mDrawer = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(mToolbar)
                .withAccountHeader(accountHeader)
                .addDrawerItems(countdown, announcements, events, map, register, ticket,
                                new DividerDrawerItem(),
                                settings)
                .build();

        // Configure item selection listener
        final Context context = this;
        mDrawer.setOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
            @Override
            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                Log.d(TAG, "nav position: " + position);

                NetworkManager manager = NetworkManager.getInstance();
                User user =  manager.getCurrentUser();

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
                    case 5:
                        if (user != null) updateFragment(registrationFragment, true);
                        else requestLogin();
                        break;
                    case 6:
                        if (user != null) updateFragment(ticketFragment, true);
                        else requestLogin();
                        break;
                    case 8:
                        updateFragment(settingsFragment, true);
                    default:
                        return false;
                }

                mDrawer.closeDrawer();
                return true;
            }
        });

    }

    private void requestLogin() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Login Required")
                        .setMessage("Would you like to login?")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                updateFragment(settingsFragment, true);
                            }
                        })
                        .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .setIcon(R.drawable.ic_dialog_alert)
                        .show();

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

}
