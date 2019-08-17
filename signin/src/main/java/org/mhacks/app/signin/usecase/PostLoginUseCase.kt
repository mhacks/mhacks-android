package org.mhacks.app.signin.usecase

import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.mhacks.app.core.domain.auth.AuthRepository
import org.mhacks.app.core.domain.auth.data.model.Auth
import org.mhacks.app.core.usecase.SingleUseCase
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

data class AuthRequest(
        val email: String = "",
        val password: String = ""
)

class PostLoginUseCase @Inject constructor(
        private val authRepository: AuthRepository)
    : SingleUseCase<AuthRequest, Auth>() {

    override fun getSingle(parameters: AuthRequest): Single<Auth> {
        val (email, password) = parameters
        return authRepository
                .postAuth(email, password)
                .doOnSuccess {
                    // Overwrites the current login stored in SQLite.
                    it.id = 1
                    authRepository.updateCachedAuth(it)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({
                                Timber.d("Login Success")
                            }, {
                                Timber.d("Login Failure")
                            })
                }
    }

}