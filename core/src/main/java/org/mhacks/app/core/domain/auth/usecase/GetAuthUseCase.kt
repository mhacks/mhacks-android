package org.mhacks.app.core.domain.auth.usecase

import org.mhacks.app.core.domain.auth.data.model.Auth
import org.mhacks.app.core.domain.user.UserRepository
import org.mhacks.app.core.usecase.SingleUseCase
import javax.inject.Inject

class GetAuthUseCase @Inject constructor(
        private val userRepository: UserRepository
) : SingleUseCase<Unit, Auth>() {

    override fun getSingle(parameters: Unit) =
            userRepository.getLoginCache()

}