package com.mhacks.android.ui.kotlin.schedule


import android.graphics.Color
import android.graphics.RectF
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.View
import android.widget.LinearLayout

import com.alamkanak.weekview.MonthLoader
import com.alamkanak.weekview.WeekView
import com.alamkanak.weekview.WeekViewEvent
import com.mhacks.android.data .model.Event
import com.mhacks.android.data .network.HackathonCallback
import com.mhacks.android.data .network.NetworkManager
import com.mhacks.android.ui.common.BaseFragment
import com.mhacks.android.ui.common.NavigationColor
import com.mhacks.android.ui.events.EventDetailsFragment
import com.mhacks.android.ui.map.LocationManager

import org.mhacks.android.R

import java.util.ArrayList
import java.util.Calendar
import java.util.Date
import java.util.GregorianCalendar
import java.util.TimeZone


/**
 * Created by Omkar Moghe on 10/25/2014.
 *
 *
 * Builds schedule with events pulled from the Parse database. Uses the EventDetailsFragment to
 * create event details.
 */
class EventFragment : BaseFragment(), WeekView.EventClickListener, MonthLoader.MonthChangeListener, WeekView.EventLongPressListener {

    override var setTransparent: Boolean = false
    override var AppBarTitle: Int = R.string.title_events
    override var NavigationColor: NavigationColor = NavigationColor(R.color.colorPrimary, R.color.colorPrimaryDark)
    override var LayoutResourceID: Int = R.layout.fragment_schedule
    override var configureView: (view: View) -> Unit? = fun(view: View) {

        mScheduleContainer = view.findViewById<LinearLayout>(R.id.schedule_container)
        mWeekView = view.findViewById<WeekView>(R.id.week_view)
        setUpWeekView()
        val manager = LocationManager.getInstance()

    }
    // network manager
    private val networkManager = NetworkManager.getInstance()

    // Declaring Views
    private var mScheduleContainer: LinearLayout? = null
    private var mWeekView: WeekView? = null

    // Event data structures
    private var mEvents: ArrayList<Event>? = null
    lateinit private var weekViewEvents: ArrayList<WeekViewEvent>

    // Booleans
    var eventDetailsOpened = false
        private set //Prevents multiple EventDetailFragments from opening.

    // Declares the EventDetailsFragment
    private val eventDetailsFragment: EventDetailsFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    /**
     * Builds the calendar using the WeekView class.
     * Done programmatically because the XML wouldn't auto-complete at the time. - Omkar
     */
    private fun setUpWeekView() {
        //Set listeners
        mWeekView!!.setOnEventClickListener(this)
        mWeekView!!.monthChangeListener = this
        mWeekView!!.eventLongPressListener = this
        //Set up visuals of the calendar
        mWeekView!!.setBackgroundColor(Color.WHITE)
        mWeekView!!.eventTextColor = Color.WHITE
        mWeekView!!.numberOfVisibleDays = 1
        mWeekView!!.textSize = 22
        mWeekView!!.hourHeight = 120
        mWeekView!!.headerColumnPadding = 8
        //        mWeekView.setHeaderColumnTextColor(getResources().getColor(R.color.header_column_text_color));
        mWeekView!!.headerRowPadding = 16
        mWeekView!!.columnGap = 8
        mWeekView!!.hourSeparatorColor = Color.WHITE
        mWeekView!!.hourSeparatorHeight = 4
        mWeekView!!.headerColumnBackgroundColor = Color.WHITE
        //        mWeekView.setHeaderRowBackgroundColor(getResources().getColor(R.color.header_row_bg_color));
        //        mWeekView.setDayBackgroundColor(getResources().getColor(R.color.day_bg_color));
        //        mWeekView.setTodayBackgroundColor(getResources().getColor(R.color.today_bg_color));
        mWeekView!!.headerColumnBackgroundColor = Color.BLACK
        mWeekView!!.overlappingEventGap = 2
    }

    fun getEvents() {
        networkManager.getEvents(object : HackathonCallback<List<Event>> {
            override fun success(response: List<Event>) {
                mEvents = ArrayList(response)
                Log.d(TAG, "got " + mEvents!!.size + " events")

                activity.runOnUiThread { mWeekView!!.notifyDatasetChanged() }
            }

            override fun failure(error: Throwable) {

            }
        })
    }

