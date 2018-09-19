package com.mhacks.app.ui.signin

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.mhacks.app.data.models.Login
import com.mhacks.app.data.models.Result
import com.mhacks.app.data.models.common.TextMessage
import com.mhacks.app.ui.signin.usecase.PostLoginUseCase
import org.mhacks.mhacksui.R
import javax.inject.Inject

class SignInViewModel @Inject constructor(
        private val postLoginUseCase: PostLoginUseCase): ViewModel() {

    private val postLoginResult = postLoginUseCase.observe()

    private val _login = MediatorLiveData<Login>()

    val login
        get() =_login

    private val _snackBarMessage = MediatorLiveData<TextMessage>()

    val snackbarMessage: LiveData<TextMessage>
        get() = _snackBarMessage

    private val _error: MutableLiveData<Result.Error.Kind> = MutableLiveData()

    val error: LiveData<Result.Error.Kind>
        get() = _error

    init {
        _login.addSource(postLoginResult) {
            if (it is Result.Success) {
                it.let { resultList ->
                    //                    _login.value = mapToEventWithDay(resultList.data)
                }
            } else if (it is Result.Error<*>) {
                when (it.kind) {
                    Result.Error.Kind.NETWORK-> {
                        _error.value = it.kind
                    }
                    else -> {
                        _snackBarMessage.value =
                                TextMessage(R.string.unknown_error, null)
                    }
                }
            }
        }
    }

    fun postLogin(request: Login.Request) {
        postLoginUseCase.execute(request)
    }

    override fun onCleared() {
        super.onCleared()
        postLoginUseCase.onCleared()
    }
}