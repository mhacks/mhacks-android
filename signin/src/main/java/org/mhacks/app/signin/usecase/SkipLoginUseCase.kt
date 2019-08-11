package org.mhacks.app.signin.usecase

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.mhacks.app.core.domain.auth.AuthRepository
import org.mhacks.app.core.domain.auth.data.model.Auth
import org.mhacks.app.core.domain.user.UserRepository
import org.mhacks.app.core.usecase.SingleUseCase
import timber.log.Timber
import javax.inject.Inject

class SkipLoginUseCase @Inject constructor(
        private val authRepository: AuthRepository)
    : SingleUseCase<Unit, Auth>() {

    override fun getSingle(parameters: Unit) =
            authRepository
                    .updateAuthCache(Auth.empty())
                    .doOnSuccess {
                        it.id = 1
                        authRepository
                                .updateAuthCache(it)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe({
                                    Timber.d("Login Success")
                                }, {
                                    Timber.e("Login Failure")
                                })
                    }
}