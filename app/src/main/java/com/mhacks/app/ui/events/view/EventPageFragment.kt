package com.mhacks.app.ui.events.view

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mhacks.app.R
import com.mhacks.app.data.kotlin.Event
import kotlinx.android.synthetic.main.events_pager_view.*

/**
 * Created by jeffreychang on 9/22/17.
 */
class EventPageFragment: Fragment() {


    private val listAdapter = SectionedEventsAdapter()
    private lateinit var newList: ArrayList<EventsSection.EventSectionModel>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.events_pager_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (arguments != null) {
//            val eventList = arguments.getParcelableArrayList<Event>(EXTRA_EVENT_LIST)
//            events_recycler_view.layoutManager = LinearLayoutManager(context)
//            events_recycler_view.adapter = listAdapter
//            val groupedList = eventList.groupBy { it.startDateTs }
//
//            newList = ArrayList()
//            for ((key, value) in groupedList) {
//                newList.add(EventsSection.EventSectionModel(key!!, ArrayList(value)))
//            }
//            listAdapter.removeAllSections()
//            listAdapter.addAllEventsSections(newList)
        }

    }


    companion object {
        fun newInstance(day: String, event: List<EventsFragment.EventWithDay>): EventPageFragment {
            val fragment = EventPageFragment()
            val args = Bundle()

            val ev = event.map { ev -> ev.event }

            args.putString(EXTRA_DAY_OF_THE_WEEK, day)
//            args.putParcelableArrayList(EXTRA_EVENT_LIST, ArrayList(ev))

            fragment.arguments = args
            return fragment
        }
        private val EXTRA_DAY_OF_THE_WEEK = "EXTRA_DAY_OF_THE_WEEK"
        private val EXTRA_EVENT_LIST = "EXTRA_EVENT_LIST"
    }


}