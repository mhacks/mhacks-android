package com.mhacks.app.ui.events.view

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import com.mhacks.app.R
import kotlinx.android.synthetic.main.events_description_item.view.*

/**
 * Add events descriptions within a Linear Layout
 */
class EventDescriptionItem(context: Context, attrs: AttributeSet?)
    : ConstraintLayout(context, attrs) {

    init {
        inflate(context, R.layout.events_description_item, this)
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
    }

    fun bindItem(title: String, description: String) {
        events_description_item_title.text = title
        events_description_item_description.text = description
    }
}