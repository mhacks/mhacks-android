package com.mhacks.app.ui.login.signin.view

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mhacks.app.R
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_login.*

/**
 * Fragment for the main Login component.
 */
class LoginSignInFragment : DaggerFragment(), LoginSignInView {

    private val callback: OnFromLoginFragmentCallback by lazy {
        activity as OnFromLoginFragmentCallback
    }

    override fun onCreateView(inflater: LayoutInflater?,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
            inflater?.inflate(R.layout.fragment_login, container, false)

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
        val instance get() = LoginSignInFragment()
    }
}