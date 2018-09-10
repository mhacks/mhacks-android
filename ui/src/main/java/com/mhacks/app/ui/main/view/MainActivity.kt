package com.mhacks.app.ui.main.view

import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.MenuItem
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.mhacks.app.data.Constants
import com.mhacks.app.data.models.Login
import com.mhacks.app.ui.announcement.createannouncement.view.CreateAnnouncementDialogFragment
import com.mhacks.app.ui.common.BaseActivity
import com.mhacks.app.ui.common.NavigationColor
import com.mhacks.app.ui.login.LoginActivity
import com.mhacks.app.ui.main.presenter.MainPresenter
import com.mhacks.app.ui.qrscan.QRScanActivity
import com.mhacks.app.ui.ticket.view.TicketDialogFragment
import kotlinx.android.synthetic.main.activity_main.*
import org.mhacks.mhacksui.R
import org.mhacks.mhacksui.databinding.ActivityMainBinding
import javax.inject.Inject

/**
 * Main Activity that handles most of the interactions. Sets up the Login Activity and loads
 * feature fragments with a bottom navigation bar.
 */
class MainActivity : BaseActivity(), MainView,
        TicketDialogFragment.Callback {

    @Inject lateinit var mainPresenter: MainPresenter

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

        checkIfInstantApp()
    }

    private fun checkIfInstantApp() {
        val appLinkIntent = intent
        val appLinkData = appLinkIntent?.data

        if (appLinkData?.path == Constants.INSTANT_APP_PATH) {
            initActivity()
            return
        }

        mainPresenter.checkIfLoggedIn()
    }

    private fun showTicketDialogFragment() {
        val ft = supportFragmentManager.beginTransaction()
        val prev = supportFragmentManager.findFragmentByTag("ticket_dialog")
        if (prev != null) ft.remove(prev)
        ft.addToBackStack(null)
        val ticket: TicketDialogFragment = TicketDialogFragment.newInstance()
        ticket.show(ft, "ticket_dialog")
    }

    override fun onLogInSuccess(login: Login) = initActivity()

    override fun onLogInFailure() = startLoginActivity()

    override fun onCheckAdmin(isAdmin: Boolean) {
        if (isAdmin) main_activity_qr_ticket_fab.setOnClickListener { showAdminOptions() }
        else main_activity_qr_ticket_fab.setOnClickListener { showTicketDialogFragment() }
    }

    private fun initActivity() {
        setSystemFullScreenUI()
        val binding: ActivityMainBinding =
                DataBindingUtil.setContentView(this, R.layout.activity_main)

        mainPresenter.checkAdmin()
        setBottomNavigationColor(
                NavigationColor(R.color.colorPrimary, R.color.colorPrimaryDark))

        menuItem = main_activity_navigation.menu.getItem(0)
        menuItem.setTitle(R.string.title_home)

        setSupportActionBar(main_activity_toolbar)

        // Set this after action bar is set so the fragment can change the action bar color.
        navController.navigate(R.id.welcome_fragment)

        setupBottomNavBar()

    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(null,
                Navigation.findNavController(this, R.id.main_activity_fragment_host))

    }

    override fun startLoginActivity() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    private fun startQRScanActivity() =
            startActivity(Intent(this, QRScanActivity::class.java))

    private fun startCreateAnnouncementDialogFragment() {
        CreateAnnouncementDialogFragment.instance.show(supportFragmentManager, null)
    }

    // Handles the click events for bottom navigation menu
    private fun setupBottomNavBar() {
        main_activity_navigation?.setOnNavigationItemSelectedListener { item ->
            main_activity_navigation.isEnabled = false
            if (itemId != item.itemId) {
                val fragmentId = when (item.itemId) {

                    R.id.welcome_fragment -> R.id.welcome_fragment

                    R.id.announcement_fragment -> R.id.announcement_fragment

                    R.id.events_fragment -> R.id.events_fragment

                    R.id.map_view_fragment -> R.id.map_view_fragment

                    else -> 0
                }

                navController.navigate(fragmentId)
                itemId = item.itemId
            }
            main_activity_navigation.isEnabled = true
            true
        }
    }
    private fun showAdminOptions() {
        val colors = arrayOf<CharSequence>("Scan ticket", "Post an announcement", "Ticket")
        AlertDialog.Builder(this)
                .setTitle("Admin")
                .setItems(colors) { _, which ->
                    when (which) {

                        0 -> startQRScanActivity()

                        1 -> startCreateAnnouncementDialogFragment()

                        2 -> showTicketDialogFragment()
                    }
                }.show()
    }
}
