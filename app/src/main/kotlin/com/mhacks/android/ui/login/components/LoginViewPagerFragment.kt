package com.mhacks.android.ui.login.components

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_viewpager_login.*
import org.mhacks.android.R



/**
 * Fragment for the main login component.
 * This manages the Views managed by the ViewPager.
 */

class LoginViewPagerFragment: Fragment(){

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_viewpager_login, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            login_viewpager.setAdapter(LoginViewPagerAdapter(context))
        }
    companion object {
        val instance
            get() = LoginViewPagerFragment()
    }
}