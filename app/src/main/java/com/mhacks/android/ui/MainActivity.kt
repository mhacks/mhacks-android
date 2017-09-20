package com.mhacks.android.ui

import android.app.FragmentTransaction
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.view.MenuItem
import com.google.android.gms.gcm.GoogleCloudMessaging
import com.mhacks.android.MHacksApplication
import com.mhacks.android.dagger.component.HackathonComponent
import com.mhacks.android.data.kotlin.Config
import com.mhacks.android.data.kotlin.User
import com.mhacks.android.data.network.services.HackathonApiService
import com.mhacks.android.data.room.MHacksDatabase
import com.mhacks.android.ui.events.EventsFragment
import com.mhacks.android.ui.common.BaseActivity
import com.mhacks.android.ui.common.BaseFragment
import com.mhacks.android.ui.common.NavigationColor
import com.mhacks.android.ui.countdown.WelcomeFragment
import com.mhacks.android.ui.announcement.AnnouncementFragment
import com.mhacks.android.ui.login.LoginActivity
import com.mhacks.android.ui.map.MapViewFragment
import com.mhacks.android.ui.qrscan.QRScanActivity
import com.mhacks.android.ui.ticket.TicketDialogFragment
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import org.mhacks.android.R
import javax.inject.Inject
import android.R.id.edit
import com.mhacks.android.data.network.HackathonCallback
import com.mhacks.android.ui.settings.SettingsFragment
import android.preference.PreferenceManager
import android.content.SharedPreferences
import android.os.AsyncTask
import com.mhacks.android.data.kotlin.Token
import timber.log.Timber
import java.io.IOException


/**
 * Activity defines primarily the initial hackathonService calls to GCM as well as handle Fragment transactions.
 */
