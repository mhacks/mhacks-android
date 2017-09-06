package com.mhacks.android.ui.login.components

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mhacks.android.data.model.Login
import kotlinx.android.synthetic.main.fragment_login.*
import org.mhacks.android.R
import org.mhacks.mhacks.login.LoginActivity
import timber.log.Timber

/**
 * Fragment for the main login component.
 */
class LoginFragment: Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        email_sign_in_button.setOnClickListener({
            val activity = (activity as LoginActivity)
            activity.login(object: LoginActivity.OnLoginRequestCallback {
                override fun onLoginSuccess(login: Login) {
                    Timber.i(login.id.toString())
                }
                override fun onLoginFailure(error: Throwable) {
                    Timber.e(error)
                }
            })
        })
    }


    companion object {
        val instance get() = LoginFragment()
    }
}