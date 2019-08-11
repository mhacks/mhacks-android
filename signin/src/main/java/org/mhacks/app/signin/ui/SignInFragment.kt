package org.mhacks.app.signin.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import org.mhacks.app.core.ktx.showSnackBar
import org.mhacks.app.core.widget.BaseFragment
import org.mhacks.app.signin.databinding.FragmentSigninBinding
import org.mhacks.app.signin.usecase.AuthRequest
import org.mhacks.app.R as coreR

/**
 * Fragment for logging in the user.
 */
class SignInFragment : BaseFragment() {

    private lateinit var binding: FragmentSigninBinding

    override var rootView: View? = null

    private var callback: Callback? = null

//    @Inject
//    lateinit var authInterceptor: AuthModule.AuthInterceptor

    private lateinit var viewModel: SignInViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is Callback)
            callback = context
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSigninBinding.inflate(inflater, container, false)
                .apply {

                    subscribeUi(viewModel)

                    emailSignInButton.setOnClickListener {
                        viewModel.postAuth(
                                AuthRequest(
                                        loginEmail.text.toString(),
                                        loginPassword.text.toString()))
                    }

                    noThanksButton.setOnClickListener {
                        viewModel.skipAuth()
                    }
                    lifecycleOwner = this@SignInFragment
                    rootView = root
                }

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun subscribeUi(viewModel: SignInViewModel) {
        viewModel.auth.observe(this, Observer {
            it?.let { login ->
                //                if (!login.isSkipped) {
//                    authInterceptor.token = login.token
//                }
                callback?.startMainActivity()
            }
        })

        viewModel.snackBarMessage.observe(this, Observer {
            it?.let { message ->
                val (textMessage, loginRequest) = message

                binding.root.showSnackBar(Snackbar.LENGTH_LONG, textMessage, coreR.string.try_again) {
                    viewModel.postAuth(loginRequest)
                }
            }
        })
    }

    interface Callback {

        fun startMainActivity()

    }
}