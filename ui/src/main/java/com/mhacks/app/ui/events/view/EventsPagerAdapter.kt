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
        val days = list[position]
        list[0].second
        return EventPageFragment.newInstance(days.first, days.second)
    }

    override fun getCount() = list.size

    override fun getPageTitle(position: Int) = list[position].first
}
