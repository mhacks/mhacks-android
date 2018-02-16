package com.mhacks.app.ui.main.view

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.view.MenuItem
import com.mhacks.app.R
import com.mhacks.app.ui.common.BaseActivity
import com.mhacks.app.ui.common.NavigationColor
import com.mhacks.app.ui.main.presenter.MainPresenter
import com.mhacks.app.ui.ticket.view.TicketDialogFragment
import com.mhacks.app.ui.welcome.view.WelcomeFragment
import kotlinx.android.synthetic.main.activity_main.*
import javax.annotation.CheckForNull
import javax.inject.Inject


/**
 * Activity defines primarily the initial hackathonService calls to GCM as well as handle Fragment transactions.
 */
class MainActivity : BaseActivity(), MainView
//        ActivityCompat.OnRequestPermissionsResultCallback,
//        BaseFragment.OnNavigationChangeListener,
//        WelcomeFragment.Callback,
//        TicketDialogFragment.Callback,
//        MapViewFragment.Callback,
//        AnnouncementFragment.Callback,
//        EventsFragment.Callback
{

    @Inject lateinit var mainPresenter: MainPresenter

    override fun onViewLoaded() {
        //
//        if (GooglePlayUtil.checkPlayServices(this)) {
//            val intent = Intent(this, RegistrationIntentService::class.java)
//            startService(intent)
//        }
//        appCallback.hackathonComponent.inject(this)
        setTheme(R.style.MHacksTheme)
//    val hackathonApiService = retrofit.create(MHacksService::class.java)
//    hackathonApiService.getMetaAnnouncements().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe {
//        Timber.e(it.toString())
//    }
        initActivity()
//        checkIfLogin()
    }

    //    private val appCallback by lazy {
//        application as MHacksApplication
//    }
//
//    private var notif: String? = null
//    @Inject lateinit var retrofit: MHacksService
//    @Inject lateinit var mhacksDatabase: MHacksDatabase
    private lateinit var menuItem: MenuItem
//
//    private lateinit var navigationSpinner: Spinner
//
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainPresenter.onViewLoaded()
    }
//
//
//    override fun fetchFloors(success: (floor: List<Floor>) -> Unit,
//                             failure: (error: Throwable) -> Unit) {
//        checkIfNetworkIsPresent(this, {
//            hackathonService.getMetaFloors()
//                    .subscribeOn(Schedulers.newThread())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(
//                            { response -> success(response.floors) },
//                            { error -> failure(error) })
//        })
//
//    }
//
//    override fun checkOrFetchConfig(success: (config: MetaConfiguration) -> Unit,
//                                   failure: (error: Throwable) -> Unit) {
//        checkIfNetworkIsPresent(this,
//            { hackathonService.getMetaConfiguration()
//                .subscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(
//                        { response -> success(response) },
//                        { error -> failure(error) })
//            })
//    }
//
//
//    private fun checkIfLogin() {
//        mhacksDatabase.loginDao().getLogin()
//                .subscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(
//                        { login ->
//                            appCallback.setAuthInterceptorToken(login.token)
//                            initActivity() },
//                        { startLoginActivity() }
//                )
//    }
//
//    override fun fetchAnnouncements(success: (announcements: List<Announcements>) -> Unit,
//                                    failure: (error: Throwable) -> Unit) {
//        checkIfNetworkIsPresent(this, {
//            Observable.timer(3000, TimeUnit.MILLISECONDS)
//                    .startWith(0)
//                    .flatMap {
//                        hackathonService.getMetaAnnouncements()
//                    }
//                    .subscribeOn(Schedulers.newThread())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(
//                            { (announcements) -> success(announcements) },
//                            { error -> failure(error) })
//        })
//    }
//

    private fun showTicketDialogFragment() {
        val ft = supportFragmentManager.beginTransaction()
        val prev = supportFragmentManager.findFragmentByTag("dialog")
        if (prev != null) {
            ft.remove(prev)
        }
        ft.addToBackStack(null)
        val ticket: TicketDialogFragment = TicketDialogFragment.newInstance()

        ticket.show(ft, "dialog")
    }

    private fun updateFragment(fragment: Fragment?) {
        if (fragment == null) return  // only used for pre-release while fragments are not finalized

        supportFragmentManager.beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .replace(R.id.fragment_container, fragment)
                .commit()
    }
