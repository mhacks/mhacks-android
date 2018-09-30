package com.mhacks.app.ui.welcome.widget

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mhacks.app.extension.showSnackBar
import com.mhacks.app.extension.viewModelProvider
import com.mhacks.app.ui.common.NavigationFragment
import com.mhacks.app.ui.events.EventsViewModel
import com.mhacks.app.ui.main.MainActivity
import com.mhacks.app.ui.welcome.WelcomeViewModel
import com.mhacks.app.ui.welcome.widget.favoriteevents.FavoriteEventsRecyclerViewAdapter
import org.mhacks.mhacksui.R
import org.mhacks.mhacksui.databinding.FragmentWelcomeBinding
import timber.log.Timber
import javax.inject.Inject

/**
 * The first screen that the user will open once they are logged in.
 *
 * Manages a ProgressBar that acts as a timer as well as builds
 */
class WelcomeFragment : NavigationFragment() {

    override var setTransparent: Boolean = false

    override var appBarTitle: Int = R.string.welcome

    override var rootView: View? = null

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory

    private var eventsViewModel: EventsViewModel? = null

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?): View? {

        val binding =
                FragmentWelcomeBinding.inflate(inflater, container, false).apply {
                    viewModel = viewModelProvider(viewModelFactory)

                    eventsViewModel = viewModelProvider(viewModelFactory)

                    subscribeEventsUi(
                            eventsViewModel!!,
                            this
                    )

                    viewModel?.let {
                        subscribeUi(it, this)
                    }

                    viewModel?.getAndCacheConfig()
                    eventsViewModel?.getFavoriteEvents()

                    setLifecycleOwner(this@WelcomeFragment)
                    rootView = root
                }

        return binding.root
    }
    
    private fun subscribeUi(
            welcomeViewModel: WelcomeViewModel,
            fragmentWelcomeBinding: FragmentWelcomeBinding) {
        welcomeViewModel.config.observe(this@WelcomeFragment, Observer {
            Timber.d("Get Configuration: Success: $it")
        })
        welcomeViewModel.snackBarMessage.observe(this@WelcomeFragment, Observer {
            rootView?.showSnackBar(
                    Snackbar.LENGTH_SHORT, it)
        })
        welcomeViewModel.firstTimerProgress.observe(this@WelcomeFragment, Observer {
            it?.let { percent ->
                fragmentWelcomeBinding.welcomeFragmentProgressbarCounter.setPercentage(percent)
            }
        })
    }

    private fun subscribeEventsUi(
            eventsViewModel: EventsViewModel,
            fragmentWelcomeBinding: FragmentWelcomeBinding
    ) {
        eventsViewModel.favoriteEvents.observe(this@WelcomeFragment, Observer {

            it?.let { events ->
                fragmentWelcomeBinding.apply {
                    if (events.isEmpty()) {
                        welcomeFragmentFavoriteAddEventsButton.visibility = View.VISIBLE
                        welcomeFragmentFavoriteAddEventsButton.setOnClickListener { _ ->
                            (activity as? MainActivity)?.
                                    navigateFragment(R.id.events_fragment)
                        }
                    } else {
                        welcomeFragmentFavoriteEventsRecyclerView.layoutManager =
                                LinearLayoutManager(context)
                        welcomeFragmentFavoriteEventsRecyclerView.adapter =
                                FavoriteEventsRecyclerViewAdapter(events, null)
                    }
                }
            }
        })
    }

    companion object {

        val instance get() = WelcomeFragment()

    }
}
