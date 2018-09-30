package com.mhacks.app.ui.events.view

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.mhacks.app.data.models.Event
import com.mhacks.app.ui.events.EventsViewModel

/**
 * Pager adapter for events.
 */
class EventsPagerAdapter(
        fm: FragmentManager,
        listWithDays: Map<
                String,
                List<EventsViewModel.EventWithDay>>,
        private val eventsCallback: (event: Event, isChecked: Boolean) -> Unit) : FragmentPagerAdapter(fm) {

    val list = listWithDays.toList()

    override fun getItem(position: Int): Fragment {
        val (days, events) = list[position]
        val eventPageFragment = EventPageFragment.newInstance(days, events)

        eventPageFragment.onEventsClicked = eventsCallback
        return eventPageFragment
    }

    override fun getCount() = list.size

    override fun getPageTitle(position: Int) = list[position].first
}
