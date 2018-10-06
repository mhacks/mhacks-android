package com.mhacks.app.ui.signin.usecase

import com.mhacks.app.data.models.Login
import com.mhacks.app.data.repository.UserRepository
import com.mhacks.app.mvvm.SingleUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class SkipLoginUseCase @Inject constructor(
        private val userRepository: UserRepository)
    : SingleUseCase<Unit, Login>() {

    override fun getSingle(parameters: Unit) =
            userRepository
                    .updateLoginCache(
                            Login(1, false, "", "")
                    )
                    .doOnSuccess {
                        it.id = 1
                        userRepository
                                .updateLoginCache(it)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe({ _ ->
                                    Timber.d("Login Success")
                                }, {
                                    Timber.d("Login Failure")
                                })
                    }!!
}