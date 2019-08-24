package org.mhacks.app.events.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import org.mhacks.app.events.data.model.Event
import org.mhacks.app.event.databinding.ItemEventDescBinding

/**
 * Add events descriptions within a Linear Layout
 */
class EventDescriptionItem(context: Context, attrs: AttributeSet?)
    : ConstraintLayout(context, attrs) {

    private var binding: ItemEventDescBinding =
            ItemEventDescBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
    }

    fun bindItem(
            event: Event,
            onEventClicked: ((event: Event, isChecked: Boolean) -> Unit)?) {
        binding.apply {
            itemEventDescriptionTitle.text = event.name
            itemEventDescriptionDescTextView.text = event.desc
            itemEventDescriptionLikeCheckBox.isChecked = event.favorited
            itemEventDescriptionLikeCheckBox.setOnCheckedChangeListener { _, isChecked ->
                onEventClicked?.invoke(event, isChecked)
            }
        }
    }
}