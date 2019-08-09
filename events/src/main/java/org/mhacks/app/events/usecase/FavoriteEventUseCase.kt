package org.mhacks.app.events.usecase

import io.reactivex.Single
import org.mhacks.app.data.model.Event
import org.mhacks.app.events.EventRepository
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