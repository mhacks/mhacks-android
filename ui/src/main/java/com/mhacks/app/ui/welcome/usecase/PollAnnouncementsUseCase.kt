package com.mhacks.app.ui.welcome.usecase

import com.mhacks.app.data.models.Announcement
import com.mhacks.app.data.repository.AnnouncementRepository
import com.mhacks.app.mvvm.ObservableUseCase
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class PollAnnouncementsUseCase @Inject constructor(
        private val announcementRepository: AnnouncementRepository)
    : ObservableUseCase<Unit, List<Announcement>>() {

    override fun getObservable(parameters: Unit) =
            Observable.interval(4, TimeUnit.SECONDS, Schedulers.io())
                    .flatMap {
                        announcementRepository.getAnnouncementRemote()
                                .doOnError { throwable ->
                                    Timber.e("""Announcement Polling Error $throwable""")
                                }
                                .retry(5)
                                .doOnSuccess { response ->
                                    announcementRepository.putAnnouncementLocal(
                                            response.announcements)
                                }
                                .map { response ->
                                    Timber.d("""Announcement Polling: ${response.announcements.size} events""")
                                    response.announcements
                                }.toObservable()
                    }!!

}