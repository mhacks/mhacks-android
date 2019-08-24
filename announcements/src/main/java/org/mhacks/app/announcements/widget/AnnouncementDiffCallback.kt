package org.mhacks.app.announcements.widget

import androidx.recyclerview.widget.DiffUtil
import org.mhacks.app.announcements.data.model.Announcement

class AnnouncementDiffCallback: DiffUtil.ItemCallback<Announcement>() {

    override fun areItemsTheSame(
            oldAnnouncement: Announcement,
            newAnnouncement: Announcement) =
            oldAnnouncement.id == newAnnouncement.id

    override fun areContentsTheSame(oldAnnouncement: Announcement,
                                    newAnnouncement: Announcement) =
            oldAnnouncement == newAnnouncement

}