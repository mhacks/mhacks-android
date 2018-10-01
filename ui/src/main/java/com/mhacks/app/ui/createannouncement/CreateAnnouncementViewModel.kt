package com.mhacks.app.ui.createannouncement

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
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

    private val _toastMessage = MediatorLiveData<TextMessage>()

    val toastMessage: LiveData<TextMessage>
        get() = _toastMessage

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
                                _toastMessage.value =
                                        TextMessage(
                                                null,
                                                errorResponse.message)
                            }
                        }
                        RetrofitException.Kind.NETWORK -> {
                            _toastMessage.value =
                                    TextMessage(
                                            R.string.unknown_error,
                                            null)
                        }
                        RetrofitException.Kind.UNEXPECTED -> {
                            _toastMessage.value =
                                    TextMessage(
                                            R.string.unknown_error,
                                            null)
                        }
                        RetrofitException.Kind.UNAUTHORIZED -> {
                            _toastMessage.value =
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