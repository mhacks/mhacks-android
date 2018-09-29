package com.mhacks.app.ui.events.view

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import com.mhacks.app.data.models.Event
import kotlinx.android.synthetic.main.events_description_item.view.*
import org.mhacks.mhacksui.R

/**
 * Add events descriptions within a Linear Layout
 */
class EventDescriptionItem(context: Context, attrs: AttributeSet?)
    : ConstraintLayout(context, attrs) {

    init {
        inflate(context, R.layout.events_description_item, this)
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
    }

    fun bindItem(
            event: Event,
            onEventClicked: (event: Event, isChecked: Boolean) -> Unit) {

        events_description_item_title.text = event.name
        events_description_item_description.text = event.desc
        events_description_item_like_image_view.setOnCheckedChangeListener { _, isChecked ->
            onEventClicked.invoke(event, isChecked)
        }
    }
}