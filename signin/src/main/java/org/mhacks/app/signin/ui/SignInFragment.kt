package org.mhacks.app.signin.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.BaseTransientBottomBar
import org.mhacks.app.core.ktx.showSnackBar
import org.mhacks.app.core.widget.BaseFragment
import org.mhacks.app.signin.R
import org.mhacks.app.signin.databinding.FragmentSignInBinding
import org.mhacks.app.signin.inject
import org.mhacks.app.signin.usecase.AuthRequest
import javax.inject.Inject
import org.mhacks.app.core.R as coreR

/**
 * Fragment for logging in the user.
 */
class SignInFragment : BaseFragment() {

    private lateinit var binding: FragmentSignInBinding

    override var rootView: View? = null

    private var callback: Callback? = null

    private val authRequest: AuthRequest
        get() = AuthRequest(
                binding.fragmentSignInEmailEditText.text.toString(),
                binding.fragmentSignInPasswordEditText.text.toString()
        )

    @Inject
    lateinit var viewModel: SignInViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is Callback)
            callback = context
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        inject()
        setLoadingBackground(R.drawable.mhacks_wallpaper)
        binding = FragmentSignInBinding.inflate(inflater, container, false)
                .apply {
                    subscribeUi(viewModel)
                    fragmentSignInEmailSubmitButton.setOnClickListener {
                        viewModel.postAuth(authRequest)
                    }

                    fragmentSignInNoThanksButton.setOnClickListener {
                        viewModel.skipAuth()
                    }
                    fragmentSignInPasswordEditText.setOnEditorActionListener { _, actionId, _ ->
                        if (actionId == EditorInfo.IME_ACTION_DONE) {
                            viewModel.postAuth(authRequest)
                            return@setOnEditorActionListener true
                        }
                        false
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
        viewModel.loginFail.observe(this, Observer {
            showMainContent()
            it?.let { message ->
                val (text, authRequest) = message
                authRequest?.let {
                    binding.root.showSnackBar(
                            BaseTransientBottomBar.LENGTH_LONG,
                            text,
                            coreR.string.try_again
                    ) {
                        viewModel.postAuth(it)
                    }
                } ?: run {
                    binding.root.showSnackBar(text)
                }
            }

        })
        viewModel.loading.observe(viewLifecycleOwner, Observer {
            showProgressBar(R.string.signing_in)
        })
    }

    interface Callback {

        fun startMainActivity()

    }
}