package com.mhacks.app.ui.main

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.ViewModel
import android.arch.persistence.room.EmptyResultSetException
import com.mhacks.app.data.models.Login
import com.mhacks.app.data.models.Result
import com.mhacks.app.data.models.common.TextMessage
import com.mhacks.app.ui.main.usecase.CheckAdminAuthSingleUseCase
import com.mhacks.app.ui.main.usecase.CheckLoginAuthSingleUseCase
import org.mhacks.mhacksui.R
import timber.log.Timber
import javax.inject.Inject

class MainViewModel @Inject constructor(
        private val checkLoginAuthUseCase: CheckLoginAuthSingleUseCase,
        private val checkAdminAuthUseCase: CheckAdminAuthSingleUseCase): ViewModel() {

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
                Timber.e("LOGIN SUCCESS")
                _login.value = it.data
                checkIfAdmin()
            } else if (it is Result.Error<*>) {
                if (it.exception is EmptyResultSetException) {
                    _login.value = null
                } else {
                    _snackBarMessage.value =
                            TextMessage(R.string.unknown_error, null)
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