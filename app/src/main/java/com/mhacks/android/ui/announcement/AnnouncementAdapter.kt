package com.mhacks.android.ui.announcement

import android.content.Context
import android.support.v7.widget.RecyclerView.Adapter
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import com.mhacks.android.data.kotlin.Announcements
import org.mhacks.x.R
import java.util.*

/**
 * Created by jeffreychang on 9/15/17.
 */
class AnnouncementsAdapter(val context: Context,
                          private val announcementList: ArrayList<Announcements>):
        Adapter<AnnouncementViewHolder>() {

    override fun getItemCount(): Int {
        return announcementList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): AnnouncementViewHolder {
        val view = LayoutInflater.from(context)
                .inflate(R.layout.announcements_viewholder_item, parent, false)
        return AnnouncementViewHolder(view)
    }

    override fun onBindViewHolder(holder: AnnouncementViewHolder, position: Int) {
        bindItems(holder, announcementList[position])

    }
    private fun bindItems(holder: AnnouncementViewHolder, announcement: Announcements) {

        holder.announcementsHeader.text = announcement.title

        val relativeTime = DateUtils.getRelativeTimeSpanString(
                announcement.createdAtTs,
                Date().time, DateUtils.MINUTE_IN_MILLIS,
                DateUtils.FORMAT_NO_NOON)


        holder.announcementDate.text = relativeTime
        holder.announcementEventDetail.text = announcement.body

    }

}