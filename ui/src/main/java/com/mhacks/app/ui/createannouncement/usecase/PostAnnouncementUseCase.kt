package com.mhacks.app.ui.createannouncement.usecase

import com.mhacks.app.data.models.CreateAnnouncement
import com.mhacks.app.data.repository.AnnouncementRepository
import com.mhacks.app.data.repository.UserRepository
import com.mhacks.app.di.module.AuthModule
import com.mhacks.app.mvvm.SingleUseCase
import io.reactivex.Single
import timber.log.Timber
import javax.inject.Inject

class PostAnnouncementUseCase @Inject constructor(
        private val announcementRepository: AnnouncementRepository,
        private val userRepository: UserRepository,
        private val authInterceptor: AuthModule.AuthInterceptor)
    : SingleUseCase<CreateAnnouncement, CreateAnnouncement>() {

    override fun getSingle(parameters: CreateAnnouncement): Single<CreateAnnouncement> {
        return userRepository
                .getLoginCache()
                .flatMap {
                    Timber.d("User is admin: %s", it.isAdmin)

                    it.isAdmin?.let{ isAdmin ->
                        if (!isAdmin) {
                            Timber.d("User does not belong to admin")
                        }
                    }
                    authInterceptor.token = it.token
                    announcementRepository.postAnnouncement(parameters)
                }
    }
}