package org.mhacks.app.signin.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import org.mhacks.app.signin.usecase.SkipLoginUseCase
import org.mhacks.app.core.data.model.Outcome
import org.mhacks.app.core.data.model.RetrofitException
import org.mhacks.app.core.data.model.Text
import org.mhacks.app.core.domain.auth.data.model.Auth
import org.mhacks.app.signin.usecase.AuthRequest
import org.mhacks.app.signin.usecase.PostLoginUseCase
import org.mhacks.app.core.R as coreR
import timber.log.Timber
import javax.inject.Inject

class SignInViewModel @Inject constructor(
        private val postAuthUseCase: PostLoginUseCase,
        private val skipAuthUseCase: SkipLoginUseCase): ViewModel() {

    private val authRequest = AuthRequest("", "")

    private val postAuthResult = postAuthUseCase.observe()

    private val skipAuthResult = skipAuthUseCase.observe()

    private val _auth = MediatorLiveData<Auth>()

    val auth
        get() =_auth

    private val _snackBarMessage = MediatorLiveData<Pair<Text, AuthRequest>>()

    val snackBarMessage: LiveData<Pair<Text, AuthRequest>>
        get() = _snackBarMessage

    init {
        _auth.addSource(postAuthResult, ::updateAuth)
        _auth.addSource(skipAuthResult, ::updateAuth)
    }

    fun postAuth(request: AuthRequest) {
        postAuthUseCase(request)
    }

    fun skipAuth() {
        skipAuthUseCase(Unit)
    }

    private fun updateAuth(result: Outcome<Auth>) {
        if (result is Outcome.Success) {
            result.let { auth ->
                Timber.d("Sign in Successful")
                _auth.value = auth.data
            }
        } else if (result is Outcome.Error<*>) {
            (result.exception as? RetrofitException)?.let { retrofitException ->
                when (retrofitException.kind) {
                    RetrofitException.Kind.HTTP -> {
                        retrofitException.errorResponse?.let { errorResponse ->
                            _snackBarMessage.value =
                                    Pair(
                                            Text.TextString(errorResponse.message),
                                            authRequest)
                        }
                    }
                    RetrofitException.Kind.NETWORK -> {
                        _snackBarMessage.value =
                                Pair(
                                        Text.Res(coreR.string.no_internet),
                                        authRequest)
                    }
                    RetrofitException.Kind.UNEXPECTED -> {
                        _snackBarMessage.value =
                                Pair(
                                        Text.Res(coreR.string.unknown_error), authRequest)
                    }
                    RetrofitException.Kind.UNAUTHORIZED -> {
                        // no-op
                    }
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        postAuthUseCase.onCleared()
    }

}
