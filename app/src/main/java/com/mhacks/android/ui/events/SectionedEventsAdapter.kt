package com.mhacks.android.ui.events

import com.mhacks.android.ui.events.EventsSection.TimeLineType
import com.mhacks.android.ui.events.EventsSection.EventSectionModel
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter

/**
 * Created by jeffreychang on 9/1/17.
 */
open class SectionedEventsAdapter: SectionedRecyclerViewAdapter() {
    private val eventsSectionList: ArrayList<EventSectionModel> = ArrayList()

    open fun addAllEventsSections(events: ArrayList<EventSectionModel>) {
        eventsSectionList.clear()
        eventsSectionList.addAll(events)

        notifyDataSetChanged()
        for (it in 0 until eventsSectionList.size) {

            val type: TimeLineType = when (it) {

                0 -> TimeLineType.START

                eventsSectionList.size - 1 -> TimeLineType.END

                else -> TimeLineType.NORMAL
            }
            addSection(EventsSection(
                    time = eventsSectionList[it].time.toString(),
                    type = type,
                    events = eventsSectionList[it].events))
        }
    }
}
