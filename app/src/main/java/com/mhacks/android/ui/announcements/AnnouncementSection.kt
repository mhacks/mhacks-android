package com.mhacks.android.ui.announcements

import android.graphics.Color
import android.support.v4.content.ContextCompat
import com.mhacks.android.data.model.Announcement
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.TextView
import com.github.vipulasri.timelineview.LineType
import com.github.vipulasri.timelineview.TimelineView
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection
import org.mhacks.android.R


/**
 * Created by jeffreychang on 8/31/17.
 */



 class AnnouncementSection(val time: String,
                           val type: TimeLineType,
                           val announcements: ArrayList<Announcement>): StatelessSection(
        SectionParameters.Builder(R.layout.announcement_list_item)
        .headerResourceId(R.layout.announcement_header_item)
        .build()) {

     override fun getContentItemsTotal(): Int {
         return announcements.size
     }


    override fun getHeaderViewHolder(view: View): RecyclerView.ViewHolder {
        val holder = AnnouncementSectionHeaderViewHolder(view)

        when (type) {
            TimeLineType.START -> {
                Log.d("JEFFREY", "BEGIN")
                holder.timelineView.initLine(LineType.BEGIN)
            }
            TimeLineType.NORMAL -> {
                Log.d("JEFFREY", "NORMAL")
                holder.timelineView.initLine(LineType.ONLYONE)
            }
            TimeLineType.END -> {
                Log.d("JEFFREY", "END")
                holder.timelineView.initLine(LineType.END)
            }
        }
        return holder

    }

    override fun onBindHeaderViewHolder(holder: RecyclerView.ViewHolder?) {
        val holder = holder as AnnouncementSectionHeaderViewHolder
        holder.announcementsTimeTextView.text = time
    }

     override fun getItemViewHolder(view: View): RecyclerView.ViewHolder {
         val holder = AnnouncementSectionItemViewHolder(view)
         if (type == TimeLineType.END) holder.hide()
         return holder
     }

    override fun onBindItemViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val view = holder as AnnouncementSectionItemViewHolder
        view.announcementItemTitle.text = announcements[position].title
        view.descriptionItemTitle.text = announcements[position].info
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

    data class AnnouncementSectionModel(val time: String, val announcements: ArrayList<Announcement>)

    enum class TimeLineType {
        START, NORMAL, END
    }

}


