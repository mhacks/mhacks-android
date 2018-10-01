package com.mhacks.app.ui.events.widget

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
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
            onEventClicked: ((event: Event, isChecked: Boolean) -> Unit)?) {

        events_description_item_title.text = event.name
        events_description_item_description.text = event.desc
        events_description_item_like_check_box.isChecked = event.favorited
        events_description_item_like_check_box.setOnCheckedChangeListener { _, isChecked ->
            onEventClicked?.invoke(event, isChecked)
        }
    }
}