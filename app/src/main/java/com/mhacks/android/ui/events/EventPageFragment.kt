package com.mhacks.android.ui.events

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mhacks.android.data.kotlin.Event
import org.mhacks.android.R

/**
 * Created by jeffreychang on 9/22/17.
 */
class EventPageFragment: Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.events_pager_view, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (arguments != null) {
            val eventList = arguments.getParcelableArrayList<Event>(EXTRA_EVENT_LIST)
        }

    }


    companion object {
        fun newInstance(day: String, event: List<EventsFragment.EventWithDay>): EventPageFragment {
            val fragment = EventPageFragment()
            val args = Bundle()

            val ev = event.map { ev -> ev.event }

            args.putString( EXTRA_DAY_OF_THE_WEEK, day)
            args.putParcelableArrayList(EXTRA_EVENT_LIST, ArrayList(ev))

            fragment.arguments = args
            return fragment
        }
        private val EXTRA_DAY_OF_THE_WEEK = "EXTRA_DAY_OF_THE_WEEK"
        private val EXTRA_EVENT_LIST = "EXTRA_EVENT_LIST"
    }


}