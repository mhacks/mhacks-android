//package com.mhacks.android.ui;
//
//import android.annotation.TargetApi;
//import android.app.FragmentTransaction;
//import android.content.SharedPreferences;
//import android.content.pm.PackageManager;
//import android.content.res.ColorStateList;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.preference.PreferenceManager;
//import android.support.annotation.NonNull;
//import android.support.design.widget.BottomNavigationView;
//import android.support.v4.app.ActivityCompat;
//import android.support.v4.app.Fragment;
//import android.support.v4.content.ContextCompat;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.Toolbar;
//import android.util.Log;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.WindowManager;
//
//import com.google.android.gms.common.ConnectionResult;
//import com.google.android.gms.common.GooglePlayServicesUtil;
//import com.google.android.gms.gcm.GoogleCloudMessaging;
//import com.mhacks.android.data.model.Token;
//import com.mhacks.android.data.network.HackathonCallback;
//import com.mhacks.android.data.network.NetworkManager;
//import com.mhacks.android.ui.common.BaseFragment;
//import com.mhacks.android.ui.common.NavigationColor;
//import com.mhacks.android.ui.countdown.WelcomeFragment;
//import com.mhacks.android.ui.events.EventFragment;
//import com.mhacks.android.ui.info.InfoFragment;
//import com.mhacks.android.ui.map.MapViewFragmentJava;
//import com.mhacks.android.ui.settings.SettingsFragment;
//import com.mhacks.android.ui.kotlin.announcements.AnnouncementFragment;
//import org.mhacks.android.R;
//
//import java.io.IOException;
//import java.util.Set;
//
///**
// Activity defines primarily the initial network calls to GCM as well as handle Fragment transactions.
// **/
//
//public class SecondaryActivity extends AppCompatActivity implements
//        BottomNavigationView.OnNavigationItemSelectedListener,
//        ActivityCompat.OnRequestPermissionsResultCallback,
//        BaseFragment.OnNavigationChangeListener {
//
//    public static final String TAG = "org.mhacks/SecondaryActivity";
//
//    // Permissions
//    public static final int LOCATION_REQUEST_CODE = 7;
//    String regid;
//    String PROJECT_NUMBER;
//    String notif;
//
//    // Toolbar
//    private Toolbar mToolbar;
//    private boolean val;
//    private BottomNavigationView mNavigation;
//    private MenuItem mMenuItem;
//
//    //GCM
//    private GoogleCloudMessaging gcm;
//
//    @TargetApi(21)
//    @Override
//    public void setStatusBarColor(int color) {
//        getWindow().setStatusBarColor(ContextCompat.getColor(this, color));
//    }
//
//    @Override
//    public void setLayoutFullScreen() {
//
//        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
//    }
//
//    @Override
//    public void setTransparentStatusBar() {
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//    }
//
//    @TargetApi(21)
//    @Override
//    public void clearTransparentStatusBar() {
//        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//    }
//
//    @Override
//    public void setActionBarColor(int drawable) {
//        if (getSupportActionBar() != null) {
//            getSupportActionBar().setBackgroundDrawable(ContextCompat.getDrawable(this, drawable));
//        }
//    }
//
//    @Override
//    public void setFragmentTitle(int title) {
//        setTitle(title);
//    }
//
//    @Override
//    public void setBottomNavigationColor(NavigationColor color) {
//        ColorStateList colorStateList = NavigationColor.Companion.getColorStateList(
//                ContextCompat.getColor(this, color.getPrimaryColor()),
//                ContextCompat.getColor(this, color.getSecondaryColor())
//        );
//
//        mNavigation.setItemIconTintList(colorStateList);
//        mNavigation.setItemTextColor(colorStateList);
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setTheme(R.style.MHacksTheme);
//        setContentView(R.layout.activity_main);
//        // Add the toolbar
//        mToolbar = (Toolbar) findViewById(R.id.toolbar);
//        mNavigation = (BottomNavigationView) findViewById(R.id.navigation);
//        mMenuItem = mNavigation.getMenu().getItem(0);
//        mMenuItem.setTitle(R.string.title_home);
//        setSupportActionBar(mToolbar);
//        updateFragment(WelcomeFragment.Companion.getInstance());
//
//
//        // If Activity opened from push notification, value will reflect fragment that will initially open
//        notif = getIntent().getStringExtra("notif_link");
//        PROJECT_NUMBER = getString(R.string.gcm_server_id);
//
//        updateGcm();
//        /*if (true) {
//            Intent intent = new Intent(this, LoginActivity.class);
//            startActivity(intent);
//            finish();
//        }*/
//        mNavigation.setOnNavigationItemSelectedListener(this);
//
//        if (notif != null) {
//            // Opens Announcements
//            if (notif.equals("Announcements")){
//                // updateFragment(announcementsFragment);
//            }
//        }
//    }
//
////    public void login() {
////        // Log in if possible.
////        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
////        final String username = sharedPref.getString(SettingsFragment.USERNAME_KEY, "");
////        String password = sharedPref.getString(SettingsFragment.PASSWORD_KEY, "");
////
////        final NetworkManager networkManager = NetworkManager.getInstance();
////        if (username.length() != 0 && password.length() != 0) {
////            networkManager.login(username, password, new HackathonCallback<User>() {
////                @Override
////                public void success(User response) {
////                    userProfile.withName(response.getName());
////                    mDrawer.updateItem(userProfile);
////                }
////
////                @Override
////                public void failure(Throwable error) {
////                }
////            });
////        }
////    }
//
//    public void updateGcm(){
//        if (!checkPlayServices()) {
//            Log.e(TAG, "No valid Google Play Services APK found.");
//            return;
//        }
//        // Grabs the Google Cloud Messaging REG ID and sends it to the backend
//
//        new AsyncTask<Void, Void, String>() {
//            @Override
//            protected String doInBackground(Void... params) {
//                String msg = "";
//                try {
//                    if (gcm == null) {
//                        gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
//                    }
//                    // reg_id
//                    Bundle data = new Bundle();
//
//                    regid = gcm.register(PROJECT_NUMBER);
//
//                    data.putString("regid",regid);
//                    /*InstanceID instanceID = InstanceID.getInstance(getApplicationContext());
//                    String token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
//                            GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);*/
//
//                    final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//                    String gcmPush = sharedPref.getString("gcm", "");
//                    Set<String> channels =  sharedPref.getStringSet(SettingsFragment.PUSH_NOTIFICATION_CHANNELS, null);
//
//                    int pref = 1;
//                    if (channels != null) {
//                        String[] channelPrefs = channels.toArray(new String[channels.size()]);
//                        for (int i = 0; i < channelPrefs.length; ++i) {
//                            pref += Integer.parseInt(channelPrefs[i]);
//                        }
//                    } else pref = 63;
//
//                    final Token token = new Token(regid);
//                    token.setName(String.valueOf(pref));
//                    // active=true by default
//
//                    final NetworkManager networkManager = NetworkManager.getInstance();
//                    networkManager.sendToken(token, new HackathonCallback<Token>() {
//                        @Override
//                        public void success(Token response) {
//                            Log.d(TAG, "gcm sent successfully: " + token.getRegistrationId());
//                            sharedPref.edit().putString("gcm", regid).apply();
//                        }
//
//                        @Override
//                        public void failure(Throwable error) {
//                            Log.e(TAG, "gcm didnt work", error);
//                        }
//                    });
//
//                    msg = "Device registered, reg id =" + regid;
//                    Log.i("GCM",  msg);
//
//                } catch (IOException ex) {
//                    msg = "Error :" + ex.getMessage();
//                    Log.e(TAG, "IOException when registering the device", ex);
//
//                }
//                return msg;
//            }
//
//            @Override
//            protected void onPostExecute(String msg) {
//
//            }
//        }.execute(null, null, null);
//    }
//
//
//
//    // Checks if the user can obtain the correct Google Play Services number
//    private boolean checkPlayServices() {
//        val = true;
//        new AsyncTask<Void, Void, String>() {
//            @Override
//            protected String doInBackground(Void... params) {
//                String msg = "";
//                int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());
//                if (resultCode != ConnectionResult.SUCCESS)
//
//                {
//                    if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
//                        //GooglePlayServicesUtil.getErrorDialog(resultCode, this ,GooglePlayServicesUtil.GOOGLE_PLAY_SERVICES_VERSION_CODE).show();
//                    } else {
//                        Log.e(TAG, "This device is not supported.");
//                        finish();
//                    }
//
//                    return "false";
//                }
//
//                return "true";
//            }
//            @Override
//            protected void onPostExecute(String msg) {
//                val = (msg=="true");
//            }
//        }.execute(null, null, null);
//        return val;
//    }
//
//    /**
//     * Updates the main_fragment_container with the given fragment.
//     * @param fragment fragment to replace the main container with
//     */
//    private void updateFragment(Fragment fragment) {
//        if (fragment == null) return; // only used for pre-release while fragments are not finalized
//
//        getSupportFragmentManager().
//                beginTransaction()
//                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
//                .replace(R.id.main_container, fragment)
//                .commit();
//    }
//
////    private void requestAndEnableLocation() {
////        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
////            PackageManager.PERMISSION_GRANTED) {
////
////            new AlertDialog.Builder(this)
////                    .setTitle("Location Permission")
////                    .setMessage("The MHacks app uses your location to show you where you are on the map and help you find rooms. Would you like to enable location services?")
////                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
////                        public void onClick(DialogInterface dialog, int which) {
////                            // Request permission
////                            ActivityCompat.requestPermissions(SecondaryActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
////                                                                                          Manifest.permission.ACCESS_COARSE_LOCATION},
////                                                              LOCATION_REQUEST_CODE);
////                        }
////                    })
////                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
////                        public void onClick(DialogInterface dialog, int which) {
////                            updateFragment(mapViewFragment, true);
////                        }
////                    })
////                    .show();
////        } else {
////            updateFragment(mapViewFragment, true);
////        }
////    }
//
//
//    /**
//     * Handles all the clicks for the EventFragment and it's fragments.
//     * @param v clicked View
//     */
//    public void scheduleFragmentClick(View v) {
////        if (v.getId() == R.id.event_close_button) scheduleFragment.scheduleFragmentClick(v);
//    }
//
//    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//
//        /*
//            Necessary to set the global variable to the current MenuItem. There is a layout
//            issue where the menu images are clipped if the title is set to another item.
//        */
//
//        mMenuItem.setTitle("");
//        switch (item.getItemId()) {
//            case R.id.navigation_home:
//                item.setTitle(R.string.title_home);
//                updateFragment(WelcomeFragment.Companion.getInstance());
//                break;
//            case R.id.navigation_announcements:
//                item.setTitle(R.string.title_announcements);
//                updateFragment(AnnouncementFragment.Companion.getInstance());
//
//                break;
//            case R.id.navigation_events:
//                item.setTitle(R.string.title_events);
//                updateFragment(EventFragment.getInstance());
//                break;
//            case R.id.navigation_map:
//                item.setTitle(R.string.title_map);
//                updateFragment(MapViewFragmentJava.getInstance());
//                break;
//            case R.id.navigation_info:
//                item.setTitle(R.string.title_info);
//                updateFragment(InfoFragment.Companion.getInstance());
//                break;
//        }
//        mMenuItem = item;
//        return true;
//    }
//
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode,
//                                           @NonNull String[] permissions,
//                                           @NonNull int[] grantResults) {
//        switch (requestCode) {
//            case LOCATION_REQUEST_CODE:
//                if (permissions.length > 0 && (grantResults[0] == PackageManager.PERMISSION_GRANTED || grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
//                    Log.d(TAG, "Location permissions granted");
//                }
//                // updateFragment(mapViewFragment);
//                break;
//        }
//    }
//}
