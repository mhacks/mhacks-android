package org.mhacks.app.qrscan.usecase

import io.reactivex.Single
import org.mhacks.app.core.domain.user.UserRepository
import org.mhacks.app.core.domain.user.data.Feedback
import org.mhacks.app.core.usecase.SingleUseCase
import javax.inject.Inject

class VerifyTicketUseCase @Inject constructor(
        private val userRepository: UserRepository)
    : SingleUseCase<String, List<Feedback>>() {

    override fun getSingle(parameters: String): Single<List<Feedback>> =
            userRepository
                    .getUserCache()
                    .flatMap {
                        userRepository.verifyUserTicket(parameters).map { response ->
                            response.feedback
                        }
                    }

}