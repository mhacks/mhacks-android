package com.mhacks.app.ui.events.view

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.mhacks.app.data.kotlin.Event
import com.github.vipulasri.timelineview.TimelineView
import org.threeten.bp.Instant
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter


class EventsRecyclerViewAdapter(val context: Context,
                                val events: ArrayList<EventSectionModel>)
    : RecyclerView.Adapter<EventsRecyclerViewAdapter.EventViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): EventViewHolder {
        val viewHolder = EventViewHolder(EventTimeLineItem(context))
        viewHolder.eventTimeLineItem.timeLineType = viewType
        return viewHolder
    }

    override fun getItemCount() = events.size

    override fun onBindViewHolder(holder: EventViewHolder?, position: Int) {
        holder?.eventTimeLineItem?.addEventGroup(events[position])
        val localTime =
                Instant.ofEpochMilli(events[position].time)
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime()
        val formatter = DateTimeFormatter.ofPattern("h:mm a")

        holder?.eventTimeLineItem?.timeText = formatter.format(localTime)
    }

    override fun getItemViewType(position: Int): Int {
        return TimelineView.getTimeLineViewType(position, itemCount)
    }

    class EventViewHolder(val eventTimeLineItem: EventTimeLineItem)
        : RecyclerView.ViewHolder(eventTimeLineItem)

    data class EventSectionModel(val time: Long, val events: ArrayList<Event>)
}