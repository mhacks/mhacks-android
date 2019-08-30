package org.mhacks.app.eventlibrary.widget

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import org.mhacks.app.eventlibrary.data.model.Event
import org.mhacks.app.eventlibrary.data.model.EventSectionModel
import org.mhacks.app.eventlibrary.databinding.ItemEventTimelineBinding

/**
 * Creates lists of timeline views.
 */
class EventTimeLineItem(context: Context) : ConstraintLayout(context) {

    var onEventClicked: ((event: Event, isChecked: Boolean) -> Unit)? = null

    private val binding = ItemEventTimelineBinding.inflate(
            LayoutInflater.from(context),
            this,
            true
    )

    init {
        layoutParams = LayoutParams(
                LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    var timeLineType: Int = -999
        set(viewType) {
            field = viewType
            binding.eventTimelineView.initLine(viewType)
        }

    var timeText: String? = null
        set(time) {
            field = time
            binding.eventDescriptionTimeTextview.text = time
        }

    fun addEventGroup(eventsSection: EventSectionModel) {
        binding.eventDescriptionLinearLayout.removeAllViews()
        for (event in eventsSection.events) {
            val eventDescriptionItem = EventDescriptionItem(context, null)

            eventDescriptionItem.bindItem(event, onEventClicked)
            binding.eventDescriptionLinearLayout.addView(eventDescriptionItem)
        }
    }
}