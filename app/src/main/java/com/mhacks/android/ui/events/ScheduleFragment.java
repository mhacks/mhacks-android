package com.mhacks.android.ui.events;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;
import com.mhacks.android.data.model.Event;
import com.mhacks.android.data.network.HackathonCallback;
import com.mhacks.android.data.network.NetworkManager;
import com.mhacks.android.ui.map.LocationManager;

import org.mhacks.android.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;


/**
 * Created by Omkar Moghe on 10/25/2014.
 * <p/>
 * Builds schedule with events pulled from the Parse database. Uses the EventDetailsFragment to
 * create event details.
 */
public class ScheduleFragment extends Fragment implements WeekView.EventClickListener,
                                                          MonthLoader.MonthChangeListener,
                                                          WeekView.EventLongPressListener {

    public static final String TAG = "ScheduleFragment";

    // network manager
    private final NetworkManager networkManager = NetworkManager.getInstance();

    // Declaring Views
    private LinearLayout mScheduleContainer;
    private WeekView mWeekView;

    // Event data structures
    private ArrayList<Event>         mEvents;
    private ArrayList<WeekViewEvent> weekViewEvents;

    // Booleans
    private boolean eventDetailsOpen = false; //Prevents multiple EventDetailFragments from opening.

    // Declares the EventDetailsFragment
    private EventDetailsFragment eventDetailsFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater,
                             final ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schedule, container, false);

        mScheduleContainer = (LinearLayout) view.findViewById(R.id.schedule_container);
        mWeekView = (WeekView) view.findViewById(R.id.week_view);
        setUpWeekView();

        LocationManager manager = LocationManager.getInstance();

        return view;
    }

    /**
     * Builds the calendar using the WeekView class.
     * Done programmatically because the XML wouldn't auto-complete at the time. - Omkar
     */
    private void setUpWeekView() {
        //Set listeners
        mWeekView.setOnEventClickListener(this);
        mWeekView.setMonthChangeListener(this);
        mWeekView.setEventLongPressListener(this);
        //Set up visuals of the calendar
        mWeekView.setBackgroundColor(Color.WHITE);
        mWeekView.setEventTextColor(Color.WHITE);
        mWeekView.setNumberOfVisibleDays(1);
        mWeekView.setTextSize(22);
        mWeekView.setHourHeight(120);
        mWeekView.setHeaderColumnPadding(8);
        mWeekView.setHeaderColumnTextColor(getResources().getColor(R.color.header_column_text_color));
        mWeekView.setHeaderRowPadding(16);
        mWeekView.setColumnGap(8);
        mWeekView.setHourSeparatorColor(Color.WHITE);
        mWeekView.setHourSeparatorHeight(4);
        mWeekView.setHeaderColumnBackgroundColor(Color.WHITE);
        mWeekView.setHeaderRowBackgroundColor(getResources().getColor(R.color.header_row_bg_color));
        mWeekView.setDayBackgroundColor(getResources().getColor(R.color.day_bg_color));
        mWeekView.setTodayBackgroundColor(getResources().getColor(R.color.today_bg_color));
        mWeekView.setHeaderColumnBackgroundColor(Color.BLACK);
        mWeekView.setOverlappingEventGap(2);
    }

    public void getEvents() {
        networkManager.getEvents(new HackathonCallback<List<Event>>() {
            @Override
            public void success(List<Event> response) {
                mEvents = new ArrayList<Event>(response);
                Log.d(TAG, "got " + mEvents.size() + " events");

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mWeekView.notifyDatasetChanged();
                    }
                });
            }

            @Override
            public void failure(Throwable error) {

            }
        });
    }

    public ArrayList<WeekViewEvent> createWeekViewEvents(ArrayList<Event> events, int month) {
        weekViewEvents = new ArrayList<WeekViewEvent>();

        long id = 0;

        for (Event event : events) {
            // Create start event.
            GregorianCalendar startTime = new GregorianCalendar(TimeZone.getDefault());
            startTime.setTime(new Date(event.getStart()));

            // Create end event.
            GregorianCalendar endTime = (GregorianCalendar) startTime.clone();
            endTime.add(Calendar.SECOND, (int) event.getDuration());

            // Set color based on EventType (Category).
            int color = getEventColor(event.getCategory());

            // Create a WeekViewEvent
            WeekViewEvent weekViewEvent = new WeekViewEvent(id, event.getName(), startTime, endTime);
            weekViewEvent.setColor(color);

            // Add the WeekViewEvent to the list.
            if (startTime.get(Calendar.MONTH) == month) weekViewEvents.add(weekViewEvent);

            // Increment the id
            id++;
        }

        Log.d(TAG, "created " + weekViewEvents.size() + " events");
        return weekViewEvents;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    /**
     * Takes the event type based on the EventType class in Parse and returns the corresponding
     * color of the event.
     * @param eventType Event type/category.
     * @return color of the event.
     */
    public int getEventColor(int eventType) {
        switch (eventType) {
            case 0: // Logistics - GO BLUE
                return ContextCompat.getColor(getActivity(), R.color.event_blue);
            case 1: // Social - Red
                return ContextCompat.getColor(getActivity(), R.color.event_red);
            case 2: // Food - MAIZE
                return ContextCompat.getColor(getActivity(), R.color.event_yellow);
            case 3: // Tech Talk - Purple
                return ContextCompat.getColor(getActivity(), R.color.event_purple);
            case 4: // Other - Green
                return ContextCompat.getColor(getActivity(), R.color.event_green);
            default:
                return ContextCompat.getColor(getActivity(), R.color.event_blue);
        }
    }

    public boolean getEventDetailsOpened () {
        return eventDetailsOpen;
    }

    public void setEventDetailsOpened(Boolean bool) {
        eventDetailsOpen = bool;
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////
    //  HANDLES BUTTON CICKS FOR ScheduleFragment AND EventDetailsFragment
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Gets clicked view in the ScheduleFragment and the EventDetailsFragment and handles them
     * appropriately.
     *
     * @param v View that was clicked
     */
    public void scheduleFragmentClick(View v) {
        //Switch the id of the clicked view.
        switch (v.getId()) {
            case R.id.event_close_button:
                closeEventDetails();
                break;
            default:
                break;
        }
    }

    /**
     * Refresh all the events from the Parse database and call the onMonthChange listener to
     * re-draw the new events on the calendar.
     */
    public void refreshEvents() {
        getEvents();
    }

    /**
     * Finds the instance of the EventDetails fragment that is in the R.id.drawer_layout and closes
     * it. Uses findFragmentById because the MainActivity can create an EventDetailsFragment from
     * a push notification when the ScheduleFragment itself is not open, (ie eventDetailsFragment
     * has not yet been declared or instantiated).
     */
    public void closeEventDetails () {
        //Close the EventDetailsFragment
        getActivity().getFragmentManager().beginTransaction()
                     .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                     .remove(getFragmentManager().findFragmentById(R.id.drawer_layout)).commit();
        setEventDetailsOpened(false);


    }

    @Override
    public void onEventClick(WeekViewEvent event, RectF eventRect) {
        if (!eventDetailsOpen) {
            eventDetailsFragment =
                    EventDetailsFragment.newInstance(mEvents.get((int) event.getId()), event.getColor());
            eventDetailsFragment.setParent(this);
            getActivity().getFragmentManager()
                         .beginTransaction()
                         .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                         .addToBackStack(null) //IMPORTANT. Allows the EventDetailsFragment to be closed.
                         .add(R.id.drawer_layout, eventDetailsFragment)
                         .commit();
            //Hide the toolbar so the event details are full screen.
            ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
            //Prevents other events from being clicked while one event's details are being shown.
            setEventDetailsOpened(true);
        }
    }

    @Override
    public List<? extends WeekViewEvent> onMonthChange(int newYear, int newMonth) {
        if (mEvents == null || mEvents.size() == 0) {
            getEvents();
            return new ArrayList<WeekViewEvent>();
        } else {
            // NOTE: WeekView indexes at 1, Calendar indexes at 0.
            return createWeekViewEvents(mEvents, newMonth - 1);
        }
    }

    @Override
    public void onEventLongPress(WeekViewEvent event, RectF eventRect) {

    }

}
