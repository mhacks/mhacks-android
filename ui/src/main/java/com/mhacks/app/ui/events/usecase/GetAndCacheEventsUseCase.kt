package com.mhacks.app.ui.events.usecase

import com.mhacks.app.data.models.Event
import com.mhacks.app.data.repository.EventsRepository
import com.mhacks.app.mvvm.SingleUseCase
import io.reactivex.Single
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class GetAndCacheEventsUseCase @Inject constructor(
        private val eventsRepository: EventsRepository)
    : SingleUseCase<Unit, List<Event>>() {

    override fun getSingle(parameters: Unit) =
            eventsRepository
                    .getEventCache()
                    .delay(400, TimeUnit.MILLISECONDS)
                    .flatMap {
                        if (it.isEmpty())
                            eventsRepository.getEventRemote()
                                    .doOnSuccess { response ->
                                        eventsRepository.deleteAndUpdateEvents(response.events)
                                    }
                                    .map { response ->
                                        response.events
                                    }
                        else Single.just(it)
                    }!!

}