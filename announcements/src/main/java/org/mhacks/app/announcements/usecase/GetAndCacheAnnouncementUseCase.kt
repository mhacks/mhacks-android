package org.mhacks.app.announcements.usecase

import org.mhacks.app.announcements.data.model.Announcement
import org.mhacks.app.announcements.AnnouncementRepository
import org.mhacks.app.core.usecase.SingleUseCase
import io.reactivex.Single
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class GetAndCacheAnnouncementUseCase @Inject constructor(
        private val announcementRepository: AnnouncementRepository)
    : SingleUseCase<Unit, List<Announcement>>() {

    override fun getSingle(parameters: Unit) =
            announcementRepository
                    .getAnnouncementCache()
                    .delay(400, TimeUnit.MILLISECONDS)
                    .flatMap {
                        if (it.isEmpty()) {
                            announcementRepository.getAnnouncementRemote()
                                    .map { configResponse ->
                                        configResponse.announcements
                                    }
                        }
                        else {
                            Single.just(it)
                        }
                    }
                    .doOnSuccess {
                        announcementRepository.putAnnouncementCache(it)
                    }
}