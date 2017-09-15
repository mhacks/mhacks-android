package com.mhacks.android.ui.events

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.github.vipulasri.timelineview.LineType
import com.github.vipulasri.timelineview.TimelineView
import com.mhacks.android.data.model.Events
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection
import org.mhacks.android.R


/**
 * Created by jeffreychang on 8/31/17.
 */



 class EventsSection(val time: String,
                     val type: TimeLineType,
                     val events: ArrayList<Events>): StatelessSection(
        SectionParameters.Builder(R.layout.events_list_item)
        .headerResourceId(R.layout.events_header_item)
        .build()) {

     override fun getContentItemsTotal(): Int {
         return events.size
     }

    override fun getHeaderViewHolder(view: View): RecyclerView.ViewHolder {
        val holder = AnnouncementSectionHeaderViewHolder(view)

        when (type) {
            TimeLineType.START -> {
                holder.timelineView.initLine(LineType.BEGIN)
            }
            TimeLineType.NORMAL -> {
                holder.timelineView.initLine(LineType.ONLYONE)
            }
            TimeLineType.END -> {
                holder.timelineView.initLine(LineType.END)
            }
        }
        return holder

    }

    override fun onBindHeaderViewHolder(holder: RecyclerView.ViewHolder?) {
        val vh = holder as AnnouncementSectionHeaderViewHolder
        vh.announcementsTimeTextView.text = time
    }

     override fun getItemViewHolder(view: View): RecyclerView.ViewHolder {
         val holder = AnnouncementSectionItemViewHolder(view)
         if (type == TimeLineType.END) holder.hide()
         return holder
     }

    override fun onBindItemViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder = holder as AnnouncementSectionItemViewHolder
        viewHolder.announcementItemTitle.text = events[position].title
        viewHolder.descriptionItemTitle.text = events[position].info
     }

    class AnnouncementSectionHeaderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val timelineView = view.findViewById<View>(R.id.announcements_info_time_marker) as TimelineView
        val announcementsTimeTextView = view.findViewById<View>(R.id.announcements_time_text) as TextView

    }

    class AnnouncementSectionItemViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val announcementItemTitle = view.findViewById<View>(R.id.announcements_item_title) as TextView
        val descriptionItemTitle = view.findViewById<View>(R.id.announcements_item_description) as TextView

        fun hide() {
            view.visibility = View.GONE
        }
    }

    data class AnnouncementSectionModel(val time: String, val events: ArrayList<Events>)

    enum class TimeLineType {
        START, NORMAL, END
    }

}


