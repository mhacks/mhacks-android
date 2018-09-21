package com.mhacks.app.ui.signin

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.content.Context
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mhacks.app.data.models.Login
import com.mhacks.app.di.module.AuthModule
import com.mhacks.app.extension.showSnackBar
import com.mhacks.app.extension.viewModelProvider
import com.mhacks.app.ui.common.BaseBindingFragment
import org.mhacks.mhacksui.R
import org.mhacks.mhacksui.databinding.FragmentSigninBinding
import javax.inject.Inject

/**
 * Fragment for logging in the user.
 */
class SignInFragment : BaseBindingFragment() {

    override var rootView: View? = null

    private var callback: Callback? = null

    @Inject lateinit var authInterceptor: AuthModule.AuthInterceptor

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is Callback) callback = context
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        FragmentSigninBinding.inflate(inflater, container, false)
                .apply {
                    val viewModel = viewModelProvider<SignInViewModel>(viewModelFactory)

                    subscribeUi(viewModel)

                    emailSignInButton.setOnClickListener {
                        viewModel.postLogin(
                                Login.Request(
                                        loginEmail.text.toString(),
                                        loginPassword.text.toString()))
                    }

                    noThanksButton.setOnClickListener {
                        viewModel.skipLogin()
                    }
                    setLifecycleOwner(this@SignInFragment)
                    rootView = root
                }

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun subscribeUi(viewModel: SignInViewModel) {
        viewModel.login.observe(this, Observer {
            it?.let { login ->
                if (!login.isSkipped) {
                    authInterceptor.token = login.token
                }
                callback?.startMainActivity()
            }
        })

        viewModel.snackBarMessage.observe(this, Observer {
            it?.let { message ->
                val (textMessage, loginRequest) = message
                rootView?.showSnackBar(Snackbar.LENGTH_LONG, textMessage, R.string.try_again) {
                    viewModel.postLogin(loginRequest)
                }
            }
        })
    }

    interface Callback {

        fun startViewPagerFragment(fragment: Fragment)

        fun startMainActivity()

    }

    companion object {

        val instance get() = SignInFragment()

    }
}