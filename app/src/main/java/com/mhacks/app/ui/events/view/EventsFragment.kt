package com.mhacks.app.ui.events.view

import android.graphics.Color
import android.os.Bundle
import android.view.View
import com.mhacks.app.R
import com.mhacks.app.data.kotlin.Event
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

    private var weekDateFormat = SimpleDateFormat("EEEE", Locale.US)

    @Inject lateinit var eventsFragmentPresenter: EventsFragmentPresenter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        event_pager_tab_strip.tabIndicatorColor = Color.parseColor("#5D3E6E")
        eventsFragmentPresenter
    }

    private fun bindEvents(eventList: List<Event>) {
        val list = eventList.map { events ->
            EventWithDay(weekDateFormat.format(Date(events.startDateTs)),
                    events)
        }
                .groupBy { it.day }
        val adapter = EventsPagerAdapter(childFragmentManager, list)
        events_pager.adapter = adapter
    }

    override fun onGetEventsSuccess(events: List<Event>) {

    }

    override fun onGetEventsFailure(error: Throwable) {

    }

    data class EventWithDay (
            val day: String,
            val event: Event)

    companion object {
        val instance: EventsFragment
            get() = EventsFragment()
    }
}





