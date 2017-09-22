package com.mhacks.android.ui.events

import android.graphics.Color
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.mhacks.android.data.kotlin.Event
import com.mhacks.android.ui.common.BaseFragment
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter
import kotlinx.android.synthetic.main.fragment_events.*
import org.mhacks.android.R
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by Omkar Moghe on 10/25/2014.
 * Made Kotlin by Tristan on 10/6/2017
 */

class EventsFragment : BaseFragment() {

    override var setTransparent: Boolean = false
    override var AppBarTitle: Int = R.string.title_events
    override var LayoutResourceID: Int = R.layout.fragment_events

    private var weekDateFormat = SimpleDateFormat("EEEE", Locale.US)

    private val callback by lazy { activity as Callback }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        event_pager_tab_strip.tabIndicatorColor = Color.parseColor("#5D3E6E")
        callback.fetchEvents(
                { events -> bindEvents(events) },
                { error -> Timber.e(error) })
    }

    private fun bindEvents(eventList: List<Event>) {
        val list = eventList.map { events ->
            EventWithDay(weekDateFormat.format(Date(events.startDateTs!!)),
                    events)  }
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





