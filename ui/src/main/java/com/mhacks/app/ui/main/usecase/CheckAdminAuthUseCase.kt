package com.mhacks.app.ui.main.usecase

import com.mhacks.app.data.repository.UserRepository
import com.mhacks.app.mvvm.SingleUseCase
import io.reactivex.Single
import timber.log.Timber
import javax.inject.Inject

class CheckAdminAuthUseCase @Inject constructor(
        private val userRepository: UserRepository)
    : SingleUseCase<Unit, Boolean>() {

    override fun getSingle(parameters: Unit): Single<Boolean> =
            userRepository
                    .getLoginCache()
                    .map {
                        Timber.d("User is admin: %s", it.isAdmin)
                        it.isAdmin
                    }
}