package com.mhacks.app.ui.main.usecase

import com.mhacks.app.data.repository.UserRepository
import com.mhacks.app.di.SingleUseCase
import io.reactivex.Single
import javax.inject.Inject

class CheckAdminAuthUseCase @Inject constructor(
        private val userRepository: UserRepository)
    : SingleUseCase<Unit, Boolean>() {

    override fun getSingle(parameters: Unit): Single<Boolean> =
            userRepository.getIsAdmin()

}