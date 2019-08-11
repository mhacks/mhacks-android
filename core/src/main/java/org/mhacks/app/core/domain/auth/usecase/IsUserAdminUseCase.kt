package org.mhacks.app.core.domain.auth.usecase

import io.reactivex.Single
import org.mhacks.app.core.usecase.SingleUseCase
import org.mhacks.app.core.domain.user.UserRepository
import timber.log.Timber
import javax.inject.Inject

class IsUserAdminUseCase @Inject constructor(
        private val userRepository: UserRepository
) : SingleUseCase<Unit, Boolean>() {

    override fun getSingle(parameters: Unit): Single<Boolean> =
            userRepository
                    .getLoginCache()
                    .map {
                        Timber.d("User is admin: %s", it.isAdmin)
                        it.isAdmin
                    }
}