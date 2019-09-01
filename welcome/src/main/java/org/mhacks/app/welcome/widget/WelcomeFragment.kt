package org.mhacks.app.welcome.widget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.jakewharton.threetenabp.AndroidThreeTen
import org.mhacks.app.core.ktx.showSnackBar
import org.mhacks.app.core.widget.NavigationFragment
import org.mhacks.app.eventlibrary.EventViewModel
import org.mhacks.app.eventlibrary.data.model.Event
import org.mhacks.app.ui.main.MainActivity
import org.mhacks.app.welcome.R
import org.mhacks.app.R as mainR
import org.mhacks.app.welcome.WelcomeViewModel
import org.mhacks.app.welcome.databinding.FragmentWelcomeBinding
import org.mhacks.app.welcome.di.inject
import org.mhacks.app.welcome.widget.favoriteevents.FavoriteEventsRecyclerViewAdapter
import timber.log.Timber
import javax.inject.Inject

/**
 * The first screen that the user will open once they are logged in.
 *
 * Manages a ProgressBar that acts as a timer as well as builds
 */
class WelcomeFragment : NavigationFragment() {

    override var transparentToolbarColor: Int? = null

    override var appBarTitle: Int = R.string.welcome

    override var rootView: View? = null

    private lateinit var binding: FragmentWelcomeBinding

    @Inject
    lateinit var welcomeViewModel: WelcomeViewModel

    @Inject
    lateinit var eventsViewModel: EventViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?): View? {
        AndroidThreeTen.init(context)
        inject()

        binding = FragmentWelcomeBinding.inflate(inflater, container, false)
                .apply {
                    viewModel = welcomeViewModel
                    subscribeEventsUi(eventsViewModel, this)
                    subscribeUi(welcomeViewModel, this)
                    welcomeViewModel.getAndCacheConfig()
                    eventsViewModel.getFavoriteEvents()

                    lifecycleOwner = this@WelcomeFragment
                    rootView = root
                }

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun subscribeUi(
            welcomeViewModel: WelcomeViewModel,
            fragmentWelcomeBinding: FragmentWelcomeBinding) {
        welcomeViewModel.config.observe(this@WelcomeFragment, Observer {
            Timber.d("Get Configuration: Success: $it")
        })
        welcomeViewModel.snackBarMessage.observe(this@WelcomeFragment, Observer {
            rootView?.showSnackBar(it)
        })
        welcomeViewModel.firstTimerProgress.observe(this@WelcomeFragment, Observer {
            it?.let { percent ->
                fragmentWelcomeBinding.welcomeFragmentProgressbarCounter.setPercentage(percent)
            }
        })
    }

    private fun subscribeEventsUi(
            eventsViewModel: EventViewModel,
            fragmentWelcomeBinding: FragmentWelcomeBinding
    ) {
        eventsViewModel.favoriteEvents.observe(this@WelcomeFragment, Observer {

            it.let { events ->
                fragmentWelcomeBinding.apply {
                    if (events.isEmpty()) {
                        welcomeFragmentFavoriteAddEventsButton.visibility = View.VISIBLE
                        welcomeFragmentFavoriteAddEventsButton.setOnClickListener {
                            (activity as? MainActivity)?.navigate(mainR.id.events_fragment)
                        }
                    } else {
                        welcomeFragmentFavoriteEventsRecyclerView.layoutManager =
                                LinearLayoutManager(context)
                        welcomeFragmentFavoriteEventsRecyclerView.adapter =
                                FavoriteEventsRecyclerViewAdapter(events, ::onEventsClicked)
                    }
                }
            }
        })
    }

    private fun onEventsClicked(event: Event, isChecked: Boolean) {
        Timber.d("Event %s was clicked:", event.id)
        event.favorited = isChecked
        eventsViewModel.insertFavoriteEvent(event)
    }

}
