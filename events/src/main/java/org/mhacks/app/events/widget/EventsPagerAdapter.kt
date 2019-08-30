package org.mhacks.app.events.widget

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import org.mhacks.app.eventlibrary.data.model.Event
import org.mhacks.app.eventlibrary.EventWithDay

/**
 * Pager adapter for events.
 */
class EventsPagerAdapter(
        fm: FragmentManager,
        listWithDays: Map<
                String,
                List<EventWithDay>>,
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
