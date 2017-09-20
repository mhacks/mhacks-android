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
import com.mhacks.android.data.network.fcm.RegistrationIntentService
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

import com.mhacks.android.util.GooglePlayUtil
import timber.log.Timber


/**
 * Activity defines primarily the initial hackathonService calls to GCM as well as handle Fragment transactions.
 */
class MainActivity : BaseActivity(),
        ActivityCompat.OnRequestPermissionsResultCallback,
        BaseFragment.OnNavigationChangeListener,
        TicketDialogFragment.OnFromTicketDialogFragmentCallback{

    private val gcm: GoogleCloudMessaging by lazy {
        GoogleCloudMessaging.getInstance(applicationContext)
    }

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
        if (GooglePlayUtil.checkPlayServices(this)) {
            Timber.d("Hello world")
            val intent = Intent(this, RegistrationIntentService::class.java)
            startService(intent)
        }
        appCallback.hackathonComponent.inject(this)
        setTheme(R.style.MHacksTheme)
        checkIfLogin()
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





    interface OnFromMainActivityCallback {

        fun setAuthInterceptorToken(token: String)

        val hackathonComponent: HackathonComponent
    }

    companion object {

        val LOCATION_REQUEST_CODE = 7
    }
}
