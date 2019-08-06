package org.mhacks.app.events.usecase

import kotlinx.coroutines.withContext
import org.mhacks.app.Result
import org.mhacks.app.UseCase
import org.mhacks.app.core.CoroutinesDispatcherProvider
import org.mhacks.app.data.models.Event
import org.mhacks.app.events.EventRepository
import javax.inject.Inject

class GetEventsUseCase @Inject constructor(
        private val eventsRepository: EventRepository,
        private val dispatcherProvider: CoroutinesDispatcherProvider
): UseCase<Unit, List<Event>>() {

    override suspend fun execute(parameters: Unit): Result<List<Event>> {
        withContext(dispatcherProvider.io) {
            val eventResponse = eventsRepository.getEvent()
            return@withContext eventResponse.events
        }
    }
}