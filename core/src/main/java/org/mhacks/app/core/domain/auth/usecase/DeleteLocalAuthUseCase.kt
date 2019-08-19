package org.mhacks.app.core.domain.auth.usecase

import org.mhacks.app.core.domain.auth.AuthRepository
import org.mhacks.app.core.usecase.SingleUseCase
import javax.inject.Inject

class DeleteLocalAuthUseCase @Inject constructor(
        private val authRepository: AuthRepository
) : SingleUseCase<Unit, Unit>() {

    override fun getSingle(parameters: Unit) =
            authRepository.deleteAuth()

}