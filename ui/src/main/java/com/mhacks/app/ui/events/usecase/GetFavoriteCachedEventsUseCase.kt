package com.mhacks.app.ui.events.usecase

import com.mhacks.app.data.models.Event
import com.mhacks.app.data.repository.EventsRepository
import com.mhacks.app.mvvm.SingleUseCase
import javax.inject.Inject

class GetFavoriteCachedEventsUseCase @Inject constructor(
        private val eventsRepository: EventsRepository)
    : SingleUseCase<Unit, List<Event>>() {

    override fun getSingle(parameters: Unit) =
            eventsRepository
                    .getEventCache()
                    .map {
                        it.filter { events ->
                            events.favorited
                        }
                    }!!

}