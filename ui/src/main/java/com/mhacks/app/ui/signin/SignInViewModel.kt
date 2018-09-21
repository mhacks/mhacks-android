package com.mhacks.app.ui.signin

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.ViewModel
import com.mhacks.app.data.models.Login
import com.mhacks.app.data.models.Result
import com.mhacks.app.data.models.common.RetrofitException
import com.mhacks.app.data.models.common.TextMessage
import com.mhacks.app.ui.signin.usecase.PostLoginUseCase
import com.mhacks.app.ui.signin.usecase.SkipLoginUseCase
import org.mhacks.mhacksui.R
import timber.log.Timber
import javax.inject.Inject

class SignInViewModel @Inject constructor(
        private val postLoginUseCase: PostLoginUseCase,
        private val skipLoginUseCase: SkipLoginUseCase): ViewModel() {

    private val loginRequest = Login.Request("", "")

    private val postLoginResult = postLoginUseCase.observe()

    private val skipLoginResult = skipLoginUseCase.observe()

    private val _login = MediatorLiveData<Login>()

    val login
        get() =_login

    private val _snackBarMessage = MediatorLiveData<Pair<TextMessage, Login.Request>>()

    val snackBarMessage: LiveData<Pair<TextMessage, Login.Request>>
        get() = _snackBarMessage

    init {
        _login.addSource(postLoginResult, ::updateLogin)
        _login.addSource(skipLoginResult, ::updateLogin)
    }

    fun postLogin(request: Login.Request) {
        postLoginUseCase.execute(request)
    }

    fun skipLogin() {
        skipLoginUseCase.execute(Unit)
    }

    private fun updateLogin(result: Result<Login>?) {
        if (result is Result.Success) {
            result.let { login ->
                Timber.d("Sign in Successful")
                _login.value = login.data
            }
        } else if (result is Result.Error<*>) {
            (result.exception as? RetrofitException)?.let { retrofitException ->

                when (retrofitException.kind) {
                    RetrofitException.Kind.HTTP -> {
                        retrofitException.errorResponse?.let { errorResponse ->
                            _snackBarMessage.value =
                                    Pair(
                                            TextMessage(
                                                    null,
                                                    errorResponse.message),
                                            loginRequest)
                        }
                    }
                    RetrofitException.Kind.NETWORK -> {
                        _snackBarMessage.value =
                                Pair(
                                        TextMessage(
                                                R.string.no_internet,
                                                null),
                                        loginRequest)
                    }
                    RetrofitException.Kind.UNEXPECTED -> {
                        _snackBarMessage.value =
                                Pair(
                                        TextMessage(
                                                R.string.unknown_error,
                                                null),
                                        loginRequest)
                    }
                }
            }
        }
    }


    override fun onCleared() {
        super.onCleared()
        postLoginUseCase.onCleared()
    }

}
