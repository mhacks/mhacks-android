package com.mhacks.app.ui.events.widget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import org.mhacks.mhacksui.R
import com.mhacks.app.data.models.Event
import com.mhacks.app.ui.events.EventsViewModel
import com.mhacks.app.ui.events.model.EventSectionModel
import kotlinx.android.synthetic.main.events_pager_view.*

/**
 * Create page for each insertFavoriteEvent day.
 */
class EventPageFragment: Fragment() {

    var onEventsClicked: ((event: Event, isChecked: Boolean) -> Unit)? = null

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?)
            = inflater.inflate(R.layout.events_pager_view, container, false)!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (arguments != null) {
            val eventList = arguments?.getParcelableArrayList<Event>(EXTRA_EVENT_LIST)
            val groupedList = eventList?.groupBy { it.startDateTs }

            val eventSectionModelList  = ArrayList<EventSectionModel>()
            groupedList?.let {
                for ((time, value) in it)
                    eventSectionModelList
                            .add(EventSectionModel(time, ArrayList(value)))
            }
            context?.let { context ->
                events_recycler_view.layoutManager = LinearLayoutManager(context)
                onEventsClicked?.let { callback ->
                    events_recycler_view.adapter =
                            EventsRecyclerViewAdapter(context, eventSectionModelList, callback)
                }
            }
        }
    }

    companion object {

        fun newInstance(day: String, events: List<EventsViewModel.EventWithDay>)
                : EventPageFragment {
            val fragment = EventPageFragment()
            val args = Bundle()

            val ev = events.map { ev -> ev.event }

            args.putString(EXTRA_DAY_OF_THE_WEEK, day)
            args.putParcelableArrayList(EXTRA_EVENT_LIST, ArrayList(ev))

            fragment.arguments = args
            return fragment
        }

        private const val EXTRA_DAY_OF_THE_WEEK = "EXTRA_DAY_OF_THE_WEEK"

        private const val EXTRA_EVENT_LIST = "EXTRA_EVENT_LIST"
    }
}