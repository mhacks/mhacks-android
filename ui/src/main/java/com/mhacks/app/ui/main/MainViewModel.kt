package com.mhacks.app.ui.main

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.ViewModel
import android.arch.persistence.room.EmptyResultSetException
import com.mhacks.app.data.models.Login
import com.mhacks.app.data.models.Result
import com.mhacks.app.data.models.common.SnackbarMessage
import com.mhacks.app.ui.main.usecase.CheckAdminAuthUseCase
import com.mhacks.app.ui.main.usecase.CheckLoginAuthUseCase
import org.mhacks.mhacksui.R
import javax.inject.Inject

class MainViewModel @Inject constructor(
        private val checkLoginAuthUseCase: CheckLoginAuthUseCase,
        private val checkAdminAuthUseCase: CheckAdminAuthUseCase): ViewModel() {

    private val checkLoginResult: MediatorLiveData<Result<Login>> = checkLoginAuthUseCase.observe()

    private val checkAdminResult: MediatorLiveData<Result<Boolean>> = checkAdminAuthUseCase.observe()

    private val _login = MediatorLiveData<Login>()

    val login: LiveData<Login>
        get() = _login

    private val _loginFailed = MediatorLiveData<Unit>()

    val loginFailed: LiveData<Unit>
        get() = _loginFailed

    private val _snackBarMessage = MediatorLiveData<SnackbarMessage>()

    val snackBarMessage: MediatorLiveData<SnackbarMessage>
        get() = _snackBarMessage

    private val _isAdmin = MediatorLiveData<Boolean>()

    val isAdmin: LiveData<Boolean>
        get() = _isAdmin

    init {
        _login.addSource(checkLoginResult) {
            if (it is Result.Success) {
                _login.value = it.data
                checkIfAdmin()
            } else if (it is Result.Error<*>) {
                if (it.exception is EmptyResultSetException) {
                    _loginFailed.value = Unit
                } else {
                    _snackBarMessage.value =
                            SnackbarMessage(R.string.unknown_error, null)
                }
            }
        }

        _isAdmin.addSource(checkAdminResult) {
            if (it is Result.Success) {
                _isAdmin.value = it.data

            }
        }
    }
    fun checkIfLoggedIn() {
        checkLoginAuthUseCase.execute(Unit)
    }

    private fun checkIfAdmin() {
        checkAdminAuthUseCase.execute(Unit)
    }
}