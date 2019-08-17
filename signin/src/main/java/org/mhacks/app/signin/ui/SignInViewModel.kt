package org.mhacks.app.signin.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.mhacks.app.core.data.model.Outcome
import org.mhacks.app.core.data.model.RetrofitException
import org.mhacks.app.core.data.model.Text
import org.mhacks.app.core.domain.auth.data.model.Auth
import org.mhacks.app.signin.R
import org.mhacks.app.signin.usecase.AuthRequest
import org.mhacks.app.signin.usecase.PostLoginUseCase
import org.mhacks.app.signin.usecase.SkipLoginUseCase
import timber.log.Timber
import javax.inject.Inject
import org.mhacks.app.core.R as coreR

class SignInViewModel @Inject constructor(
        private val postAuthUseCase: PostLoginUseCase,
        private val skipAuthUseCase: SkipLoginUseCase) : ViewModel() {

    private var authRequest = AuthRequest()

    private val postAuthResult = postAuthUseCase.observe()

    private val skipAuthResult = skipAuthUseCase.observe()

    private val _auth = MediatorLiveData<Auth>()

    val auth
        get() = _auth

    private val _loading = MutableLiveData<Unit>()
    val loading: LiveData<Unit> get() = _loading

    private val _message = MediatorLiveData<Pair<Text, AuthRequest?>>()

    val loginFail: LiveData<Pair<Text, AuthRequest?>>
        get() = _message

    init {
        _auth.addSource(postAuthResult, ::updateAuth)
        _auth.addSource(skipAuthResult, ::updateAuth)
    }

    fun postAuth(request: AuthRequest) {
        authRequest = request
        _loading.postValue(Unit)
        postAuthUseCase(request)
    }

    fun skipAuth() {
        skipAuthUseCase(Unit)
    }

    private fun updateAuth(result: Outcome<Auth>) {
        when (result) {
            is Outcome.Success -> {
                Timber.d("Sign in Successful")
                _auth.value = result.data
            }
            is Outcome.Error<*> -> {
                (result.exception as? RetrofitException)?.let { retrofitException ->
                    when (retrofitException.kind) {
                        RetrofitException.Kind.HTTP -> {
                            retrofitException.errorResponse?.let { errorResponse ->
                                _message.value =
                                        Pair(
                                                Text.TextString(errorResponse.message),
                                                authRequest)
                            }
                        }
                        RetrofitException.Kind.NETWORK -> {
                            _message.value =
                                    Pair(
                                            Text.Res(coreR.string.no_internet),
                                            authRequest)
                        }
                        RetrofitException.Kind.UNEXPECTED -> {
                            _message.value =
                                    Pair(
                                            Text.Res(coreR.string.unknown_error), authRequest)
                        }
                        RetrofitException.Kind.UNAUTHORIZED -> {
                            _message.value = Pair(Text.Res(R.string.auth_error), null)
                        }
                    }
                }
            }
            is Outcome.Loading -> {

            }
        }
        authRequest = AuthRequest()
    }

    override fun onCleared() {
        super.onCleared()
        postAuthUseCase.onCleared()
    }

}
