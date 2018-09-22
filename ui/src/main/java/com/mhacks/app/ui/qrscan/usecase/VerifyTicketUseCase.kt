package com.mhacks.app.ui.qrscan.usecase

import com.mhacks.app.data.models.Feedback
import com.mhacks.app.data.models.common.RetrofitException
import com.mhacks.app.data.repository.UserRepository
import com.mhacks.app.di.module.AuthModule
import com.mhacks.app.mvvm.SingleUseCase
import io.reactivex.Single
import timber.log.Timber
import javax.inject.Inject

class VerifyTicketUseCase @Inject constructor(
        private val userRepository: UserRepository,
        private val authInterceptor: AuthModule.AuthInterceptor)
    : SingleUseCase<String, List<Feedback>>() {

    override fun getSingle(parameters: String): Single<List<Feedback>> =
            userRepository
                    .getLoginCache()
                    .flatMap {
                        Timber.d("User is admin: %s", it.isAdmin)

                     it.isAdmin?.let{ isAdmin ->
                       if (!isAdmin) {
                           Timber.d("User does not belong to admin")
                       }
                     }
                        authInterceptor.token = it.token
                        userRepository.verifyUserTicket(parameters).map { response ->
                            response.feedback
                        }
                    }

}