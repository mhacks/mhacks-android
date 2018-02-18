package com.mhacks.app.ui.main.view

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.view.MenuItem
import com.mhacks.app.R
import com.mhacks.app.data.kotlin.Login
import com.mhacks.app.ui.announcement.view.AnnouncementFragment
import com.mhacks.app.ui.common.BaseActivity
import com.mhacks.app.ui.common.NavigationColor
import com.mhacks.app.ui.events.view.EventsFragment
import com.mhacks.app.ui.login.LoginActivity
import com.mhacks.app.ui.main.presenter.MainPresenter
import com.mhacks.app.ui.map.view.MapViewFragment
import com.mhacks.app.ui.ticket.view.TicketDialogFragment
import com.mhacks.app.ui.welcome.view.WelcomeFragment
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

/**
 * Main Activity that handles most of the interactions. Sets up the Login Activity and loads
 * feature fragments with a bottom navigation bar.
 */
class MainActivity : BaseActivity(), MainView,
        TicketDialogFragment.Callback {

    @Inject lateinit var mainPresenter: MainPresenter

    private lateinit var menuItem: MenuItem

    private var itemId = R.id.navigation_home

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.MHacksTheme)
        mainPresenter.onCheckIfLoggedIn()
    }

    private fun showTicketDialogFragment() {
        val ft = supportFragmentManager.beginTransaction()
        val prev = supportFragmentManager.findFragmentByTag("ticket_dialog")
        if (prev != null) ft.remove(prev)
        ft.addToBackStack(null)
        val ticket: TicketDialogFragment = TicketDialogFragment.newInstance()

        ticket.show(ft, "ticket_dialog")
    }

    private fun updateFragment(fragment: Fragment?) {
        supportFragmentManager.beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .replace(R.id.fragment_container, fragment)
                .commit()
    }

    override fun onLogInSuccess(login: Login) = initActivity()

    override fun onLogInFailure() = startLoginActivity()

    private fun initActivity() {
        setSystemFullScreenUI()
        setContentView(R.layout.activity_main)
        setBottomNavigationColor(
                NavigationColor(R.color.colorPrimary, R.color.colorPrimaryDark))
        qr_ticket_fab.setOnClickListener({
            showTicketDialogFragment()
        })
        menuItem = main_activity_navigation.menu.getItem(0)
        menuItem.setTitle(R.string.title_home)
        setSupportActionBar(toolbar)
        updateFragment(WelcomeFragment.instance)
        main_activity_navigation?.setOnNavigationItemSelectedListener({ item ->
            if (itemId != item.itemId) {
                when (item.itemId) {
                    R.id.navigation_home -> updateFragment(WelcomeFragment.instance)
                    R.id.navigation_announcements -> updateFragment(AnnouncementFragment.instance)
                    R.id.navigation_events -> updateFragment(EventsFragment.instance)
                    R.id.navigation_map -> updateFragment(MapViewFragment.instance)
                }
                itemId = item.itemId
            }
            true
        })
    }

    override fun startLoginActivity() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}
