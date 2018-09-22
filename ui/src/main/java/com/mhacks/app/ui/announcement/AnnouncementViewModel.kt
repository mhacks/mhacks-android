package com.mhacks.app.ui.announcement

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.mhacks.app.data.models.Announcement
import com.mhacks.app.data.models.Result
import com.mhacks.app.data.models.common.RetrofitException
import com.mhacks.app.data.models.common.TextMessage
import com.mhacks.app.ui.announcement.usecase.GetAndCacheAnnouncementUseCase
import com.mhacks.app.ui.welcome.usecase.PollAnnouncementsUseCase
import org.mhacks.mhacksui.R
import timber.log.Timber
import javax.inject.Inject

class AnnouncementViewModel @Inject constructor(
        private val getAndCacheAnnouncementUseCase: GetAndCacheAnnouncementUseCase,
        private val pollAnnouncementsUseCase: PollAnnouncementsUseCase): ViewModel() {

    private val getAnnouncementResult = getAndCacheAnnouncementUseCase.observe()

    private val pollAnnouncementResult = pollAnnouncementsUseCase.observe()

    private val _announcements = MediatorLiveData<List<Announcement>>()

    val announcements: LiveData<List<Announcement>> = _announcements

    private val _snackBarMessage = MediatorLiveData<TextMessage>()

    val snackbarMessage: LiveData<TextMessage>
        get() = _snackBarMessage

    private val _error: MutableLiveData<RetrofitException.Kind> = MutableLiveData()

    val error: LiveData<RetrofitException.Kind>
        get() = _error

    init {
        _announcements.addSource(getAnnouncementResult) {
            if (it is Result.Success) {
                Timber.d("Announcements Got from Cache")
                _announcements.value = it.data
                pollAnnouncementsUseCase.execute(Unit)

            } else if (it is Result.Error<*>) {
                Timber.d("Announcements Errpr")
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
                            _error.value = retrofitException.kind

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

        _announcements.addSource(pollAnnouncementResult) {
            if (it is Result.Success) {
                _announcements.value = it.data
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
                                            R.string.announcement_network_failure,
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

    fun getAndCacheAnnouncements() {
        getAndCacheAnnouncementUseCase.execute(Unit)
    }

    override fun onCleared() {
        super.onCleared()
        getAndCacheAnnouncementUseCase.onCleared()
    }

}