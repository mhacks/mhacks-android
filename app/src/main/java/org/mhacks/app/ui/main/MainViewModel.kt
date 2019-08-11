package org.mhacks.app.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.room.EmptyResultSetException
import org.mhacks.app.R
import org.mhacks.app.core.domain.auth.data.model.Auth
import org.mhacks.app.core.domain.auth.usecase.GetAuthUseCase
import org.mhacks.app.core.domain.auth.usecase.IsUserAdminUseCase
import org.mhacks.app.core.data.model.Outcome
import org.mhacks.app.core.data.model.RetrofitException
import org.mhacks.app.core.data.model.Text
import org.mhacks.app.core.data.model.Text.*
import timber.log.Timber
import javax.inject.Inject

class MainViewModel @Inject constructor(
        private val getAuthUseCase: GetAuthUseCase,
        private val checkAdminAuthUseCase: IsUserAdminUseCase) : ViewModel() {

    private val checkLoginResult = getAuthUseCase.observe()

    private val checkAdminResult = checkAdminAuthUseCase.observe()

    private val _login = MediatorLiveData<Auth>()

    val auth: LiveData<Auth?>
        get() = _login

    private val _text = MediatorLiveData<Text>()

    val text: MediatorLiveData<Text>
        get() = _text

    private val _isAdmin = MediatorLiveData<Boolean>()

    val isAdmin: LiveData<Boolean>
        get() = _isAdmin

    init {
        _login.addSource(checkLoginResult) {
            if (it is Outcome.Success) {
                Timber.d("Auth Success")
                _login.value = it.data
                checkIfAdmin()
            } else if (it is Outcome.Error<*>) {
                Timber.d("Auth Failure")
                if (it.exception is EmptyResultSetException) {
                    Timber.d("Going to the SignInActivity")
                    _login.value = null
                }

                (it.exception as? RetrofitException)?.let { retrofitException ->

                    when (retrofitException.kind) {
                        RetrofitException.Kind.HTTP -> {
                            retrofitException.errorResponse?.let { errorResponse ->
                                _text.value = TextString(errorResponse.message)
                            }
                        }
                        RetrofitException.Kind.NETWORK -> {
                            _text.value = Res(R.string.no_internet)
                        }
                        RetrofitException.Kind.UNEXPECTED -> {
                            _text.value = Res(R.string.unknown_error)
                        }
                        RetrofitException.Kind.UNAUTHORIZED -> {
                            _text.value = Res(R.string.unauthorized_error)
                        }
                    }
                }
            }
        }

        _isAdmin.addSource(checkAdminResult) {
            if (it is Outcome.Success) {
                _isAdmin.value = it.data
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        getAuthUseCase.onCleared()
        checkAdminAuthUseCase.onCleared()
    }

    fun checkIfLoggedIn() {
        getAuthUseCase(Unit)
    }

    private fun checkIfAdmin() {
        checkAdminAuthUseCase(Unit)
    }
}