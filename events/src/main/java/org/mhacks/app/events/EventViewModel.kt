package org.mhacks.app.events

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.mhacks.app.data.models.Event
import org.mhacks.app.data.models.Outcome
import org.mhacks.app.data.models.common.RetrofitException
import org.mhacks.app.data.models.common.TextMessage
import org.mhacks.app.event.R
import org.mhacks.app.events.usecase.FavoriteEventUseCase
import org.mhacks.app.events.usecase.GetAndCacheEventsUseCase
import org.mhacks.app.events.usecase.GetFavoriteCachedEventsUseCase
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Named

data class EventWithDay(
        val day: String,
        val event: Event)

class EventViewModel @Inject constructor(
        private val getAndCacheEventsUseCase: GetAndCacheEventsUseCase,
        private val favoriteEventUseCase: FavoriteEventUseCase,
        private val getFavoriteCachedEventsUseCase: GetFavoriteCachedEventsUseCase
) : ViewModel() {

    private val getAndCacheEventResult = getAndCacheEventsUseCase.observe()
    private val favoriteEventResult = favoriteEventUseCase.observe()
    private val getFavoriteCachedEventsResult = getFavoriteCachedEventsUseCase.observe()

    private val _events = MediatorLiveData<Map<String, List<EventWithDay>>>()
    val events get() = _events

    private val _favoriteEvent = MediatorLiveData<Event>()
    val favoriteEvent get() = _favoriteEvent

    private val _favoriteEvents = MediatorLiveData<Map<String, List<EventWithDay>>>()
    val favoriteEvents get() = _favoriteEvents

    private val _snackBarMessage = MediatorLiveData<TextMessage>()
    val snackbarMessage: LiveData<TextMessage> get() = _snackBarMessage

    private val _error: MutableLiveData<RetrofitException.Kind> = MutableLiveData()
    val error: LiveData<RetrofitException.Kind> get() = _error

    init {
        _events.addSource(getAndCacheEventResult) {
            if (it is Outcome.Success) {
                it.let { resultList ->
                    _events.value = mapToEventWithDay(resultList.data)
                }
            } else if (it is Outcome.Error<*>) {
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
                                            R.string.unknown_error,
                                            null)
                        }
                    }
                }
            }
        }

        _favoriteEvent.addSource(favoriteEventResult) {
            if (it is Outcome.Success) {
                it.let { result ->
                    _favoriteEvent.value = result.data
                }
            }
        }

        _favoriteEvents.addSource(getFavoriteCachedEventsResult) {
            if (it is Outcome.Success) {
                it.let { result ->
                    _favoriteEvents.value = mapToEventWithDay(result.data)
                }
            }
        }
    }

    private fun mapToEventWithDay(eventList: List<Event>): Map<String, List<EventWithDay>> =
            eventList.asSequence()
                    .map { events ->
                        EventWithDay(
                                weekDateFormat.format(Date(events.startDateTs)),
                                events)
                    }
                    .groupBy { it.day }

    fun getAndCacheEvents() {
        getAndCacheEventsUseCase(Unit)
    }

    fun insertFavoriteEvent(event: Event) {
        favoriteEventUseCase.execute(event)
    }

    fun getFavoriteEvents() {
        getFavoriteCachedEventsUseCase.execute(Unit)
    }

    override fun onCleared() {
        super.onCleared()
        getAndCacheEventsUseCase.onCleared()
        favoriteEventUseCase.onCleared()
        getFavoriteCachedEventsUseCase.onCleared()
    }

    companion object {

        @SuppressLint("ConstantLocale")
        val weekDateFormat = SimpleDateFormat("EEEE", Locale.getDefault())
    }
}