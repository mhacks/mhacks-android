package org.mhacks.app.events.widget

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.vipulasri.timelineview.TimelineView
import org.mhacks.app.events.data.model.Event
import org.mhacks.app.events.data.model.EventSectionModel
import org.threeten.bp.Instant
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter

/**
 * RecyclerViewAdapter that creates each individual timeline for the respective day.
 */
class EventsRecyclerViewAdapter(
        val events: ArrayList<EventSectionModel>,
        private val eventsCallback: (
                event: Event,
                isChecked: Boolean) -> Unit)
    : RecyclerView.Adapter<EventsRecyclerViewAdapter.EventViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val viewHolder = EventViewHolder(EventTimeLineItem(parent.context))
        viewHolder.eventTimeLineItem.timeLineType = viewType
        viewHolder.eventTimeLineItem.onEventClicked = eventsCallback
        return viewHolder
    }

    override fun getItemCount() = events.size

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.eventTimeLineItem.addEventGroup(events[position])
        val localTime =
                Instant.ofEpochMilli(events[position].time)
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime()
        val formatter = DateTimeFormatter.ofPattern("h:mm a")

        holder.eventTimeLineItem.timeText = formatter.format(localTime)
    }

    override fun getItemViewType(position: Int) =
            TimelineView.getTimeLineViewType(position, itemCount)

    class EventViewHolder(val eventTimeLineItem: EventTimeLineItem)
        : RecyclerView.ViewHolder(eventTimeLineItem)

}
