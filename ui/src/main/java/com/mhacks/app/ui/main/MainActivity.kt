package com.mhacks.app.ui.main

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx
import com.mhacks.app.data.Constants
import com.mhacks.app.ui.createannouncement.CreateAnnouncementDialogFragment
import com.mhacks.app.ui.common.BaseActivity
import com.mhacks.app.ui.common.NavigationColor
import com.mhacks.app.ui.signin.SignInActivity
import com.mhacks.app.ui.qrscan.QRScanActivity
import com.mhacks.app.ui.ticket.TicketDialogFragment
import org.mhacks.mhacksui.R
import org.mhacks.mhacksui.databinding.ActivityMainBinding
import javax.inject.Inject

@SuppressLint("Registered")
/**
 * Main Activity that handles most of the interactions. Sets up the Login Activity and loads
 * feature fragments with a bottom navigation bar.
 */
class MainActivity : BaseActivity(),
        TicketDialogFragment.Callback {

    @Inject lateinit var mainViewModel: MainViewModel

    private lateinit var menuItem: MenuItem

    // Default value for the first fragment id reference.
    private var itemId = R.id.welcome_fragment

    private val navController by lazy {
        Navigation.findNavController(
                this,
                R.id.main_activity_fragment_host)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.MHacksTheme)

        subscribeNonUi()

        checkIfInstantApp()
    }

    private fun checkIfInstantApp() {
        val appLinkIntent = intent
        val appLinkData = appLinkIntent?.data

        if (appLinkData?.path == Constants.INSTANT_APP_PATH) {
            initActivity()
            return
        }

        mainViewModel.checkIfLoggedIn()
    }

    private fun showTicketDialogFragment() {
        val ft = supportFragmentManager.beginTransaction()
        val prev = supportFragmentManager.findFragmentByTag("ticket_dialog")
        if (prev != null) ft.remove(prev)
        ft.addToBackStack(null)
        val ticket: TicketDialogFragment = TicketDialogFragment.newInstance()
        ticket.show(ft, "ticket_dialog")
    }

    private fun initActivity() {
        setSystemFullScreenUI()
        DataBindingUtil.setContentView<ActivityMainBinding>(
                this, R.layout.activity_main).apply {
            subscribeUi(this)
            menuItem = mainActivityNavigation.menu.getItem(0)

            setBottomNavigationColor(
                    NavigationColor(R.color.colorPrimary, R.color.colorPrimaryDark))

            menuItem.setTitle(R.string.title_home)

            setSupportActionBar(mainActivityToolbar)

            // Set this after action bar is set so the fragment can change the action bar color.
            navController.navigate(R.id.welcome_fragment)

            setupBottomNavBar(mainActivityNavigation)
        }
    }

    private fun subscribeUi(binding: ActivityMainBinding) {
        mainViewModel.isAdmin.observe(this, Observer {
            it?.let { isAdmin ->
                val listener = if (isAdmin) {
                    View.OnClickListener { _ ->
                        showAdminOptions()
                    }
                } else {
                    View.OnClickListener { _ ->
                        showTicketDialogFragment()
                    }
                }
                binding.mainActivityQrTicketFab.setOnClickListener(listener)
            }
        })
    }

    private fun subscribeNonUi() {
        mainViewModel.login.observe(this, Observer {
            it?.let { _ ->
                initActivity()
            } ?: run {
                startLoginActivity()
            }

        })

        mainViewModel.textMessage.observe(this, Observer {
            it?.let { textMessage ->
                showSnackBar(textMessage)
            }
        })
    }

    // No Android X dependency yet.
    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(null,
                Navigation.findNavController(this, R.id.main_activity_fragment_host))
    }

    override fun startLoginActivity() {
        startActivity(Intent(this, SignInActivity::class.java))
        finish()
    }

    private fun startQRScanActivity() =
            startActivity(Intent(this, QRScanActivity::class.java))


    private fun showCreateAnnouncementDialogFragment() {
        val fragment = CreateAnnouncementDialogFragment.instance
        fragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.Theme_AppCompat_Light_Dialog)
        fragment.show(supportFragmentManager, null)
    }

    // Handles the click events for bottom navigation menu
    private fun setupBottomNavBar(bottomNavigationView: BottomNavigationViewEx) {

        bottomNavigationView.enableAnimation(false)
        bottomNavigationView.enableShiftingMode(false)
        bottomNavigationView.enableItemShiftingMode(false)

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


    fun navigateFragment(fragmentId: Int) {
        navController.navigate(fragmentId)
    }


    private fun showAdminOptions() {
        AlertDialog.Builder(this)
                .setTitle(getString(R.string.admin))
                .setItems(R.array.admin_options) { _, which ->
                    when (which) {

                        0 -> startQRScanActivity()

                        1 -> showCreateAnnouncementDialogFragment()

                        2 -> showTicketDialogFragment()
                    }
                }.show()
    }
}
