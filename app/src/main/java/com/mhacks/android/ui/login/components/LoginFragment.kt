package com.mhacks.android.ui.login.components

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mhacks.android.data.kotlin.NetworkCallback
import com.mhacks.android.data.model.Login
import kotlinx.android.synthetic.main.fragment_login.*
import org.mhacks.android.R
import org.mhacks.mhacks.login.LoginActivity
import timber.log.Timber

/**
 * Fragment for the main attemptLogin component.
 */
class LoginFragment: Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        email_sign_in_button.setOnClickListener({
            val activity = (activity as LoginActivity)
            activity.attemptLogin(
                    "changjef@umich.edu",
                    "JeffJellyfish1",
                    object: NetworkCallback<Login> {
                override fun onResponseSuccess(response: Login) {
                    Timber.d(response.message)
                }

                override fun onResponseFailure(error: Throwable) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }


            })
        })
    }


    companion object {
        val instance get() = LoginFragment()
    }
}