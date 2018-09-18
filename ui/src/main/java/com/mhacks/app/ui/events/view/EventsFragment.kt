package com.mhacks.app.ui.events.view

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.View
import org.mhacks.mhacksui.R
import com.mhacks.app.data.models.Event
import com.mhacks.app.ui.common.NavigationFragment
import com.mhacks.app.ui.events.presenter.EventsFragmentPresenter
import kotlinx.android.synthetic.main.fragment_events.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

/**
 * Fragment to display list of events in a viewpager with tabs corresponding to the weekdays.
 */
class EventsFragment : NavigationFragment(), EventsView {

    override var setTransparent: Boolean = false

    override var appBarTitle: Int = R.string.title_events

    override var layoutResourceID: Int = R.layout.fragment_events

    @Inject lateinit var eventsFragmentPresenter: EventsFragmentPresenter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        event_pager_tab_strip.tabIndicatorColor = Color.WHITE
        showProgressBar( getString(R.string.loading_events))
        eventsFragmentPresenter.getEvents()
    }

    private fun bindEvents(eventList: List<Event>) {
        val list = eventList.asSequence().map { events ->
            EventWithDay(weekDateFormat.format(Date(events.startDateTs)),
                    events) }
                .groupBy { it.day }
        val adapter = EventsPagerAdapter(childFragmentManager, list)
        events_pager.adapter = adapter
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        eventsFragmentPresenter.onAttach()
    }

    override fun onDetach() {
        super.onDetach()
        eventsFragmentPresenter.onDetach()
    }

    override fun onGetEventsSuccess(events: List<Event>) {
        showMainContent()
        bindEvents(events)
    }

    override fun onGetEventsFailure(error: Throwable) =
        showErrorView(R.string.events_network_failure) {
            showProgressBar(getString(R.string.loading_events))
            eventsFragmentPresenter.getEvents()
        }

    data class EventWithDay(
            val day: String,
            val event: Event)

    companion object {
        val instance: EventsFragment
            get() = EventsFragment()

        val weekDateFormat = SimpleDateFormat("EEEE", Locale.getDefault())
    }
}





