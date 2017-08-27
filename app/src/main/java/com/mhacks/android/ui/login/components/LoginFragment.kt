package com.mhacks.android.ui.login.components

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_login.*
import org.mhacks.android.R
import org.mhacks.mhacks.login.LoginActivity

/**
 * Fragment for the main login component.
 */
class LoginFragment: Fragment(), OnClickListener{

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_login, container, false)

        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        email_sign_in_button.setOnClickListener(this);
    }
    override fun onClick(v: View?) {
        (activity as LoginActivity)
                .switchFragment(LoginViewPagerFragment.instance)
    }


    companion object {
        val instance
            get() = LoginFragment()
    }


}
