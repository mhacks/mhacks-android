package com.mhacks.app.ui.createannouncement.usecase

import com.mhacks.app.data.models.CreateAnnouncement
import com.mhacks.app.data.repository.AnnouncementRepository
import com.mhacks.app.mvvm.SingleUseCase
import io.reactivex.Single

class PostAnnouncementUseCase(
        private val announcementRepository: AnnouncementRepository)
    : SingleUseCase<CreateAnnouncement, CreateAnnouncement>() {

    override fun getSingle(parameters: CreateAnnouncement): Single<CreateAnnouncement> {
        return announcementRepository.postAnnouncement(parameters)
    }
}