class MainActivity : BaseActivity(),
        ActivityCompat.OnRequestPermissionsResultCallback,
        BaseFragment.OnNavigationChangeListener,
        TicketDialogFragment.OnFromTicketDialogFragmentCallback{

    private lateinit var gcm: GoogleCloudMessaging

    // Callbacks to properties and methods on the application class.
    private val appCallback by lazy {
        application as MHacksApplication
    }

    var notif: String? = null

    lateinit var regid: String
    val PROJECT_NUMBER: String by lazy { getString(R.string.gcm_server_id) }

    @Inject lateinit var hackathonService: HackathonApiService
    @Inject lateinit var mhacksDatabase: MHacksDatabase
    private lateinit var menuItem: MenuItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        if (GooglePlayUtil.checkPlayServices(this)) {
//            val intent = Intent(this, RegistrationIntentService::class.java)
//            startService(intent)
//        }
        appCallback.hackathonComponent.inject(this)
        setTheme(R.style.MHacksTheme)
        checkIfLogin()
    }

    fun updateGcm() {
        // Grabs the Google Cloud Messaging REG ID and sends it to the backend

        gcmAsyncTask().execute(null, null, null)
    }


    private fun checkOrFetchConfig(success: (config: Config) -> Unit,
                                   failure: (error: Throwable) -> Unit) {
        hackathonService.getConfiguration()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { response -> success(response) },
                        { error -> failure(error) })
    }


    private fun checkIfLogin() {
        mhacksDatabase.loginDao().getLogin()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { login ->
                            appCallback.setAuthInterceptorToken(login.token)
                            initActivity() },
                        { startLoginActivity() }
                )
    }

    private fun showTicketDialogFragment() {
        val ft = supportFragmentManager.beginTransaction()
        val prev = supportFragmentManager.findFragmentByTag("dialog")
        if (prev != null) {
            ft.remove(prev)
        }
        ft.addToBackStack(null)

        val ticket: TicketDialogFragment = TicketDialogFragment
                .newInstance()
        ticket.show(ft, "dialog")
    }

    private fun updateFragment(fragment: Fragment?) {
        if (fragment == null) return  // only used for pre-release while fragments are not finalized

        supportFragmentManager.beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .replace(R.id.fragment_container, fragment)
                .commit()
    }

    override fun startLoginActivity() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    private fun startQRScanActivity() {
        startActivity(Intent(this, QRScanActivity::class.java))
        finish()
    }

    override fun checkOrFetchUser(success: (user: User) -> Unit,
                                  failure: (error: Throwable) -> Unit) {
        mhacksDatabase.userDao().getUser()
                .onErrorResumeNext ({
                    mhacksDatabase.loginDao().getLogin()
                            .flatMap({ login ->
                                if (login.userSkipped) {
                                    startLoginActivity()
                                    finish()
                                }
                                appCallback.setAuthInterceptorToken(login.token)
                                hackathonService.getUser()
                                        .flatMap { user -> Single.just(user.user) }
                    })
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { response -> success(response) },
                        { error -> failure(error) })
    }

    fun fetchGcmToken() {
//        hackathonService.getGcmToken()
//        .subscribeOn(Schedulers.newThread())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe(
//                    { response ->  },
//                    { error ->  })
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>,
                                            grantResults: IntArray) {
        when (requestCode) {
            LOCATION_REQUEST_CODE ->
                if (permissions.isNotEmpty() &&
                        (grantResults[0] == PackageManager.PERMISSION_GRANTED ||
                         grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
            }
        }
    }


    private fun initActivity() {

        setSystemFullScreenUI()
        setContentView(R.layout.activity_main)
        setBottomNavigationColor(
                NavigationColor(R.color.colorPrimary, R.color.colorPrimaryDark))

        qr_ticket_fab.setOnClickListener({
            val ft = supportFragmentManager.beginTransaction()
            val prev = supportFragmentManager.findFragmentByTag("dialog")
            if (prev != null) {
                ft.remove(prev)
            }
            ft.addToBackStack(null)

            val ticket: TicketDialogFragment = TicketDialogFragment
                    .newInstance()
            ticket.show(ft, "dialog")
        })

        menuItem = navigation.menu.getItem(0)
        menuItem.setTitle(R.string.title_home)
        setSupportActionBar(toolbar)
        updateFragment(WelcomeFragment.instance)


        // If Activity opened from push notification, value will reflect fragment that will initially open
        notif = intent.getStringExtra("notif_link")


        navigation?.setOnNavigationItemSelectedListener({ item ->
            when (item.itemId) {
                R.id.navigation_home -> updateFragment(WelcomeFragment.instance)
                R.id.navigation_announcements -> updateFragment(AnnouncementFragment.instance)
                R.id.navigation_events -> updateFragment(EventsFragment.instance)
                R.id.navigation_map -> updateFragment(MapViewFragment.instance)
            }
            menuItem = item
            true
        })
    }




    inner class gcmAsyncTask: AsyncTask<Void, Void, String>() {
        override fun doInBackground(vararg params: Void): String {
            var msg = ""
            try {
                val data = Bundle()

                regid = gcm.register(PROJECT_NUMBER)

                data.putString("regid", regid)
                /*InstanceID instanceID = InstanceID.getInstance(getApplicationContext());
                String token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
                        GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);*/

                val sharedPref = PreferenceManager.getDefaultSharedPreferences(applicationContext)
                val gcmPush = sharedPref.getString("gcm", "")
                val channels = sharedPref.getStringSet(SettingsFragment.PUSH_NOTIFICATION_CHANNELS, null)

                var pref = 1
                if (channels != null) {
                    val channelPrefs = channels.toTypedArray()
                    for (i in channelPrefs.indices) {
                        pref += Integer.parseInt(channelPrefs[i])
                    }
                } else
                    pref = 63

                val token = Token(regid)
                token.name = pref.toString()
                // active=true by default

//                val networkManager = NetworkManager.getInstance()
//                networkManager.sendToken(token, object : HackathonCallback<Token> {
//                    override fun success(response: Token) {
//                        Log.d(FragmentActivity.TAG, "gcm sent successfully: " + token.getRegistrationId())
//                        sharedPref.edit().putString("gcm", regid).apply()
//                    }
//
//                    override fun failure(error: Throwable) {
//                        Log.e(FragmentActivity.TAG, "gcm didnt work", error)
//                    }
//                })
//
//                msg = "Device registered, reg id =" + regid
//                Log.i("GCM", msg)
//
            } catch (e: IOException) {
                msg = "Error :" + e.message
                Timber.e("IOException when registering the device")

            }

            return msg
        }

        override fun onPostExecute(msg: String) {

        }

    }

    interface OnFromMainActivityCallback {

        fun setAuthInterceptorToken(token: String)

        val hackathonComponent: HackathonComponent
    }

    companion object {

        val LOCATION_REQUEST_CODE = 7
    }
}

