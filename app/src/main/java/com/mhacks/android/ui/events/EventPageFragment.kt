package com.mhacks.android.ui.events

import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.support.v4.app.Fragment

/**
 * Created by jeffreychang on 9/22/17.
 */
class EventPageFragment: Fragment() {

    companion object {
        fun newInstance(day: String, event: List<EventsFragment.EventWithDay>): EventPageFragment {
            val fragment = EventPageFragment()
            val args = Bundle()
//            args.putParcelableArrayList()
            fragment.arguments = args
            return fragment
        }
    }


}