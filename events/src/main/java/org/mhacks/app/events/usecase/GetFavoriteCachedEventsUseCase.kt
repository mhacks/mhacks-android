//package org.mhacks.app.events.usecase
//
//import org.mhacks.app.core.CoroutinesDispatcherProvider
//import org.mhacks.app.data.models.EventsResponse
//import org.mhacks.app.events.EventRepository
//import javax.inject.Inject
//
//class GetFavoriteCachedEventsUseCase @Inject constructor(
//        private val dispatcherProvider: CoroutinesDispatcherProvider,
//        private val eventRepository: EventRepository
//) {
//
//    fun getSingle(parameters: Unit): EventsResponse {
//        return eventRepository.getEvent()
//    }
//
////    : SingleUseCase<Unit, List<Event>>() {
////
////    override fun getSingle(parameters: Unit) =
////            eventRepository
////                    .getEventCache()
////                    .map {
////                        it.filter { events ->
////                            events.favorited
////                        }
////                    }!!
////
//}