package org.mhacks.app.ticket.usecase

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.mhacks.app.core.domain.user.UserRepository
import org.mhacks.app.core.domain.user.data.User
import org.mhacks.app.core.usecase.SingleUseCase
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class GetAndCacheUserUseCase @Inject constructor(
        private val userRepository: UserRepository) : SingleUseCase<Unit, User>() {

    override fun getSingle(parameters: Unit) = userRepository.getUserCache()
            .delay(500, TimeUnit.MILLISECONDS)
            .onErrorResumeNext {
                userRepository.getUserRemote()
            }
            .doOnSuccess {
                userRepository.updateUserCache(it)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe()
            }

}