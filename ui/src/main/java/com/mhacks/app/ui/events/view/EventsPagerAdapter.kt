package com.mhacks.app.ui.events.view

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.mhacks.app.ui.events.EventsViewModel

/**
 * Pager adapter for events.
 */
class EventsPagerAdapter(fm: FragmentManager, listWithDays: Map<String,
        List<EventsViewModel.EventWithDay>>) : FragmentPagerAdapter(fm) {

    val list = listWithDays.toList()

    override fun getItem(position: Int): Fragment {
        val (days, events) = list[position]
        return EventPageFragment.newInstance(days, events)
    }

    override fun getCount() = list.size

    override fun getPageTitle(position: Int) = list[position].first
}
