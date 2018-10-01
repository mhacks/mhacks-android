package com.mhacks.app.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.room.EmptyResultSetException
import com.mhacks.app.data.models.Login
import com.mhacks.app.data.models.Result
import com.mhacks.app.data.models.common.RetrofitException
import com.mhacks.app.data.models.common.TextMessage
import com.mhacks.app.ui.main.usecase.CheckAdminAuthUseCase
import com.mhacks.app.ui.main.usecase.CheckLoginAuthUseCase
import org.mhacks.mhacksui.R
import timber.log.Timber
import javax.inject.Inject

class MainViewModel @Inject constructor(
        private val checkLoginAuthUseCase: CheckLoginAuthUseCase,
        private val checkAdminAuthUseCase: CheckAdminAuthUseCase): ViewModel() {

    private val checkLoginResult = checkLoginAuthUseCase.observe()

    private val checkAdminResult = checkAdminAuthUseCase.observe()

    private val _login = MediatorLiveData<Login>()

    val login: LiveData<Login?>
        get() = _login

    private val _snackBarMessage = MediatorLiveData<TextMessage>()

    val textMessage: MediatorLiveData<TextMessage>
        get() = _snackBarMessage

    private val _isAdmin = MediatorLiveData<Boolean>()

    val isAdmin: LiveData<Boolean>
        get() = _isAdmin

    init {
        _login.addSource(checkLoginResult) {
            if (it is Result.Success) {
                Timber.d("Login Success")
                _login.value = it.data
                checkIfAdmin()
            } else if (it is Result.Error<*>) {
                Timber.d("Login Failure")
                if (it.exception is EmptyResultSetException) {
                    Timber.d("Going to the SignInActivity")
                    _login.value = null
                }

                (it.exception as? RetrofitException)?.let { retrofitException ->

                    when (retrofitException.kind) {
                        RetrofitException.Kind.HTTP -> {
                            retrofitException.errorResponse?.let { errorResponse ->
                                _snackBarMessage.value =
                                                TextMessage(
                                                        null,
                                                        errorResponse.message)
                            }
                        }
                        RetrofitException.Kind.NETWORK -> {
                            _snackBarMessage.value =
                                            TextMessage(
                                                    R.string.no_internet,
                                                    null)
                        }
                        RetrofitException.Kind.UNEXPECTED -> {
                            _snackBarMessage.value =
                                            TextMessage(
                                                    R.string.unknown_error,
                                                    null)
                        }
                        RetrofitException.Kind.UNAUTHORIZED -> {
                            _snackBarMessage.value =
                                    TextMessage(
                                            R.string.unauthorized_error,
                                            null)
                        }
                    }
                }
            }
        }

        _isAdmin.addSource(checkAdminResult) {
            if (it is Result.Success) {
                _isAdmin.value = it.data
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        checkLoginAuthUseCase.onCleared()
        checkAdminAuthUseCase.onCleared()
    }

    fun checkIfLoggedIn() {
        checkLoginAuthUseCase.execute(Unit)
    }

    private fun checkIfAdmin() {
        checkAdminAuthUseCase.execute(Unit)
    }
}