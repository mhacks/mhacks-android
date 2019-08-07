package org.mhacks.app.events.usecase

import io.reactivex.Single
import org.mhacks.app.UseCase
import org.mhacks.app.data.models.Event
import org.mhacks.app.events.EventRepository
import javax.inject.Inject

class GetEventsUseCase @Inject constructor(
        private val eventsRepository: EventRepository
) : UseCase<Unit, Single<List<Event>>>() {

    override fun execute(parameters: Unit) =
            eventsRepository
                    .getEvent()
                    .map { it.events }

}