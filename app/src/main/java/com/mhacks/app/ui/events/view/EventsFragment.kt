package com.mhacks.app.ui.events.view

import android.graphics.Color
import android.os.Bundle
import android.view.View
import com.mhacks.app.R
import com.mhacks.app.data.kotlin.Event
import com.mhacks.app.ui.common.NavigationFragment
import kotlinx.android.synthetic.main.fragment_events.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Omkar Moghe on 10/25/2014.
 * Made Kotlin by Tristan on 10/6/2017
 */

class EventsFragment : NavigationFragment(), EventsView {

    override var setTransparent: Boolean = false
    override var appBarTitle: Int = R.string.title_events
    override var layoutResourceID: Int = R.layout.fragment_events

    private var weekDateFormat = SimpleDateFormat("EEEE", Locale.US)

    override var onProgressStateChange: OnProgressStateChangeListener? = null

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        event_pager_tab_strip.tabIndicatorColor = Color.parseColor("#5D3E6E")
    }

    private fun bindEvents(eventList: List<Event>) {
        val list = eventList.map { events ->
            EventWithDay(weekDateFormat.format(Date(events.startDateTs!!)),
                    events)
        }
                .groupBy { it.day }
        val adapter = EventsPagerAdapter(childFragmentManager, list)
        events_pager.adapter = adapter
    }

    data class EventWithDay (
            val day: String,
            val event: Event)

    interface Callback {
        fun fetchEvents(success: (events: List<Event>) -> Unit,
                        failure: (error: Throwable) -> Unit)
    }

    companion object {
        val instance: EventsFragment
            get() = EventsFragment()
    }
}





