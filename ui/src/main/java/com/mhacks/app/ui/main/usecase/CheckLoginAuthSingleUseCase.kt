package com.mhacks.app.ui.main.usecase

import com.mhacks.app.data.models.Login
import com.mhacks.app.data.repository.UserRepository
import com.mhacks.app.di.SingleUseCase
import javax.inject.Inject

class CheckLoginAuthSingleUseCase @Inject constructor(
        private val userRepository: UserRepository)
    : SingleUseCase<Unit, Login>() {

    override fun getSingle(parameters: Unit) =
            userRepository.getLoginCache()

}