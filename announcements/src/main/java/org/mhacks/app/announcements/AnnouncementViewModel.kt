package org.mhacks.app.announcements

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.mhacks.app.announcements.data.model.Announcement
import org.mhacks.app.core.data.model.Outcome
import org.mhacks.app.core.data.model.RetrofitException
import org.mhacks.app.core.data.model.Text
import org.mhacks.app.announcements.usecase.GetAndCacheAnnouncementUseCase
import org.mhacks.app.announcements.usecase.PollAnnouncementsUseCase
import org.mhacks.app.core.R as coreR

import timber.log.Timber
import javax.inject.Inject

class AnnouncementViewModel @Inject constructor(
        private val getAndCacheAnnouncementUseCase: GetAndCacheAnnouncementUseCase,
        private val pollAnnouncementsUseCase: PollAnnouncementsUseCase): ViewModel() {

    private val getAnnouncementResult = getAndCacheAnnouncementUseCase.observe()

    private val pollAnnouncementResult = pollAnnouncementsUseCase.observe()

    private val _announcements = MediatorLiveData<List<Announcement>>()

    val announcements: LiveData<List<Announcement>> = _announcements

    private val _snackBarMessage = MediatorLiveData<Text>()

    val snackbarMessage: LiveData<Text>
        get() = _snackBarMessage

    private val _error: MutableLiveData<RetrofitException.Kind> = MutableLiveData()

    val error: LiveData<RetrofitException.Kind>
        get() = _error

    init {
        _announcements.addSource(getAnnouncementResult) {
            if (it is Outcome.Success) {
                Timber.d("Announcements Got from Cache")
                _announcements.value = it.data
                pollAnnouncementsUseCase.execute(Unit)

            } else if (it is Outcome.Error<*>) {
                Timber.d("Announcements Errpr")
                (it.exception as? RetrofitException)?.let { retrofitException ->
                    when (retrofitException.kind) {
                        RetrofitException.Kind.HTTP -> {
                            retrofitException.errorResponse?.let { errorResponse ->
                                _snackBarMessage.value =
                                        Text.TextString(errorResponse.message)
                            }
                        }
                        RetrofitException.Kind.NETWORK -> {
                            _error.value = retrofitException.kind

                        }
                        RetrofitException.Kind.UNEXPECTED -> {
                            _snackBarMessage.value =
                                    Text.Res(coreR.string.unknown_error)
                        }
                        RetrofitException.Kind.UNAUTHORIZED -> {
                            _snackBarMessage.value =
                                    Text.Res(coreR.string.unauthorized_error)
                        }
                    }
                }
            }
        }

        _announcements.addSource(pollAnnouncementResult) {
            if (it is Outcome.Success) {
                _announcements.value = it.data
            } else if (it is Outcome.Error<*>) {
                (it.exception as? RetrofitException)?.let { retrofitException ->
                    when (retrofitException.kind) {
                        RetrofitException.Kind.HTTP -> {
                            retrofitException.errorResponse?.let { errorResponse ->
                                _snackBarMessage.value =
                                        Text.TextString(errorResponse.message)
                            }
                        }
                        RetrofitException.Kind.NETWORK -> {
                            _snackBarMessage.value =
                                    Text.Res(R.string.announcement_network_failure)
                        }
                        RetrofitException.Kind.UNEXPECTED -> {
                            _snackBarMessage.value =
                                    Text.Res(coreR.string.unknown_error)
                        }
                        RetrofitException.Kind.UNAUTHORIZED -> {
                            _snackBarMessage.value =
                                    Text.Res(coreR.string.unauthorized_error)
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
         pollAnnouncementsUseCase.onCleared()
    }

}