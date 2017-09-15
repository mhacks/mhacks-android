package com.mhacks.android.ui.events

import com.mhacks.android.ui.events.EventsSection.TimeLineType
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter

/**
 * Created by jeffreychang on 9/1/17.
 */
class sectionedEventsAdapter(eventsSectionList: ArrayList<EventsSection.AnnouncementSectionModel>)
    : SectionedRecyclerViewAdapter() {
    init {
        for (it in 0 until eventsSectionList.size) {

            val type: TimeLineType = when (it) {
                0 -> {
                    TimeLineType.START
                }
                eventsSectionList.size - 1 -> {
                    TimeLineType.END
                }
                else -> {
                    TimeLineType.NORMAL
                }
            }

            addSection(EventsSection(
                    time = eventsSectionList[it].time,
                    type = type,
                    events = eventsSectionList[it].events))
        }
    }
}
