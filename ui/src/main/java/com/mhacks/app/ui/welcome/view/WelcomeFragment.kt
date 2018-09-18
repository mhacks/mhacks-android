package com.mhacks.app.ui.welcome.view

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.databinding.library.baseAdapters.BR.viewModel
import com.mhacks.app.data.models.Result
import com.mhacks.app.extension.showSnackBar
import com.mhacks.app.extension.viewModelProvider
import com.mhacks.app.ui.common.NavigationBindingFragment
import com.mhacks.app.ui.welcome.WelcomeViewModel
import org.mhacks.mhacksui.R
import org.mhacks.mhacksui.databinding.FragmentWelcomeBinding
import timber.log.Timber
import javax.inject.Inject

/**
 * The first screen that the user will open once they are logged in.
 *
 * Manages a ProgressBar that acts as a timer as well as builds
 */
class WelcomeFragment : NavigationBindingFragment() {

    override var setTransparent: Boolean = false

    override var appBarTitle: Int = R.string.welcome

    override var rootView: View? = null

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?): View? {

        val binding = FragmentWelcomeBinding.inflate(inflater, container, false).apply {
            viewModel = viewModelProvider(viewModelFactory)

            viewModel?.let {
                subscribeUi(it)
            }

            viewModel?.getAndCacheConfig()

            setLifecycleOwner(this@WelcomeFragment)
            rootView = root
        }

        return binding.root
    }


    private fun subscribeUi(welcomeViewModel: WelcomeViewModel) {
        welcomeViewModel.config.observe(this@WelcomeFragment, Observer {
            Timber.d("Get Configuration: Success: $it")
        })
        welcomeViewModel.snackbarMessage.observe(this@WelcomeFragment, Observer {
            rootView?.showSnackBar(
                    Snackbar.LENGTH_SHORT, it)
        })
    }

    companion object {

        val instance get() = WelcomeFragment()

    }
}
