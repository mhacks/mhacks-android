package com.mhacks.app.ui.signin.usecase

import com.mhacks.app.data.models.Login
import com.mhacks.app.data.repository.UserRepository
import com.mhacks.app.mvvm.SingleUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class PostLoginUseCase @Inject constructor(
        private val userRepository: UserRepository)
    : SingleUseCase<Login.Request, Login>() {

    override fun getSingle(parameters: Login.Request) = userRepository
                .postLogin(parameters)
                .doOnSuccess {
                    it.id = 1
                    userRepository.updateLoginCache(it)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({
                                Timber.d("Login Success")
                            }, {
                                Timber.d("Login Failure")
                            })
                }!!
}