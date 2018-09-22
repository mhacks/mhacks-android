package com.mhacks.app.ui.ticket.usecase

import com.mhacks.app.data.models.User
import com.mhacks.app.data.repository.UserRepository
import com.mhacks.app.di.module.AuthModule
import com.mhacks.app.mvvm.SingleUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class GetAndCacheUserUseCase @Inject constructor(
        private val userRepository: UserRepository,
        private val authInterceptor: AuthModule.AuthInterceptor): SingleUseCase<Unit, User>() {

    override fun getSingle(parameters: Unit) = userRepository.getUserCache()
            .delay(500, TimeUnit.MILLISECONDS)
            .onErrorResumeNext {
                userRepository.getLoginCache()
                        .flatMap { login ->
                            authInterceptor.token = login.token
                            userRepository.getUserRemote()
                        }
            }
            .doOnSuccess {
                userRepository.updateUserCache(it)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe()
            }!!


}