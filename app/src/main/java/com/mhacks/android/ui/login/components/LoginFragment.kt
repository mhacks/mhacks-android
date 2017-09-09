package com.mhacks.android.ui.login.components

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mhacks.android.data.model.Login
import com.mhacks.android.ui.MainActivity
import com.mhacks.android.ui.login.LoginActivity
import kotlinx.android.synthetic.main.fragment_login.*
import org.mhacks.android.R
import java.lang.Exception

/**
 * Fragment for the main Login component.
 */

class LoginFragment: Fragment() {

    private val parentActivity: LoginActivity by lazy {
        activity as LoginActivity
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        email_sign_in_button.setOnClickListener({


//            parentActivity.networkSingleton.getLoginVerification(
//                    email = login_email.text.toString(),
//                    password = login_password.text.toString(),
//                    success = this::onLoginNetworkSuccess,
//                    failure = this::onLoginNetworkFailure
//            )
        })
        no_thanks_button.setOnClickListener({
            parentActivity.roomSingleton.insertLogin(
                    Login(1, true, false, "", ""))
            parentActivity.startActivity(Intent(context, MainActivity::class.java))
        })
    }


    private fun onLoginNetworkSuccess(login: Login) {
        if (login.status) {
            parentActivity.roomSingleton.insertLogin(login)
            parentActivity.goToFragment(LoginViewPagerFragment.instance)
        }
    }
    private fun onLoginNetworkFailure(error: Throwable) {
        when (error) {
            is retrofit2.HttpException ->
                Snackbar.make(view!!, "Username or password is invalid", Snackbar.LENGTH_SHORT).show()
            else ->
                Snackbar.make(view!!, "Unknown error.", Snackbar.LENGTH_SHORT).show()
        }
    }

    companion object {
        val instance get() = LoginFragment()
    }
}