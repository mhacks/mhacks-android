package com.mhacks.android.ui

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.FragmentTransaction
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.annotation.ColorRes
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.WindowManager
import com.google.android.gms.gcm.GoogleCloudMessaging
import com.mhacks.android.MHacksApplication
import com.mhacks.android.data.kotlin.User
import com.mhacks.android.data.model.Login
import com.mhacks.android.data.network.services.HackathonApiService
import com.mhacks.android.data.room.MHacksDatabase
import com.mhacks.android.ui.announcements.AnnouncementFragment
import com.mhacks.android.ui.common.BaseFragment
import com.mhacks.android.ui.common.NavigationColor
import com.mhacks.android.ui.countdown.WelcomeFragment
import com.mhacks.android.ui.info.InfoFragment
import com.mhacks.android.ui.login.LoginActivity
import com.mhacks.android.ui.map.MapViewFragment
import com.mhacks.android.ui.schedule.EventFragment
import com.mhacks.android.ui.ticket.TicketDialogFragment
import com.mhacks.android.util.ResourceUtil
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.toSingle
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import org.mhacks.android.R
import timber.log.Timber
import java.util.*
import javax.inject.Inject

/**
 * Activity defines primarily the initial hackathonService calls to GCM as well as handle Fragment transactions.
 */
class MainActivity : AppCompatActivity(),
        ActivityCompat.OnRequestPermissionsResultCallback,
        BaseFragment.OnNavigationChangeListener,
        TicketDialogFragment.OnFromTicketDialogFragmentCallback{

    private val gcm: GoogleCloudMessaging by lazy {
        GoogleCloudMessaging.getInstance(applicationContext)
    }

    var notif: String? = null

    lateinit var regid: String
    val PROJECT_NUMBER: String by lazy { getString(R.string.gcm_server_id) }
    private var canUsePlayServices: Boolean = false

    @Inject lateinit var hackathonService: HackathonApiService
    @Inject lateinit var database: MHacksDatabase
    private lateinit var menuItem: MenuItem

    override fun onResume() {
        super.onResume()
        checkLogin()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as MHacksApplication).hackathonComponent.inject(this)
        setTheme(R.style.MHacksTheme)


    }

    private fun checkOrFetchConfig() {
        hackathonService.getConfiguration()
                .doOnNext { config -> database.configDao().insertConfig(config.configuration) }
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { response -> Timber.d(response.configuration.startDate) },
                        { error -> Timber.d(error.message) })
    }


    private fun checkLogin() {
        database.loginDao().getLogin()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { initActivity() },
                        {
                            startActivity(Intent(this, LoginActivity::class.java))
                            finish()
                        }
                )
    }
    fun FetchUser(token: String): Observable<User>  {
            return hackathonService.getUser(token)
        }

    override fun checkOrFetchUser(success: (user: User) -> Unit,
                                  failure: (error: Throwable) -> Unit) {
        database.userDao()
                .getUser()
                .onErrorResumeNext ({
                    database.loginDao().getLogin()
                            .flatMap({ login ->
                                FetchUser(login.token).singleOrError()
                    })
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { response -> Timber.d(response.email) },
                        { error -> Timber.d(error.message) })
    }





    @TargetApi(21)
    override fun setStatusBarColor(color: Int) {
        window.statusBarColor = ContextCompat.getColor(this, color)
    }

    @SuppressLint("InlinedApi")
    fun setSystemFullScreenUI() {
        window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    }

    @SuppressLint("InlinedApi")
    override fun setTransparentStatusBar() {
        window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    }

    @TargetApi(21)
    override fun clearTransparentStatusBar() {
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    }

    override fun setActionBarColor(@ColorRes color: Int) {
        supportActionBar?.setBackgroundDrawable(ContextCompat.getDrawable(this, color))
    }

    override fun setFragmentTitle(title: Int) {
        setTitle(title)
    }

    private fun setBottomNavigationColor(color: NavigationColor) {
        val colorStateList = NavigationColor.getColorStateList(
                ContextCompat.getColor(this, color.primaryColor),
                ContextCompat.getColor(this, color.secondaryColor)
        )

        navigation?.itemIconTintList = colorStateList
        navigation?.itemTextColor = colorStateList
    }

    /**
     * Updates the main_fragment_container with the given fragment.
     * @param fragment fragment to replace the main container with
     */

    private fun updateFragment(fragment: Fragment?) {
        if (fragment == null) return  // only used for pre-release while fragments are not finalized

        supportFragmentManager.beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .replace(R.id.fragment_container, fragment)
                .commit()
    }

    override fun addPadding() {
        val height: Int = ResourceUtil.convertDpResToPixel(context = this,
                res = R.dimen.toolbar_height)
        fragment_container.setPadding(0, height, 0, 0)
    }

    override fun removePadding() {
        fragment_container.setPadding(0, 0, 0, 0)
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>,
                                            grantResults: IntArray) {
        when (requestCode) {
            LOCATION_REQUEST_CODE -> if (permissions.isNotEmpty() && (grantResults[0] == PackageManager.PERMISSION_GRANTED || grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
            }
        }
        // updateFragment(mapViewFragment);
    }


    private fun initActivity() {

        setSystemFullScreenUI()
        setContentView(R.layout.activity_main)
        checkOrFetchConfig()
        setBottomNavigationColor(
                NavigationColor(R.color.colorPrimary, R.color.colorPrimaryDark))

        // Add the toolbar
        qr_ticket_fab.setOnClickListener({
            val ft = supportFragmentManager.beginTransaction()
            val prev = supportFragmentManager.findFragmentByTag("dialog")
            if (prev != null) {
                ft.remove(prev)
            }
            ft.addToBackStack(null)

            val ticket: TicketDialogFragment = TicketDialogFragment
                    .newInstance("Jeffrey Chang")
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
                R.id.navigation_home -> {
                    updateFragment(WelcomeFragment.instance)
                }
                R.id.navigation_announcements -> {
                    updateFragment(AnnouncementFragment.instance)
                }
                R.id.navigation_events -> {
                    updateFragment(EventFragment.instance)
                }
                R.id.navigation_map -> {
                    updateFragment(MapViewFragment.instance)
                }
                R.id.navigation_info -> {
                    updateFragment(InfoFragment.instance)
                }
            }
            menuItem = item
            true
        })
    }

    companion object {

        // Permissions
        val LOCATION_REQUEST_CODE = 7
    }
}
