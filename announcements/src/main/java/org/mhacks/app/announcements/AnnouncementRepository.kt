package org.mhacks.app.announcements

import org.mhacks.app.announcements.data.model.Announcement

import org.mhacks.app.announcements.data.db.AnnouncementDao
import org.mhacks.app.announcements.data.service.AnnouncementService

import io.reactivex.Single
import javax.inject.Inject

class AnnouncementRepository @Inject constructor(
        private val announcementService: AnnouncementService,
        private val announcementDao: AnnouncementDao) {

    fun getAnnouncementRemote() =
            announcementService.getAnnouncementResponse()

    fun getAnnouncementCache() = announcementDao.getAnnouncements()

    fun putAnnouncementCache(announcementList: List<Announcement>) =
            Single.fromCallable {
                announcementDao.updateAnnouncements(announcementList)
                return@fromCallable announcementList
            }
}