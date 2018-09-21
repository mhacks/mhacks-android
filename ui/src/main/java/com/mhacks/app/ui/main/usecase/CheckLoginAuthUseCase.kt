package com.mhacks.app.ui.main.usecase

import com.mhacks.app.data.models.Login
import com.mhacks.app.data.repository.UserRepository
import com.mhacks.app.mvvm.SingleUseCase
import javax.inject.Inject

class CheckLoginAuthUseCase @Inject constructor(
        private val userRepository: UserRepository)
    : SingleUseCase<Unit, Login>() {

    override fun getSingle(parameters: Unit) =
            userRepository.getLoginCache()

}