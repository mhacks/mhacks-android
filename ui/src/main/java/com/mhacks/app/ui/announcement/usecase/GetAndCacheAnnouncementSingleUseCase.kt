package com.mhacks.app.ui.announcement.usecase

import com.mhacks.app.data.models.Announcement
import com.mhacks.app.data.repository.AnnouncementRepository
import com.mhacks.app.di.SingleUseCase
import javax.inject.Inject

class GetAndCacheAnnouncementSingleUseCase @Inject constructor(
        private val announcementRepository: AnnouncementRepository)
    : SingleUseCase<Unit, List<Announcement>>() {

    override fun getSingle(parameters: Unit) =
            announcementRepository
                    .getAnnouncementLocal()
                    .onErrorResumeNext {
                        announcementRepository.getAnnouncementRemote()
                                .map { configResponse ->
                                    configResponse.announcements
                                }
                    }
                    .doOnSuccess {
                        announcementRepository.putAnnouncementLocal(it)
                    }!!
}