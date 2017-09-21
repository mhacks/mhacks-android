package com.mhacks.android.ui.events

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.mhacks.android.data.kotlin.Events
import com.mhacks.android.ui.common.BaseFragment
import com.mhacks.android.ui.map.MapViewFragment
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

    private lateinit var listAdapter: SectionedRecyclerViewAdapter

    override var setTransparent: Boolean = false
    override var AppBarTitle: Int = R.string.title_events
    override var LayoutResourceID: Int = R.layout.fragment_events

    private val callback by lazy { activity as Callback }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        events_recycler_view.setHasFixedSize(true)
        events_recycler_view.layoutManager = LinearLayoutManager(activity)

        callback.fetchEvents(
                { events -> Timber.d(events[0].category) },
                { error -> Timber.e(error) })

//        val list = getEventSectionModelList()
//        listAdapter = sectionedEventsAdapter(list)
//        events_recycler_view.adapter = listAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // TODO cancel active requests
    }

    private fun getEventSectionModelList(): ArrayList<EventsSection.EventSectionModel> {

        val sectionList = ArrayList<EventsSection.EventSectionModel>();

//        sectionList.add(EventsSection.EventSectionModel("8:00", getEvents()))
//        sectionList.add(EventsSection.EventSectionModel("8:00", getEvents()))
//        sectionList.add(EventsSection.EventSectionModel("8:00", getEvents()))
//        sectionList.add(EventsSection.EventSectionModel("8:00", getEvents()))
//        sectionList.add(EventsSection.EventSectionModel("", getEventsList()))

        return sectionList

    }

    private fun getEvents(): ArrayList<Events> {
        val eventsList = ArrayList<Events>()
//        eventsList.add(Events("Test Events Something Cool is Happening over Here", "This is description 1. There's stuff in this building. Check it out fam", 1501997324, 1, true, false))
//        eventsList.add(Events("WOW cool neato wow that's so cool wait what how", "This is description 2. So many memes, so many dreams. Dank memes, dank dreams. Alliteration", 1501997324, 1, true, false))
//        eventsList.add(Events("There are lots of people here in the place with all the people in the place where there are so many people in the place containing many people", "This is description 3.", 1501997324, 1, true, false))
//        eventsList.add(Events("New Events", "This is description 4.", 1501997324, 1, true, false))

        return eventsList
    }


    private fun getEventsList(): ArrayList<Events> {
        val eventsList = ArrayList<Events>()
//        eventsList.add(Events("", "", 1501997324, 1, true, false))
        return eventsList
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


