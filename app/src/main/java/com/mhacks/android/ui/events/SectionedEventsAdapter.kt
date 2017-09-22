package com.mhacks.android.ui.events

import com.mhacks.android.ui.events.EventsSection.TimeLineType
import com.mhacks.android.ui.events.EventsSection.EventSectionModel
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by jeffreychang on 9/1/17.
 */
open class SectionedEventsAdapter: SectionedRecyclerViewAdapter() {
    private val eventsSectionList: ArrayList<EventSectionModel> = ArrayList()
    private val dateFormat: SimpleDateFormat = SimpleDateFormat("hh:mm", Locale.US)

    open fun addAllEventsSections(events: ArrayList<EventSectionModel>) {
        eventsSectionList.clear()
        eventsSectionList.addAll(events)

        notifyDataSetChanged()
        for (it in 0 until eventsSectionList.size) {

            val type: TimeLineType = when (it) {

                0 -> TimeLineType.START

                eventsSectionList.size -> TimeLineType.END

                else -> TimeLineType.NORMAL
            }
            val eventTime = Date(eventsSectionList[it].time)

            addSection(EventsSection(
                    time = dateFormat.format(eventTime),
                    type = type,
                    events = eventsSectionList[it].events))
        }
        addSection(EventsSection("", TimeLineType.END, ArrayList()))
    }
}
