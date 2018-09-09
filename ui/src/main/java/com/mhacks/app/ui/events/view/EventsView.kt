package com.mhacks.app.ui.events.view

import com.mhacks.app.data.models.Event

/**
 * Contract for the Events View.
 */
interface EventsView {

    fun onGetEventsSuccess(events: List<Event>)

    fun onGetEventsFailure(error: Throwable)
}