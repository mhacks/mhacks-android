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
import org.mhacks.app.signin.databinding.FragmentSignInBinding
import org.mhacks.app.signin.inject
import org.mhacks.app.signin.usecase.AuthRequest
import javax.inject.Inject
import org.mhacks.app.R as coreR

/**
 * Fragment for logging in the user.
 */
class SignInFragment : BaseFragment() {

    private lateinit var binding: FragmentSignInBinding

    override var rootView: View? = null

    private var callback: Callback? = null

    @Inject lateinit var viewModel: SignInViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is Callback)
            callback = context
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        inject()
        binding = FragmentSignInBinding.inflate(inflater, container, false)
                .apply {
                    subscribeUi(viewModel)
                    fragmentSignInEmailSubmitButton.setOnClickListener {
                        viewModel.postAuth(
                                AuthRequest(
                                        fragmentSignInEmailEditText.text.toString(),
                                        fragmentSignInPasswordEditText.text.toString()))
                    }

                    fragmentSignInNoThanksButton.setOnClickListener {
                        viewModel.skipAuth()
                    }
                    lifecycleOwner = this@SignInFragment
                    rootView = root
                }

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun subscribeUi(viewModel: SignInViewModel) {
        viewModel.auth.observe(this, Observer {
            callback?.startMainActivity()
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