//
//    override fun startLoginActivity() {
//        startActivity(Intent(this, LoginActivity::class.java))
//        finish()
//    }
//
////    private fun startQRScanActivity() {
////        startActivity(Intent(this, QRScanActivity::class.java))
////        finish()
////    }
//
//    override fun checkOrFetchUser(success: (user: User) -> Unit,
//                                  failure: (error: Throwable) -> Unit) {
//        mhacksDatabase.userDao().getUser()
//                .onErrorResumeNext ({
//                    mhacksDatabase.loginDao().getLogin()
//                            .flatMap({ login ->
//                                appCallback.setAuthInterceptorToken(login.token)
//                                hackathonService.getMetaUser()
//                                        .flatMap { user -> Single.just(user.user) }
//                            })
//                })
//                .subscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(
//                        { response -> success(response) },
//                        { error -> failure(error) })
//    }
//
//    override fun fetchEvents(success: (events: List<Event>) -> Unit,
//                             failure: (error: Throwable) -> Unit) {
//        checkIfNetworkIsPresent(this, {
//            hackathonService.getMetaEvent()
//                    .subscribeOn(Schedulers.newThread())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(
//                            { response -> success(response.events!!) },
//                            { error -> failure(error) })
//        })
//    }
//
//    override fun updateFloors(floors: List<Floor>, listener: OnItemSelectedListener) {
//
//        val adapter = ArrayAdapter<Floor>(this, R.layout.floors_spinner_item, floors)
//        adapter.setDropDownViewResource(R.layout.map_view_dropdown_item_1line)
//        navigationSpinner.adapter = adapter
//        navigationSpinner.onItemSelectedListener = listener
//    }
//
//    override fun showFloorOptions() {
//
//    }
//
//    override fun hideFloorOptions() {
//        navigationSpinner.visibility = View.GONE
//    }
//
//    override fun onRequestPermissionsResult(requestCode: Int,
//                                            permissions: Array<String>,
//                                            grantResults: IntArray) {
//        when (requestCode) {
//            LOCATION_REQUEST_CODE ->
//                if (permissions.isNotEmpty() &&
//                        (grantResults[0] == PackageManager.PERMISSION_GRANTED ||
//                         grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
//            }
//        }
//    }
//
    private fun initActivity() {

        setSystemFullScreenUI()
        setContentView(R.layout.activity_main)
        setBottomNavigationColor(
                NavigationColor(R.color.colorPrimary, R.color.colorPrimaryDark))
        qr_ticket_fab.setOnClickListener({
            showTicketDialogFragment()
        })
        menuItem = navigation.menu.getItem(0)
        menuItem.setTitle(R.string.title_home)
        setSupportActionBar(toolbar)
//        addMapFloorSpinner()
//        toolbar.addView(navigationSpinner, Toolbar.LayoutParams(Gravity.END))
        updateFragment(WelcomeFragment.instance)
//        notif = intent.getStringExtra("notif_link")
        navigation?.setOnNavigationItemSelectedListener({ item ->
            when (item.itemId) {
                R.id.navigation_home -> updateFragment(WelcomeFragment.instance)
//                R.id.navigation_announcements -> updateFragment(AnnouncementFragment.instance)
//                R.id.navigation_events -> updateFragment(EventsFragment.instance)
//                R.id.navigation_map -> updateFragment(MapViewFragment.instance)
            }
            menuItem = item
            true
        })
    }
//
//    private fun addMapFloorSpinner() {
//        navigationSpinner = Spinner(supportActionBar?.themedContext)
//        navigationSpinner.visibility = View.GONE
//        navigationSpinner.background.setColorFilter(
//                ContextCompat.getColor(this, R.color.white),
//                PorterDuff.Mode.SRC_ATOP)
//    }
//
//    interface OnFromMainActivityCallback {
//        fun setAuthInterceptorToken(token: String)
//        val hackathonComponent: AppComponent
//    }

    companion object {
        val LOCATION_REQUEST_CODE = 7
    }
}
