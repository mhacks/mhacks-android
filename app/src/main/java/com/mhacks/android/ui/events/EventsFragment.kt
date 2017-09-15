package com.mhacks.android.ui.events

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.mhacks.android.data.model.Events
import com.mhacks.android.ui.common.BaseFragment
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter
import kotlinx.android.synthetic.main.fragment_events.*
import org.mhacks.android.R
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


    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // TODO cancel active requests
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initList()

        getEvents()
    }

    // Set up the test listView for displaying events
    private fun initList() {
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        announcements_recycler_view.setHasFixedSize(true)

        announcements_recycler_view.layoutManager = LinearLayoutManager(activity)

        // Create and set the adapter for this recyclerView
        val list = getAnnouncementSectionModelList()
        listAdapter = sectionedEventsAdapter(list)

        announcements_recycler_view.adapter = listAdapter
    }

    private fun getAnnouncementSectionModelList(): ArrayList<EventsSection.AnnouncementSectionModel> {

        val sectionList = ArrayList<EventsSection.AnnouncementSectionModel>();

        sectionList.add(EventsSection.AnnouncementSectionModel("8:00", getEvents()))
        sectionList.add(EventsSection.AnnouncementSectionModel("8:00", getEvents()))
        sectionList.add(EventsSection.AnnouncementSectionModel("8:00", getEvents()))
        sectionList.add(EventsSection.AnnouncementSectionModel("8:00", getEvents()))
        sectionList.add(EventsSection.AnnouncementSectionModel("", getEventsList()))

        return sectionList

    }

    private fun getEvents(): ArrayList<Events> {
        val eventsList = ArrayList<Events>()
        eventsList.add(Events("Test Events Something Cool is Happening over Here", "This is description 1. There's stuff in this building. Check it out fam", 1501997324, 1, true, false))
        eventsList.add(Events("WOW cool neato wow that's so cool wait what how", "This is description 2. So many memes, so many dreams. Dank memes, dank dreams. Alliteration", 1501997324, 1, true, false))
        eventsList.add(Events("There are lots of people here in the place with all the people in the place where there are so many people in the place containing many people", "This is description 3.", 1501997324, 1, true, false))
        eventsList.add(Events("New Events", "This is description 4.", 1501997324, 1, true, false))

        return eventsList
//        networkManager.getEvents(object : HackathonCallback<List<Events>> {
//            override fun success(response: List<Events>) {
//                announcementsList = ArrayList<Events>()
//                for (announcement in response) {
//                    val currentTime = Calendar.getInstance()
//                    val announcementTime = Calendar.getInstance()
//                    announcementTime.time = Date(announcement.broadcastAt)
//                    if (currentTime.compareTo(announcementTime) != -1) announcementsList.add(announcement)
//
//                    updateAnnouncements()
//                }
//            }
//
//            override fun failure(error: Throwable) {
//                Log.e(TAG, "Couldn't get events", error)
//            }
//        })
    }


    private fun getEventsList(): ArrayList<Events> {
        val eventsList = ArrayList<Events>()
        eventsList.add(Events("", "", 1501997324, 1, true, false))
        return eventsList
    }

    private fun updateAnnouncements() {
        // Notify the adapter that the data changed
        listAdapter.notifyDataSetChanged()
    }
    companion object {
        val instance: EventsFragment
            get() = EventsFragment()
    }
}


