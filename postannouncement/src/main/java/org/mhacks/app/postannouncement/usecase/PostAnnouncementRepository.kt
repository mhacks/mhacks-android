package org.mhacks.app.postannouncement.usecase

import org.mhacks.app.postannouncement.data.model.PostAnnouncement
import javax.inject.Inject

class PostAnnouncementRepository @Inject constructor(
        private val announcementService: PostAnnouncementService
) {

    fun postAnnouncement(postAnnouncement: PostAnnouncement) =
            announcementService.postAnnouncement(
                    postAnnouncement.title,
                    postAnnouncement.category,
                    postAnnouncement.body,
                    isApproved = true, isSent = true, push = true)
}
