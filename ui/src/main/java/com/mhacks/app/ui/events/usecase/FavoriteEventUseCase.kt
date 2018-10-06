package com.mhacks.app.ui.events.usecase

import com.mhacks.app.data.models.Event
import com.mhacks.app.data.repository.EventsRepository
import com.mhacks.app.mvvm.SingleUseCase
import io.reactivex.Single
import javax.inject.Inject

class FavoriteEventUseCase @Inject constructor(
        private val eventsRepository: EventsRepository)
    : SingleUseCase<Event, Event>() {

    override fun getSingle(parameters: Event) =
            Single.fromCallable {
                eventsRepository.updateEvent(parameters)
                return@fromCallable parameters
            }!!
}