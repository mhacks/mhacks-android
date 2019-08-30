package org.mhacks.app.eventlibrary.usecase

import io.reactivex.Single
import org.mhacks.app.eventlibrary.data.model.Event
import org.mhacks.app.eventlibrary.EventRepository
import org.mhacks.app.core.usecase.SingleUseCase
import javax.inject.Inject

class FavoriteEventUseCase @Inject constructor(
        private val eventRepository: EventRepository
) : SingleUseCase<Event, Event>() {

    override fun getSingle(parameters: Event) =
            Single.fromCallable {
                eventRepository.updateEvent(parameters)
                return@fromCallable parameters
            }
}