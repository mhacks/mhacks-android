package com.mhacks.android.ui.login.components

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_login.*
import org.mhacks.x.R

/**
 * Fragment for the main Login component.
 */

class LoginFragment: Fragment() {

    private val callback: OnFromLoginFragmentCallback by lazy {
        activity as OnFromLoginFragmentCallback
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        email_sign_in_button.setOnClickListener({
            callback.attemptLogin(
                    login_email.text.toString(),
                    login_password.text.toString())
        })

        no_thanks_button.setOnClickListener({
            callback.skipAndGoToMainActivity()
        })
    }


    interface OnFromLoginFragmentCallback {

        fun attemptLogin(email: String, password: String)

        fun goToViewPagerFragment(fragment: Fragment)

        fun skipAndGoToMainActivity()

        fun showSnackBar(text: String)

    }

    companion object {
        val instance get() = LoginFragment()
    }
}