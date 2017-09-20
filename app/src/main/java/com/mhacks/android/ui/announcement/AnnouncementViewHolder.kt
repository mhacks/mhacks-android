package com.mhacks.android.ui.announcement

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import org.mhacks.android.R

/**
 * Created by jeffreychang on 9/15/17.
 */
class AnnouncementViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val announcementsHeader: TextView = itemView.findViewById<TextView>(R.id.announcements_viewholder_header)

    val announcementDate: TextView = itemView.findViewById<TextView>(R.id.announcements_viewholder_date)

    val announcementEventDetail: TextView = itemView.findViewById<TextView>(R.id.announcements_viewholder_event_details)

}