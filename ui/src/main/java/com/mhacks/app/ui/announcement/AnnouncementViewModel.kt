package com.mhacks.app.ui.announcement

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.mhacks.app.data.models.Announcement
import com.mhacks.app.data.models.Result
import com.mhacks.app.data.models.common.TextMessage
import com.mhacks.app.ui.announcement.usecase.GetAndCacheAnnouncementSingleUseCase
import com.mhacks.app.ui.welcome.usecase.PollAnnouncementsUseCase
import org.mhacks.mhacksui.R
import timber.log.Timber
import javax.inject.Inject

class AnnouncementViewModel @Inject constructor(
        private val getAndCacheAnnouncementUseCase: GetAndCacheAnnouncementSingleUseCase,
        private val pollAnnouncementsUseCase: PollAnnouncementsUseCase): ViewModel() {

    private val getAnnouncementResult = getAndCacheAnnouncementUseCase.observe()

    private val pollAnnouncementResult = pollAnnouncementsUseCase.observe()

    private val _announcements = MediatorLiveData<List<Announcement>>()

    val announcements: LiveData<List<Announcement>> = _announcements

    private val _snackBarMessage = MediatorLiveData<TextMessage>()

    val snackbarMessage: LiveData<TextMessage>
        get() = _snackBarMessage

    private val _error: MutableLiveData<Result.Error.Kind> = MutableLiveData()

    val error: LiveData<Result.Error.Kind>
        get() = _error

    init {
        _announcements.addSource(getAnnouncementResult) {
            if (it is Result.Success) {
                Timber.d("Announcements Got from Cache")
                _announcements.value = it.data
                pollAnnouncementsUseCase.execute(Unit)

            } else if (it is Result.Error<*>) {
                when (it.kind) {
                    Result.Error.Kind.NETWORK-> {
                        _error.value = it.kind
                    } else -> {
                        _snackBarMessage.value =
                                TextMessage(R.string.unknown_error, null)
                    }
                }
            }
        }

        _announcements.addSource(pollAnnouncementResult) {
            if (it is Result.Success) {
                _announcements.value = it.data
            } else if (it is Result.Error<*>) {
                when (it.kind) {
                    Result.Error.Kind.NETWORK-> {
                        _snackBarMessage.value =
                                TextMessage(R.string.announcement_network_failure, null)
                    } else -> {
                        _snackBarMessage.value = TextMessage(R.string.unknown_error, null)
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