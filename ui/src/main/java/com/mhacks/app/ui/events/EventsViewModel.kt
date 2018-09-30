package com.mhacks.app.ui.events

import android.annotation.SuppressLint
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.mhacks.app.data.models.Event
import com.mhacks.app.data.models.Result
import com.mhacks.app.data.models.common.RetrofitException
import com.mhacks.app.data.models.common.TextMessage
import com.mhacks.app.ui.events.usecase.FavoriteEventUseCase
import com.mhacks.app.ui.events.usecase.GetAndCacheEventsUseCase
import org.mhacks.mhacksui.R
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class EventsViewModel @Inject constructor(
        private val getAndCacheEventsUseCase: GetAndCacheEventsUseCase,
        private val favoriteEventUseCase: FavoriteEventUseCase): ViewModel() {

    private val getAndCacheEventResult = getAndCacheEventsUseCase.observe()

    private val favoriteEventResult = favoriteEventUseCase.observe()

    private val _events = MediatorLiveData<
            Map<String, List<EventsViewModel.EventWithDay>>>()

    val events
        get() =_events

    private val _event = MediatorLiveData<Event>()

    val event
        get() =_event

    private val _snackBarMessage = MediatorLiveData<TextMessage>()

    val snackbarMessage: LiveData<TextMessage>
        get() = _snackBarMessage

    private val _error: MutableLiveData<RetrofitException.Kind> = MutableLiveData()

    val error: LiveData<RetrofitException.Kind>
        get() = _error

    init {
        _events.addSource(getAndCacheEventResult) {
            if (it is Result.Success) {
                it.let { resultList ->
                    _events.value = mapToEventWithDay(resultList.data)
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

        _event.addSource(favoriteEventResult) {
            if (it is Result.Success) {
                it.let { result ->
                    _event.value = result.data
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
        getAndCacheEventsUseCase.execute(Unit)
    }

    fun favoriteEvent(event: Event) {
        favoriteEventUseCase.execute(event)
    }

    override fun onCleared() {
        super.onCleared()
        getAndCacheEventsUseCase.onCleared()
        favoriteEventUseCase.onCleared()
    }

    data class EventWithDay(
            val day: String,
            val event: Event)


    companion object {

        @SuppressLint("ConstantLocale")
        val weekDateFormat = SimpleDateFormat("EEEE", Locale.getDefault())

    }
}