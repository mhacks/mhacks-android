package com.mhacks.app.data.repository

import com.mhacks.app.data.models.Announcement
import com.mhacks.app.data.models.CreateAnnouncement
import com.mhacks.app.data.room.dao.AnnouncementDao
import com.mhacks.app.data.service.AnnouncementService
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class AnnouncementRepository @Inject constructor(
        private val announcementService: AnnouncementService,
        private val announcementDao: AnnouncementDao) {

    fun getAnnouncementRemote() =
            announcementService.getAnnouncementResponse()

    fun getAnnouncementLocal() = announcementDao.getAnnouncements()

    fun putAnnouncementLocal(announcementList: List<Announcement>) =
            Single.fromCallable {
                announcementDao.updateAnnouncements(announcementList)
                return@fromCallable announcementList
            }!!

    fun postAnnouncement(createAnnouncement: CreateAnnouncement) =
            announcementService.postAnnouncement(
                    createAnnouncement.title,
                    createAnnouncement.category,
                    createAnnouncement.body,
                    true, true, true)
}