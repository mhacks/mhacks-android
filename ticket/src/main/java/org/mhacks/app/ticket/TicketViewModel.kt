package org.mhacks.app.ticket

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.mhacks.app.core.data.model.Outcome
import org.mhacks.app.core.data.model.RetrofitException
import org.mhacks.app.core.data.model.Text
import org.mhacks.app.core.domain.user.data.User
import org.mhacks.app.ticket.usecase.GetAndCacheUserUseCase
import timber.log.Timber
import javax.inject.Inject
import org.mhacks.app.core.R as coreR

class TicketViewModel @Inject constructor(
        private val getAndCacheUserUseCase: GetAndCacheUserUseCase
) : ViewModel() {

    private val getAndCacheUserOutcome = getAndCacheUserUseCase.observe()

    private val _user = MediatorLiveData<User>()
    val user: LiveData<User> = _user

    private val _snackBarMessage = MediatorLiveData<Text>()

    val snackBarMessage: LiveData<Text>
        get() = _snackBarMessage

    private val _error: MutableLiveData<RetrofitException.Kind> = MutableLiveData()

    val error: LiveData<RetrofitException.Kind>
        get() = _error

    init {
        _user.addSource(getAndCacheUserOutcome) {
            if (it is Outcome.Success) {
                Timber.d("Ticket Success")
                _user.value = it.data
            } else if (it is Outcome.Error<*>) {
                Timber.d("Ticket Failure")
                (it.exception as? RetrofitException)?.let { retrofitException ->
                    when (retrofitException.kind) {
                        RetrofitException.Kind.HTTP -> {
                            retrofitException.errorResponse?.let { errorResponse ->
                                _snackBarMessage.value = Text.TextString(errorResponse.message)
                            }
                        }
                        RetrofitException.Kind.NETWORK -> {
                            _error.value = RetrofitException.Kind.NETWORK

                        }
                        RetrofitException.Kind.UNEXPECTED -> {
                            _snackBarMessage.value =
                                    Text.Res(coreR.string.unknown_error)
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