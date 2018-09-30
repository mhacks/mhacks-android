package com.mhacks.app.ui.events.widget

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.view.ViewGroup
import com.mhacks.app.data.models.Event
import org.mhacks.mhacksui.R
import kotlinx.android.synthetic.main.events_timeline_item.view.*

/**
 * Add events TextViews to a Linear Layout
 */
class EventTimeLineItem(context: Context): ConstraintLayout(context) {

    var onEventClicked: ((event: Event, isChecked: Boolean) -> Unit)? = null

    init {
        inflate(context, R.layout.events_timeline_item, this)
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    var timeLineType: Int = -999
            set(viewType) {
                events_timeline_view.initLine(viewType)
            }

    var timeText: String? = null
            set(time) {
                events_description_time_textview.text = time
            }

    fun addEventGroup(eventsSection: EventsRecyclerViewAdapter.EventSectionModel) {
        events_description_linear_layout.removeAllViews()
        for (event in eventsSection.events) {
            val eventDescriptionItem = EventDescriptionItem(context, null)
            onEventClicked?.let {
                eventDescriptionItem.bindItem(event, it)
            }
            events_description_linear_layout.addView(eventDescriptionItem)
        }
    }
}