package com.mhacks.app.ui.announcement.usecase

import com.mhacks.app.data.models.Announcement
import com.mhacks.app.data.repository.AnnouncementRepository
import com.mhacks.app.mvvm.SingleUseCase
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class GetAndCacheAnnouncementUseCase @Inject constructor(
        private val announcementRepository: AnnouncementRepository)
    : SingleUseCase<Unit, List<Announcement>>() {

    override fun getSingle(parameters: Unit) =
            announcementRepository
                    .getAnnouncementLocal()
                    .delay(400, TimeUnit.MILLISECONDS)
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