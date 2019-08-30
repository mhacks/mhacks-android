package org.mhacks.app.events.widget

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.jakewharton.threetenabp.AndroidThreeTen
import org.mhacks.app.core.ktx.showSnackBar
import org.mhacks.app.core.widget.NavigationFragment
import org.mhacks.app.eventlibrary.data.model.Event
import org.mhacks.app.core.data.model.RetrofitException
import org.mhacks.app.event.R
import org.mhacks.app.event.databinding.FragmentEventBinding
import org.mhacks.app.eventlibrary.EventViewModel
import org.mhacks.app.events.di.inject
import timber.log.Timber
import javax.inject.Inject

/**
 * Fragment to display list of events in a viewpager with tabs corresponding to the weekdays.
 */
class EventFragment : NavigationFragment() {

    override var transparentToolbarColor: Int? = null

    override var appBarTitle: Int = R.string.title_events

    override var rootView: View? = null

    private lateinit var binding: FragmentEventBinding

    @Inject lateinit var viewModel: EventViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?): View? {
        AndroidThreeTen.init(context)
        inject()
        binding = FragmentEventBinding.inflate(inflater, container, false)
                .apply {
                    eventPagerTabStrip.tabIndicatorColor = Color.WHITE

                    subscribeUi(viewModel)
                    viewModel.getAndCacheEvents()

                    lifecycleOwner = this@EventFragment
                    rootView = root
                }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showProgressBar(getString(R.string.loading_events))
    }

    private fun subscribeUi(eventsViewModel: EventViewModel) {
        eventsViewModel.events.observe(viewLifecycleOwner, Observer {
            it?.let { eventMap ->
                val adapter = EventsPagerAdapter(
                        childFragmentManager,
                        eventMap,
                        ::onEventsClicked)
                binding.eventsPager.adapter = adapter
            }
            showMainContent()
        })

        eventsViewModel.favoriteEvent.observe(viewLifecycleOwner, Observer {
            Timber.d("Event favorited.")
        })

        eventsViewModel.error.observe(viewLifecycleOwner, Observer { error ->
            when (error) {
                RetrofitException.Kind.NETWORK -> {
                    showErrorView(R.string.events_network_failure) {
                        showProgressBar(getString(R.string.loading_events))
                    }
                }
                else -> {
                    // no-op
                }
            }
        })
        eventsViewModel.snackbarText.observe(this, Observer {
            it?.let { textMessage ->
                rootView?.showSnackBar(textMessage)
            }
        })
    }

    private fun onEventsClicked(event: Event, isChecked: Boolean) {
        Timber.d("Event %s was clicked:", event.id)
        event.favorited = isChecked
        viewModel.insertFavoriteEvent(event)
    }

    companion object {

        val instance: EventFragment
            get() = EventFragment()
    }
}

