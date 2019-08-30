package org.mhacks.app.events.widget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import org.mhacks.app.eventlibrary.data.model.Event
import org.mhacks.app.event.databinding.FragmentEventPageBinding
import org.mhacks.app.eventlibrary.EventWithDay
import org.mhacks.app.eventlibrary.data.model.EventSectionModel

/**
 * Create page for each insertFavoriteEvent day.
 */
class EventPageFragment : Fragment() {

    private lateinit var binding: FragmentEventPageBinding

    var onEventsClicked: ((event: Event, isChecked: Boolean) -> Unit)? = null

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEventPageBinding.inflate(
                LayoutInflater.from(container?.context),
                container,
                false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (arguments != null) {
            val eventList = arguments?.getParcelableArrayList<Event>(EXTRA_EVENT_LIST)
            val groupedList = eventList?.groupBy { it.startDateTs }

            val eventSectionModelList = ArrayList<EventSectionModel>()
            groupedList?.let {
                for ((time, value) in it)
                    eventSectionModelList
                            .add(EventSectionModel(time, ArrayList(value)))
            }
            context?.let { context ->
                binding.fragmentEventPageRecyclerView.layoutManager = LinearLayoutManager(context)
                onEventsClicked?.let { callback ->
                    binding.fragmentEventPageRecyclerView.adapter =
                            EventsRecyclerViewAdapter(eventSectionModelList, callback)
                }
            }
        }
    }

    companion object {

        fun newInstance(day: String, event: List<EventWithDay>)
                : EventPageFragment {
            val fragment = EventPageFragment()
            val args = Bundle()

            val ev = event.map { ev -> ev.event }

            args.putString(EXTRA_DAY_OF_THE_WEEK, day)
            args.putParcelableArrayList(EXTRA_EVENT_LIST, ArrayList(ev))

            fragment.arguments = args
            return fragment
        }

        private const val EXTRA_DAY_OF_THE_WEEK = "EXTRA_DAY_OF_THE_WEEK"

        private const val EXTRA_EVENT_LIST = "EXTRA_EVENT_LIST"
    }
}