    fun createWeekViewEvents(events: ArrayList<Event>, month: Int): ArrayList<WeekViewEvent> {
        weekViewEvents = ArrayList<WeekViewEvent>()

        var id: Long = 0

        for (event in events) {
            // Create start event.
            val startTime = GregorianCalendar(TimeZone.getDefault())
            startTime.time = Date(event.getStart())

            // Create end event.
            val endTime = startTime.clone() as GregorianCalendar
            endTime.add(Calendar.SECOND, event.getDuration().toInt())

            // Set color based on EventType (Category).
            val color = getEventColor(event.getCategory())

            // Create a WeekViewEvent
            val weekViewEvent = WeekViewEvent(id, event.getName(), startTime, endTime)
            weekViewEvent.color = color

            // Add the WeekViewEvent to the list.
            if (startTime.get(Calendar.MONTH) == month) weekViewEvents!!.add(weekViewEvent)

            // Increment the id
            id++
        }

        Log.d(TAG, "created " + weekViewEvents!!.size + " events")
        return weekViewEvents
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun onResume() {
        super.onResume()
    }

    /**
     * Takes the event type based on the EventType class in Parse and returns the corresponding
     * color of the event.
     * @param eventType Event type/category.
     * *
     * @return color of the event.
     */
    fun getEventColor(eventType: Int): Int {
        when (eventType) {
            0 -> return 0
            1 -> return 1
            3 -> return 3
            4 -> return 4
            else -> return ContextCompat.getColor(activity, R.color.md_cyan_50)
        }
    }

    fun setEventDetailsOpened(bool: Boolean?) {
        eventDetailsOpened = bool!!
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////
    //  HANDLES BUTTON CICKS FOR EventFragment AND EventDetailsFragment
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Gets clicked view in the EventFragment and the EventDetailsFragment and handles them
     * appropriately.

     * @param v View that was clicked
     */
    fun scheduleFragmentClick(v: View) {
        //Switch the id of the clicked view.
        when (v.id) {
            R.id.event_close_button -> closeEventDetails()
            else -> {
            }
        }
    }

    /**
     * Refresh all the events from the Parse database and call the onMonthChange listener to
     * re-draw the new events on the calendar.
     */
    fun refreshEvents() {
        getEvents()
    }

    /**
     * Finds the instance of the EventDetails fragment that is in the R.id.drawer_layout and closes
     * it. Uses findFragmentById because the SecondaryActivity can create an EventDetailsFragment from
     * a push notification when the EventFragment itself is not open, (ie eventDetailsFragment
     * has not yet been declared or instantiated).
     */
    fun closeEventDetails() {
        //R


    }

    override fun onEventClick(event: WeekViewEvent, eventRect: RectF) {
        //        if (!eventDetailsOpen) {
        //            eventDetailsFragment =
        //                    EventDetailsFragment.newInstance(mEvents.get((int) event.getId()), event.getColor());
        //            eventDetailsFragment.setParent(this);
        //            getActivity().getFragmentManager()
        //                         .beginTransaction()R
        //                         .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        //                         .addToBackStack(null) //IMPORTANT. Allows the EventDetailsFragment to be closed.
        //                         .add(R.id.drawer_layout, eventDetailsFragment)
        //                         .commit();
        //            //Hide the toolbar so the event details are full screen.
        //            ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        //            //Prevents other events from being clicked while one event's details are being shown.
        //            setEventDetailsOpened(true);
        //        }
    }

    override fun onMonthChange(newYear: Int, newMonth: Int): List<WeekViewEvent> {
        if (mEvents == null || mEvents!!.size == 0) {
            getEvents()
            return ArrayList()
        } else {
            // NOTE: WeekView indexes at 1, Calendar indexes at 0.
            return createWeekViewEvents(mEvents!!, newMonth - 1)
        }
    }

    override fun onEventLongPress(event: WeekViewEvent, eventRect: RectF) {

    }

    companion object {

        val TAG = "EventFragment"

        val instance: EventFragment
            get() = EventFragment()
    }

}
