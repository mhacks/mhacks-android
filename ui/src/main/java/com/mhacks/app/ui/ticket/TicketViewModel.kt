package com.mhacks.app.ui.ticket

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.mhacks.app.data.models.Result
import com.mhacks.app.data.models.User
import com.mhacks.app.data.models.common.RetrofitException
import com.mhacks.app.data.models.common.TextMessage
import com.mhacks.app.ui.ticket.usecase.GetAndCacheUserUseCase
import org.mhacks.mhacksui.R
import timber.log.Timber
import javax.inject.Inject

class TicketViewModel @Inject constructor(
        private val getAndCacheUserUseCase: GetAndCacheUserUseCase): ViewModel() {

    private val getAndCacheConfigResult = getAndCacheUserUseCase.observe()

    private val _user = MediatorLiveData<User>()

    val user: LiveData<User> = _user

    private val _snackBarMessage = MediatorLiveData<TextMessage>()

    val snackBarMessage: LiveData<TextMessage>
        get() = _snackBarMessage

    private val _error: MutableLiveData<RetrofitException.Kind> = MutableLiveData()

    val error: LiveData<RetrofitException.Kind>
        get() = _error


    init {
        _user.addSource(getAndCacheConfigResult) {
            if (it is Result.Success) {
                Timber.d("Ticket Success")
                _user.value = it.data
            } else if (it is Result.Error<*>) {
                Timber.d("Ticket Failure")
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
                            _error.value = RetrofitException.Kind.NETWORK

                        }
                        RetrofitException.Kind.UNEXPECTED -> {
                            _snackBarMessage.value =
                                    TextMessage(
                                            R.string.unknown_error,
                                            null)
                        }
                        RetrofitException.Kind.UNAUTHORIZED -> {
                            Timber.d("User not signed in from Ticket Dialog")
                            _error.value = RetrofitException.Kind.UNAUTHORIZED
                        }
                    }
                }
            }
        }
    }

    fun getAndCacheUser() {
        getAndCacheUserUseCase.execute(Unit)
    }

    override fun onCleared() {
        super.onCleared()
        getAndCacheUserUseCase.onCleared()
    }

}