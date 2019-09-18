package org.mhacks.app.ui.main

import android.app.AlertDialog
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.IdRes
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.mhacks.app.BuildConfig
import org.mhacks.app.R
import org.mhacks.app.core.Activities
import org.mhacks.app.core.AddressableFragment
import org.mhacks.app.core.Fragments
import org.mhacks.app.core.callback.TicketDialogCallback
import org.mhacks.app.core.intentTo
import org.mhacks.app.core.ktx.showSnackBar
import org.mhacks.app.core.widget.NavigationActivity
import org.mhacks.app.core.widget.NavigationColor
import org.mhacks.app.databinding.ActivityMainBinding
import org.mhacks.ratingmanager.rate.RatingManager
import javax.inject.Inject

private const val TICKET_DIALOG_FRAGMENT_TAG = "ticket_dialog_fragment"

private const val POST_ANNOUNCEMENT_FRAGMENT_TAG = "post_announcement_fragment"

/**
 * Main Activity that handles most of the interactions. Sets up the Auth Activity and loads
 * feature fragments with a bottom navigation bar.
 */
class MainActivity : NavigationActivity(), TicketDialogCallback {

    private lateinit var binding: ActivityMainBinding

    override val bottomNavigationView: BottomNavigationView
        get() = binding.mainActivityNavigation

    override val containerView: FrameLayout
        get() = binding.mainActivityFragmentContainer

    // Default value for the first fragment id reference.
    private var itemId = R.id.welcome_fragment

    private val navController by lazy {
        Navigation.findNavController(
                this,
                R.id.main_activity_fragment_host
        )
    }

    @Inject
    lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inject()
        RatingManager
                .showRateDialogIfMeetsConditions(this)
        subscribeNonUi()
        checkIfInstantApp()
    }

    private fun checkIfInstantApp() {
        val appLinkIntent = intent
        val appLinkData = appLinkIntent?.data

        if (appLinkData?.path == BuildConfig.INSTANT_APP_URL) {
            initActivity()
            return
        }

        mainViewModel.checkIfLoggedIn()
    }

    private fun showTicketDialogFragment() {
        val ft = supportFragmentManager.beginTransaction()
        val prev = supportFragmentManager.findFragmentByTag(TICKET_DIALOG_FRAGMENT_TAG)
        prev?.let {
            ft.remove(it)
        }
        ft.addToBackStack(null)
        val ticket = Fragments.Ticket.getFragment(this) as DialogFragment
        ticket.show(ft, TICKET_DIALOG_FRAGMENT_TAG)
    }

    private fun initActivity() {
        setSystemFullScreenUI()
        binding = DataBindingUtil.setContentView<ActivityMainBinding>(
                this,
                R.layout.activity_main
        )
                .apply {
                    subscribeUi(this)
                    mainActivityNavigation.menu.getItem(0).setTitle(R.string.title_home)
                    setSupportActionBar(mainActivityToolbar)
                    setupBottomNavBar(mainActivityNavigation)

                }
        // Must be set after binding is set. The navigation fragment accesses abstract values that
        // require binding to be set.
        navController.setGraph(R.navigation.nav_main)
        navigateFirst()
        setBottomNavigationColor(NavigationColor(R.color.colorPrimary, R.color.colorPrimaryDark))
    }

    private fun navigateFirst() {
        val fragment = when (intent.action) {
            EVENT_ACTION -> R.id.events_fragment
            MAP_ACTION -> R.id.map_view_fragment
            FEED_ACTION -> R.id.announcement_fragment
            INFO_ACTION -> R.id.info_fragment
            else -> R.id.welcome_fragment
        }
        navController.navigate(fragment)
    }

    private fun subscribeUi(binding: ActivityMainBinding) {
        mainViewModel.isAdmin.observe(this, Observer {
            it?.let { isAdmin ->
                val listener = if (isAdmin) {
                    View.OnClickListener {
                        showAdminOptions()
                    }
                } else {
                    View.OnClickListener {
                        showTicketDialogFragment()
                    }
                }
                binding.mainActivityQrTicketFab.setOnClickListener(listener)
            }
        })
    }

    private fun subscribeNonUi() {
        mainViewModel.auth.observe(this, Observer {
            it?.let { initActivity() } ?: run { startSignInActivity() }
        })
        mainViewModel.text.observe(this, Observer {
            binding.root.showSnackBar(it)
        })
    }

    fun navigate(@IdRes fragment: Int) {
        navController.navigate(fragment)
    }

    override fun onSupportNavigateUp() =
            findNavController(R.id.main_activity_fragment_host).navigateUp()

    private fun startSignInActivity() {
        val intent = intentTo(Activities.SignIn)
        startActivity(intent)
        finish()
    }

    private fun startQRScanActivity() {
        val intent = intentTo(Activities.QRScan)
        startActivity(intent)
    }

    private fun startPrefActivity() {
        val intent = intentTo(Activities.Preference)
        startActivity(intent)
    }

    private fun showSingleInstanceDialogFragment(addressableFragment: AddressableFragment, tag: String) {
        val ft = supportFragmentManager.beginTransaction()
        val prev = supportFragmentManager.findFragmentByTag(tag)
        prev?.let {
            ft.remove(it)
        }
        ft.addToBackStack(null)
        val ticket = addressableFragment.getFragment(this) as DialogFragment
        ticket.show(ft, tag)
    }

    // Handles the click events for bottom navigation menu
    private fun setupBottomNavBar(bottomNavigationView: BottomNavigationView) {
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            bottomNavigationView.isEnabled = false
            if (itemId != item.itemId) {
                val fragmentId = when (item.itemId) {
                    R.id.welcome_fragment -> R.id.welcome_fragment
                    R.id.announcement_fragment -> R.id.announcement_fragment
                    R.id.events_fragment -> R.id.events_fragment
                    R.id.map_view_fragment -> R.id.map_view_fragment
                    R.id.info_fragment -> R.id.info_fragment
                    else -> 0
                }
                navController.navigate(fragmentId)
                itemId = item.itemId
            }
            bottomNavigationView.isEnabled = true
            true
        }
    }

    private fun showAdminOptions() {
        AlertDialog.Builder(this)
                .setTitle(getString(R.string.admin))
                .setItems(R.array.admin_options) { _, which ->
                    when (which) {
                        0 -> startQRScanActivity()
                        1 -> showSingleInstanceDialogFragment(
                                Fragments.PostAnnouncement,
                                POST_ANNOUNCEMENT_FRAGMENT_TAG
                        )
                        2 -> showSingleInstanceDialogFragment(
                                Fragments.Ticket,
                                TICKET_DIALOG_FRAGMENT_TAG
                        )
                    }
                }.show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_main_settings -> {

                startPrefActivity()
                true
            }
            R.id.menu_main_sign_out -> {
                mainViewModel.signOut()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onTicketUnauthorized() {

        startSignInActivity()
    }

    companion object {

        const val MAP_ACTION = "org.mhacks.app.VIEW_MAP"

        const val EVENT_ACTION = "org.mhacks.app.VIEW_EVENTS"

        const val FEED_ACTION = "org.mhacks.app.VIEW_FEED"

        const val INFO_ACTION = "org.mhacks.app.VIEW_INFO"

    }
}
