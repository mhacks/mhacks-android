package com.mhacks.android.ui;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.mhacks.android.data.model.Token;
import com.mhacks.android.data.network.HackathonCallback;
import com.mhacks.android.data.network.NetworkManager;
import com.mhacks.android.ui.account.AccountFragment;
import com.mhacks.android.ui.announcements.AnnouncementsFragment;
import com.mhacks.android.ui.countdown.CountdownFragment;
import com.mhacks.android.ui.events.ScheduleFragment;
import com.mhacks.android.ui.map.MapViewFragment;
import com.mhacks.android.ui.registration.RegistrationFragment;
import com.mhacks.android.ui.settings.SettingsFragment;
import com.mhacks.android.ui.ticket.TicketFragment;

import org.mhacks.android.R;

import java.io.IOException;
import java.util.Set;

/**
 Activity defines primarily the initial network calls to GCM as well as handle Fragment transactions.
 **/

public class MainActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {
    public static final String TAG = "MainActivity";

    // Permissions
    public static final int LOCATION_REQUEST_CODE = 7;
    String regid;
    String PROJECT_NUMBER;
    String notif;
    // Toolbar
    private Toolbar mToolbar;
    private boolean val;
    //Fragments
    private CountdownFragment countdownFragment;
    private AnnouncementsFragment announcementsFragment;
    private ScheduleFragment scheduleFragment;
    private SettingsFragment settingsFragment;
    private MapViewFragment mapViewFragment;
    private RegistrationFragment registrationFragment;
    private TicketFragment ticketFragment;
    private AccountFragment accountFragment;
    //GCM
    private GoogleCloudMessaging gcm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // Add the toolbar
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        // If Activity opened from push notification, value will reflect fragment that will initially open
        notif = getIntent().getStringExtra("notif_link");
        PROJECT_NUMBER = getString(R.string.gcm_server_id);

        //Instantiate fragments
        countdownFragment = new CountdownFragment();
//        announcementsFragment = new AnnouncementsFragment();
//        scheduleFragment = new ScheduleFragment();
//        settingsFragment = new SettingsFragment();
//        mapViewFragment = new MapViewFragment();
//        registrationFragment = new RegistrationFragment();
//        ticketFragment = new TicketFragment();
//        accountFragment = new AccountFragment();
//
//        updateFragment(countdownFragment);
        updateGcm();

        if (notif != null) {
            // Opens Announcements
            if (notif.equals("Announcements")){
                updateFragment(announcementsFragment);
            }
        }
    }

//    public void login() {
//        // Log in if possible.
//        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
//        final String username = sharedPref.getString(SettingsFragment.USERNAME_KEY, "");
//        String password = sharedPref.getString(SettingsFragment.PASSWORD_KEY, "");
//
//        final NetworkManager networkManager = NetworkManager.getInstance();
//        if (username.length() != 0 && password.length() != 0) {
//            networkManager.login(username, password, new HackathonCallback<User>() {
//                @Override
//                public void success(User response) {
//                    userProfile.withName(response.getName());
//                    mDrawer.updateItem(userProfile);
//                }
//
//                @Override
//                public void failure(Throwable error) {
//                }
//            });
//        }
//    }

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


//        // Configure item selection listener
//        mDrawer.setOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
//            @Override
//            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
//                Log.d(TAG, "nav position: " + position);
//                mDrawer.closeDrawer();
//
//                NetworkManager manager = NetworkManager.getInstance();
//                User user =  manager.getCurrentUser();
//
//                // switch 'i' aka position of item
//                // indexing starts at 1 for some reason... probably because of the account header
//                switch (position) {
//                    case 1:
//                        updateFragment(countdownFragment, true);
//                        break;
//                    case 2:
//                        updateFragment(announcementsFragment, true);
//                        break;
//                    case 3:
//                        updateFragment(scheduleFragment, true);
//                        break;
//                    case 4:
//                        requestAndEnableLocation();
//                        break;
//                    case 5:
//                        if (user != null) updateFragment(registrationFragment, true);
//                        else requestLogin();
//                        break;
//                    case 6:
//                        if (user != null) updateFragment(ticketFragment, true);
//                        else requestLogin();
//                        break;
//                    case 8:
//                        updateFragment(accountFragment, true);
//                        break;
//                    case 9:
//                        updateFragment(settingsFragment, true);
//                        break;
//                    default:
//                        return false;
//                }
//
//                return true;
//            }
//        });
//
//    }
//


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
    private void updateFragment(Fragment fragment) {
        if (fragment == null) return; // only used for pre-release while fragments are not finalized

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.replace(R.id.main_container, fragment);
        fragmentTransaction.commit();
    }

//    private void requestAndEnableLocation() {
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
//            PackageManager.PERMISSION_GRANTED) {
//
//            new AlertDialog.Builder(this)
//                    .setTitle("Location Permission")
//                    .setMessage("The MHacks app uses your location to show you where you are on the map and help you find rooms. Would you like to enable location services?")
//                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int which) {
//                            // Request permission
//                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
//                                                                                          Manifest.permission.ACCESS_COARSE_LOCATION},
//                                                              LOCATION_REQUEST_CODE);
//                        }
//                    })
//                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int which) {
//                            updateFragment(mapViewFragment, true);
//                        }
//                    })
//                    .show();
//        } else {
//            updateFragment(mapViewFragment, true);
//        }
//    }


    /**
     * Handles all the clicks for the ScheduleFragment and it's fragments.
     * @param v clicked View
     */
    public void scheduleFragmentClick(View v) {
        if (v.getId() == R.id.event_close_button) scheduleFragment.scheduleFragmentClick(v);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        Log.d(TAG, "ayy");
        switch (requestCode) {
            case LOCATION_REQUEST_CODE:
                if (permissions.length > 0 && (grantResults[0] == PackageManager.PERMISSION_GRANTED || grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
                    Log.d(TAG, "Location permissions granted");
                }
                updateFragment(mapViewFragment);
                break;
        }
    }
}
