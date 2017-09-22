package com.mhacks.android.ui.events

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.mhacks.android.data.kotlin.Events
import com.mhacks.android.ui.common.BaseFragment
import com.mhacks.android.ui.events.EventsSection.EventSectionModel
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter
import kotlinx.android.synthetic.main.fragment_events.*
import org.mhacks.android.R
import timber.log.Timber
import kotlin.collections.ArrayList

/**
 * Created by Omkar Moghe on 10/25/2014.
 * Made Kotlin by Tristan on 10/6/2017
 */

class EventsFragment : BaseFragment() {

    private lateinit var listAdapter: SectionedEventsAdapter

    override var setTransparent: Boolean = false
    override var AppBarTitle: Int = R.string.title_events
    override var LayoutResourceID: Int = R.layout.fragment_events

    val adapter = SectionedRecyclerViewAdapter()

    private val callback by lazy { activity as Callback }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        events_recycler_view.setHasFixedSize(true)
        events_recycler_view.layoutManager = LinearLayoutManager(activity)

        callback.fetchEvents(
                { events -> bindEvents(events)

                },
                { error -> Timber.e(error) })

        listAdapter = SectionedEventsAdapter()

        events_recycler_view.adapter = listAdapter
    }

    private fun bindEvents(events: List<Events>) {
        val groupByDateEvents = events.groupBy { it.startDateTs }
        val eventSections = ArrayList<EventSectionModel>()
        for ((key, value) in groupByDateEvents) {
            eventSections.add(EventSectionModel(key, ArrayList(value)))
        }
        listAdapter.addAllEventsSections(eventSections)

//        for (event in groupByDateEvents) {
//
//
//            listAdapter.addSection(EventsSection(EventSectionModel(event.key, ArrayList(event.value))))
//        }




    }


    interface Callback {
        fun fetchEvents(success: (events: List<Events>) -> Unit,
                        failure: (error: Throwable) -> Unit)
    }


    companion object {
        val instance: EventsFragment
            get() = EventsFragment()
    }
}


