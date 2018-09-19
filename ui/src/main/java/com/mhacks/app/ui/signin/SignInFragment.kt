package com.mhacks.app.ui.signin

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import org.mhacks.mhacksui.R
import com.mhacks.app.di.module.AuthModule
import com.mhacks.app.ui.common.BaseFragment
import kotlinx.android.synthetic.main.fragment_login.*
import javax.inject.Inject

/**
 * Fragment for logging in the user.
 */
class SignInFragment : BaseFragment() {

    override var layoutResourceID = R.layout.fragment_login

    private var callback: Callback? = null

    @Inject lateinit var authInterceptor: AuthModule.AuthInterceptor

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        email_sign_in_button.setOnClickListener {
//            showProgressBar(getString(R.string.logging_in))
//            loginSignInPresenter.postLogin(
//                    login_email.text.toString(),
//                    login_password.text.toString())
//        }
//        no_thanks_button.setOnClickListener {
//            loginSignInPresenter.skipLogin()
//            callback?.startMainActivity()
//        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
//        if (context is Callback) callback = context
//        loginSignInPresenter.onAttach()
    }

    override fun onDetach() {
        super.onDetach()
//        loginSignInPresenter.onDetach()
    }

//    override fun postLoginSuccess(login: Login) {
//        authInterceptor.token = login.token
//        callback?.startMainActivity()
//    }
//
//    override fun postLoginFailure(username: String, password: String, error: Throwable) {
//        showMainContent()
//        when(error) {
//            is HttpException -> {
//                when (error.code()) {
//                    401 -> Snackbar.make(view!!,
//                            R.string.logging_in_auth_error,
//                            Snackbar.LENGTH_SHORT).show()
//                }
//            }
//            is UnknownHostException ->
//                Snackbar.make(view!!, R.string.no_internet, Snackbar.LENGTH_INDEFINITE)
//                        .setActionTextColor(Color.WHITE)
//                        .setAction(R.string.try_again) {
//                            loginSignInPresenter.postLogin(username, password) }
//                        .show()
//        }
//    }

//    override fun skipLoginSuccess() {
//        callback?.startMainActivity()
//    }

    interface Callback {

        fun startViewPagerFragment(fragment: Fragment)

        fun startMainActivity()

    }

    companion object {

        val instance get() = SignInFragment()

    }
}