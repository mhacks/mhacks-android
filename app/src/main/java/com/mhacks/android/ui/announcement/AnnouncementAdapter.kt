package com.mhacks.android.ui.announcement

import android.content.Context
import android.support.v7.widget.RecyclerView.Adapter
import android.view.LayoutInflater
import android.view.ViewGroup
import com.mhacks.android.data.model.Announcement
import org.mhacks.android.R

/**
 * Created by jeffreychang on 9/15/17.
 */
class AnnouncementAdapter(val context: Context,
                          private val announcementList: ArrayList<Announcement>):
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
    fun bindItems(holder: AnnouncementViewHolder, announcement: Announcement) {

        holder.announcementsHeader.text = announcement.title
        holder.announcementDate.text = announcement.date
        holder.announcementEventDetail.text = announcement.description

    }

}