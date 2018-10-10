package com.mhacks.app.ui.events.widget


import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
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