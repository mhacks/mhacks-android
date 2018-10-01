package com.mhacks.app.ui.announcement.view

import androidx.recyclerview.widget.DiffUtil
import com.mhacks.app.data.models.Announcement

class AnnouncementDiffCallback: DiffUtil.ItemCallback<Announcement>() {

    override fun areItemsTheSame(
            oldAnnouncement: Announcement,
            newAnnouncement: Announcement) =
            oldAnnouncement.id == newAnnouncement.id

    override fun areContentsTheSame(oldAnnouncement: Announcement,
                                    newAnnouncement: Announcement) =
            oldAnnouncement == newAnnouncement

}