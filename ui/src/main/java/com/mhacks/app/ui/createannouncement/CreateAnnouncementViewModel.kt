package com.mhacks.app.ui.createannouncement

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.mhacks.app.data.models.CreateAnnouncement
import com.mhacks.app.data.models.Result
import com.mhacks.app.data.models.common.RetrofitException
import com.mhacks.app.data.models.common.TextMessage
import com.mhacks.app.ui.createannouncement.usecase.PostAnnouncementUseCase
import org.mhacks.mhacksui.R
import javax.inject.Inject

class CreateAnnouncementViewModel @Inject constructor(
        private val postAnnouncementUseCase: PostAnnouncementUseCase): ViewModel() {

    private val postAnnouncementResult = postAnnouncementUseCase.observe()

    private val _createAnnouncement = MediatorLiveData<CreateAnnouncement>()

    val createAnnouncement
        get() = _createAnnouncement

    private val _snackBarMessage = MediatorLiveData<TextMessage>()

    val snackBarMessage: LiveData<TextMessage>
        get() = _snackBarMessage

    private val _error: MutableLiveData<RetrofitException.Kind> = MutableLiveData()

    val error: LiveData<RetrofitException.Kind>
        get() = _error

    init {
        _createAnnouncement.addSource(postAnnouncementResult) {
            if (it is Result.Success) {
                it.let { createAnnouncement ->
                    _createAnnouncement.value = createAnnouncement.data
                }
            } else if (it is Result.Error<*>) {
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
                                            R.string.unknown_error,
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
    }

    fun postAnnouncement(createAnnouncement: CreateAnnouncement) {
        postAnnouncementUseCase.execute(createAnnouncement)
    }

    override fun onCleared() {
        super.onCleared()
        postAnnouncementUseCase.onCleared()
    }

}