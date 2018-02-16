package com.mhacks.app.ui.events.view

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.github.vipulasri.timelineview.LineType
import com.github.vipulasri.timelineview.TimelineView
import com.mhacks.app.R
import com.mhacks.app.data.kotlin.Event
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection

/**
 * Created by jeffreychang on 8/31/17.
 */

class EventsSection(val time: String,
                    val type: TimeLineType,
                    val events: ArrayList<Event>): StatelessSection(
        SectionParameters.Builder(R.layout.events_list_item)
        .headerResourceId(R.layout.events_header_item)
        .build()) {

    override fun getContentItemsTotal(): Int {
        return events.size
    }

    override fun getHeaderViewHolder(view: View): RecyclerView.ViewHolder {
        val holder = eventSectionHeaderViewHolder(view)

        when (type) {
            TimeLineType.START -> holder.timelineView.initLine(LineType.BEGIN)

            TimeLineType.NORMAL -> holder.timelineView.initLine(LineType.NORMAL)

            TimeLineType.END -> holder.timelineView.initLine(LineType.END)
        }

        return holder
    }

    override fun onBindHeaderViewHolder(holder: RecyclerView.ViewHolder?) {
        val vh = holder as eventSectionHeaderViewHolder
        vh.eventsTimeTextView.text = time
    }

    override fun getItemViewHolder(view: View): RecyclerView.ViewHolder {
        val holder = eventSectionItemViewHolder(view)
        if (type == TimeLineType.END) holder.hide()
     return holder
    }

    override fun onBindItemViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder = holder as eventSectionItemViewHolder
        viewHolder.announcementItemTitle.text = events[position].name
        viewHolder.descriptionItemTitle.text = events[position].desc
     }

    class eventSectionHeaderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val timelineView = view.findViewById<View>(R.id.events_info_time_marker) as TimelineView
        val eventsTimeTextView = view.findViewById<View>(R.id.events_time_text) as TextView

    }

    class eventSectionItemViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val announcementItemTitle = view.findViewById<View>(R.id.events_item_title) as TextView
        val descriptionItemTitle = view.findViewById<View>(R.id.events_item_description) as TextView

        fun hide() {
            view.visibility = View.GONE
        }
    }

    data class EventSectionModel(val time: Long, val events: ArrayList<Event>)

    enum class TimeLineType {
        START, NORMAL, END
    }

}


