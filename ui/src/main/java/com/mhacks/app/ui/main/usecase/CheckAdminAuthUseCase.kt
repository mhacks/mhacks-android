package com.mhacks.app.ui.main.usecase

import com.mhacks.app.data.repository.UserRepository
import com.mhacks.app.di.UseCase
import io.reactivex.Single
import javax.inject.Inject

class CheckAdminAuthUseCase @Inject constructor(
        private val userRepository: UserRepository)
    : UseCase<Unit, Boolean>() {

    override fun getSingle(parameters: Unit): Single<Boolean> =
            userRepository.getIsAdmin()

}