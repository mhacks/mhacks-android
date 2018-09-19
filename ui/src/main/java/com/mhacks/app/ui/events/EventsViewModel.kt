package com.mhacks.app.ui.events

import android.annotation.SuppressLint
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.mhacks.app.data.models.Event
import com.mhacks.app.data.models.Result
import com.mhacks.app.data.models.common.TextMessage
import com.mhacks.app.ui.events.usecase.GetAndCacheEventsUseCase
import com.mhacks.app.ui.events.view.EventsFragment
import com.mhacks.app.ui.events.view.EventsPagerAdapter
import kotlinx.android.synthetic.main.fragment_events.*
import org.mhacks.mhacksui.R
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class EventsViewModel @Inject constructor(
        private val getAndCacheEventsUseCase: GetAndCacheEventsUseCase): ViewModel() {

    private val getAndCacheEventResult = getAndCacheEventsUseCase.observe()

    private val _events = MediatorLiveData<
            Map<String, List<EventsViewModel.EventWithDay>>>()

    val events
        get() =_events

    private val _snackBarMessage = MediatorLiveData<TextMessage>()

    val snackbarMessage: LiveData<TextMessage>
        get() = _snackBarMessage

    private val _error: MutableLiveData<Result.Error.Kind> = MutableLiveData()

    val error: LiveData<Result.Error.Kind>
        get() = _error

    init {
        _events.addSource(getAndCacheEventResult) {
            if (it is Result.Success) {
                it.let { resultList ->
                    _events.value = mapToEventWithDay(resultList.data)
                }
            } else if (it is Result.Error<*>) {
                when (it.kind) {
                    Result.Error.Kind.NETWORK-> {
                        _error.value = it.kind
                    }
                    else -> {
                        _snackBarMessage.value =
                                TextMessage(R.string.unknown_error, null)
                    }
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

    override fun onCleared() {
        super.onCleared()
        getAndCacheEventsUseCase.onCleared()
    }

    data class EventWithDay(
            val day: String,
            val event: Event)


    companion object {

        @SuppressLint("ConstantLocale")
        val weekDateFormat = SimpleDateFormat("EEEE", Locale.getDefault())

    }
}