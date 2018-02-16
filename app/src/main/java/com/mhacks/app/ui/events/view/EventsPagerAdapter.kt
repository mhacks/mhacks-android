package com.mhacks.app.ui.events.view

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

/**
 * Created by jeffreychang on 9/22/17.
 */
class EventsPagerAdapter(fm: FragmentManager, listWithDays: Map<String,
        List<EventsFragment.EventWithDay>>) : FragmentPagerAdapter(fm) {

    val list = listWithDays.toList()


    override fun getItem(position: Int): Fragment {
        val days = list[position]
        list.get(0).second
        return EventPageFragment.newInstance(days.first, days.second)
    }

    override fun getCount(): Int {
        return list.size
    }

    override fun getPageTitle(position: Int): CharSequence {
        return list[position].first
    }
}
