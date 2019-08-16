package org.mhacks.app.postannouncement.usecase

import io.reactivex.Single
import org.mhacks.app.core.domain.user.UserRepository
import org.mhacks.app.core.usecase.SingleUseCase
import org.mhacks.app.data.model.PostAnnouncement
import timber.log.Timber
import javax.inject.Inject

class PostAnnouncementUseCase @Inject constructor(
        private val announcementRepository: PostAnnouncementRepository,
        private val userRepository: UserRepository)
    : SingleUseCase<PostAnnouncement, PostAnnouncement>() {

    override fun getSingle(parameters: PostAnnouncement): Single<PostAnnouncement> {
        return userRepository
                .getUserCache()
                .flatMap {
                    Timber.d("User is admin: %s", it.isAdmin)

                    it.isAdmin?.let{ isAdmin ->
                        if (!isAdmin) {
                            Timber.d("User does not belong to admin")
                        }
                    }
                    announcementRepository.postAnnouncement(parameters)
                }
    }
}