package com.mhacks.app.ui.events.view

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mhacks.app.data.models.common.RetrofitException
import com.mhacks.app.extension.showSnackBar
import com.mhacks.app.extension.viewModelProvider
import com.mhacks.app.ui.common.NavigationFragment
import com.mhacks.app.ui.events.EventsViewModel
import kotlinx.android.synthetic.main.fragment_events.*
import org.mhacks.mhacksui.R
import org.mhacks.mhacksui.databinding.FragmentEventsBinding
import javax.inject.Inject

/**
 * Fragment to display list of events in a viewpager with tabs corresponding to the weekdays.
 */
class EventsFragment : NavigationFragment() {

    override var setTransparent: Boolean = false

    override var appBarTitle: Int = R.string.title_events

    override var rootView: View? = null

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?): View? {
        FragmentEventsBinding.inflate(inflater, container, false)
                .apply {
                    val viewModel = viewModelProvider<EventsViewModel>(
                            viewModelFactory
                    )

                    eventPagerTabStrip.tabIndicatorColor = Color.WHITE

                    subscribeUi(viewModel)
                    viewModel.getAndCacheEvents()

                    setLifecycleOwner(this@EventsFragment)
                    rootView = root
                }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showProgressBar(getString(R.string.loading_events))
    }

    private fun subscribeUi(eventsViewModel: EventsViewModel) {
        eventsViewModel.events.observe(this, Observer {
            it?.let { eventMap ->
                val adapter = EventsPagerAdapter(childFragmentManager, eventMap)
                events_pager.adapter = adapter
            }
            showMainContent()
        })

        eventsViewModel.error.observe(this, Observer { error ->
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
        eventsViewModel.snackbarMessage.observe(this, Observer {
            it?.let { textMessage ->
                rootView?.showSnackBar(
                        Snackbar.LENGTH_SHORT, textMessage)
            }
        })
    }

    companion object {

        val instance: EventsFragment
            get() = EventsFragment()
    }
}





