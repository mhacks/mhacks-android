package org.mhacks.app.announcements.widget

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.mhacks.app.announcements.R

/**
 * Created by jeffreychang on 9/15/17.
 */
class AnnouncementViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val announcementsHeader: TextView = itemView.findViewById(R.id.announcements_viewholder_header)

    val announcementDate: TextView = itemView.findViewById(R.id.announcements_viewholder_date)

    val announcementEventDetail: TextView = itemView.findViewById(R.id.announcements_viewholder_event_details)

}