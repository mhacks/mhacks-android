package com.mhacks.android.ui.events;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.*;
import android.widget.LinearLayout;
import com.alamkanak.weekview.WeekViewEvent;
import com.mhacks.android.data_old.model.Event;
import com.mhacks.android.data_old.model.Location;
import com.mhacks.android.ui.MainActivity;

import org.mhacks.android.R;
import com.parse.*;

import java.util.*;


/**
 * Created by Omkar Moghe on 10/25/2014.
 * <p/>
 * Builds schedule with events pulled from the Parse database. Uses the EventDetailsFragment to
 * create event details.
 */
public class ScheduleFragment extends Fragment implements WeekViewModified.EventClickListener,
        WeekViewModified.EventLongPressListener,
        WeekViewModified.MonthChangeListener {

    public static final String TAG = "ScheduleFragment";

    //Local datastore pin name.
    private static final String EVENT_PIN = "eventPin";
    private static final String LOCATION_PIN = "locationPin";

    // Month number for which to get events
    private static final int SEPTEMBER_MONTH = 9;

    //Declaring Views
    private View mScheduleFragView;
    private WeekViewModified mWeekView;
    private LinearLayout mScheduleContainer;

    //Current query
    private ParseQuery<Event> currentQuery;

    /*Calendar view uses WeekViewEvent objects to build the calendar. WeekViewEvent objects built
    using Event (ParseObject) pulled from the Parse database.*/
    private List<WeekViewEvent> finalWeekViewEvents;
    private List<Event> finalEvents;

    //Booleans
    private boolean hasWeekViewBeenSetUp = false; //Sets up the WeekView on the initial start.
    private boolean eventDetailsOpen = false; //Prevents multiple EventDetailFragments from opening.

    //Declares the EventDetailsFragment
    private EventDetailsFragment eventDetailsFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater,
                             final ViewGroup container,
                             Bundle savedInstanceState) {
        mScheduleFragView = inflater.inflate(R.layout.fragment_schedule, container, false);

        hasWeekViewBeenSetUp = false;
        getLocalEvents(SEPTEMBER_MONTH); //Called initially to build the schedule view and query events

        return mScheduleFragView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(currentQuery != null) currentQuery.cancel();
    }

    /**
     * Builds the calendar using the WeekViewModified class. Adds the view to the LinearLayout
     * mScheduleContainer. Done programmatically to prevent the WeekView from being created before
     * the Event query is finished. (Causes NullPointerException).
     */
    private void setUpWeekView() {
        //Instantiate LinearLayout
        mScheduleContainer = (LinearLayout) mScheduleFragView.findViewById(R.id.schedule_container);
        mWeekView = new WeekViewModified(getActivity());
        //Set listeners
        mWeekView.setOnEventClickListener(this);
        mWeekView.setMonthChangeListener((WeekViewModified.MonthChangeListener) this);
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
        mWeekView.setHeaderColumnBackgroundColor(Color.WHITE);
        mWeekView.setHeaderRowBackgroundColor(getResources().getColor(R.color.header_row_bg_color));
        mWeekView.setDayBackgroundColor(getResources().getColor(R.color.day_bg_color));
        mWeekView.setTodayBackgroundColor(getResources().getColor(R.color.today_bg_color));
        mWeekView.setHeaderColumnBackgroundColor(Color.BLACK);
        mWeekView.setOverlappingEventGap(2);
        mWeekView.setHorizontalScrollEnabled(true);
        //Add the view to the LinearLayout
        mScheduleContainer.addView(mWeekView);
    }

    /**
     * Creates a base query to use for getting events from Parse
     */
    private ParseQuery<Event> getBaseEventQuery() {
        ParseQuery<Event> query = ParseQuery.getQuery("Event");
        query.include("category"); //Pulls EventType object.
        query.include("host"); //Pulls Sponsor object.
        query.include("locations"); //Pulls Location JSON array.
        currentQuery = query;
        return query;
    }

    /**
     * Queries Event objects from the local datastore and assigns the list to the global List<Event> events.
     *
     * @param newMonth Month for which to get events.
     */
    private void getLocalEvents(final int newMonth) {
        ParseQuery<Event> query = getBaseEventQuery();
        query.fromPin(EVENT_PIN);
        query.findInBackground(new FindCallback<Event>() {
            public void done(List<Event> eventList, ParseException e) {
                if (e != null || eventList == null) {
                    Log.e(TAG, "Couldn't get any local events, falling back on remote");
                } else {
                    Log.d(TAG, "Found local events, displaying them, then updating from remote");
                    createEvents(eventList, newMonth);
                }

                getRemoteEvents(newMonth);
            }
        });
    }

    /**
     * Queries Event objects from Parse and assigns the list to the global List<Event> events.
     *
     * @param newMonth Month for which to get events.
     */
    private void getRemoteEvents(final int newMonth) {
        ParseQuery<Location> locQuery = ParseQuery.getQuery("Location");
        locQuery.findInBackground(new FindCallback<Location>() {
            public void done(List<Location> locList, ParseException f) {
                if (f == null && locList != null && locList.size() > 0) {
                    ParseObject.unpinAllInBackground(LOCATION_PIN);
                    ParseObject.pinAllInBackground(LOCATION_PIN, locList);
                }
            }
        });

        ParseQuery<Event> query = getBaseEventQuery();
        query.findInBackground(new FindCallback<Event>() {
            public void done(List<Event> eventList, ParseException e) {
                if (e != null || eventList == null) {
                    Log.e(TAG, "Couldn't fetch the remote events");

                    // We don't have anything locally, let the user know we need internet
                    if(finalEvents == null || finalEvents.size() <= 0) {
                        if(getActivity() != null) ((MainActivity)getActivity()).showNoInternetOverlay();
                    } else {
                        // We do have local stuff, no make sure we aren't in the way
                        if(getActivity() != null) ((MainActivity)getActivity()).hideNoInternetOverlay();
                    }
                } else {
                    Log.d(TAG, "Got the remote events, displaying them");

                    // We got the remote events, so unpin the old ones and pin the new ones
                    ParseObject.unpinAllInBackground(EVENT_PIN);
                    ParseObject.pinAllInBackground(EVENT_PIN, eventList);

                    // Display the new events and get outta the way
                    Log.d(TAG, eventList.size() + " events");
                    createEvents(eventList, newMonth);
                    if(getActivity() != null) ((MainActivity)getActivity()).hideNoInternetOverlay();
                }
            }
        });
    }

    /**
     * Uses a list of Event objects to build WeekViewEvent objects that the WeekView uses to draw
     * the calendar.
     *
     * @param eventList List of Event objects
     * @param newMonth  Month for which to get events.
     */
    public void createEvents(List<Event> eventList, int newMonth) {
        finalWeekViewEvents = new ArrayList<WeekViewEvent>();
        finalEvents = new ArrayList<Event>(eventList);

        //Id for the events. Doubles as the position of the event in both lists.
        long id = 0;

        //For each loop that builds WeekViewEvent objects from each Event object.
        for (Event event : eventList) {
            //Create start event.
            GregorianCalendar startTime = new GregorianCalendar(TimeZone.getDefault());
            startTime.setTime(event.getStartTime());

            //Create end event.
            GregorianCalendar endTime = (GregorianCalendar) startTime.clone();
            int hourDuration = event.getDuration() / 3600;      //getDuration returns seconds as an int. Need to convert to hours.
            int minuteDuration = (event.getDuration() % 3600) / 60;    //Converting remainder of minutes to int minutes.
            endTime.add(Calendar.HOUR_OF_DAY, hourDuration);
            endTime.add(Calendar.MINUTE, minuteDuration);

            //Set color based on EventType (Category).
            int color;
            if (event.getCategory() != null) {
                color = getEventColor(event.getCategory().getColor());
            } else {
                color = getEventColor(-1);
            }

            //Create a WeekViewEvent
            WeekViewEvent weekViewEvent = new WeekViewEvent(id, event.getTitle(), startTime, endTime);
            weekViewEvent.setColor(color);

            //Add the WeekViewEvent to the list.
            finalWeekViewEvents.add(weekViewEvent);

            //Increment the id
            id++;
        }

        if(hasWeekViewBeenSetUp && mWeekView != null) {
            mWeekView.notifyDatasetChanged();
        } else {
            setUpWeekView();
            hasWeekViewBeenSetUp = true;
        }
    }

    /**
     * Takes the event type based on the EventType class in Parse and returns the corresponding
     * color of the event.
     * @param eventType Event type/category.
     * @return color of the event.
     */
    public int getEventColor(int eventType) {
        switch (eventType) {
            case 0: //Red
                return getResources().getColor(R.color.event_red);
            case 1: //Orange
                return getResources().getColor(R.color.event_orange);
            case 2: //Yellow
                return getResources().getColor(R.color.event_yellow);
            case 3: //Green
                return getResources().getColor(R.color.event_green);
            case 4: //Blue
                return getResources().getColor(R.color.event_blue);
            case 5: //Purple
                return getResources().getColor(R.color.event_purple);
            default:
                return getResources().getColor(R.color.mh_yellow);
        }
    }

    @Override
    public void onEventClick(WeekViewEvent event, RectF eventRect) {
        if (!eventDetailsOpen) {
            eventDetailsFragment =
                    EventDetailsFragment.newInstance(finalEvents.get((int) event.getId()), event.getColor());
            getActivity().getFragmentManager()
                         .beginTransaction()
                         .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .addToBackStack(null) //IMPORTANT. Allows the EventDetailsFragment to be closed.
                         .add(R.id.drawer_layout, eventDetailsFragment)
                    .commit();
            //Hide the toolbar so the event details are full screen.
            ((ActionBarActivity) getActivity()).getSupportActionBar().hide();
            //Prevents other events from being clicked while one event's details are being shown.
            setEventDetailsOpened(true);
        }
    }

    public boolean getEventDetailsOpened () {
        return eventDetailsOpen;
    }

    public void setEventDetailsOpened(Boolean bool) {
        eventDetailsOpen = bool;
    }

    @Override
    public void onEventLongPress(WeekViewEvent event, RectF eventRect) {
    }

    @Override
    public List<WeekViewEvent> onMonthChange(int newYear, int newMonth) {
        /* Checks to see if the month being called is September (9). Not the best way to handle it but
        it works for this case since we only care about 3 days */
        if (newMonth == SEPTEMBER_MONTH) {
            //getRemoteEvents(newMonth);
            return finalWeekViewEvents;
        } else {
            /*onMonthChange is called 3 times by the view when it is created. This results in
            duplicate events being created. Thus, I return an empty ArrayList of WeekViewEvent
             objects if the month is not 1 (January).*/
            return new ArrayList<WeekViewEvent>();
        }
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
        getRemoteEvents(SEPTEMBER_MONTH); //Forces a remote Event query.
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

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //  TOOLBAR BUTTONS, ETC.
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_schedule_actions, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.schedule_action_refresh:
                refreshEvents();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
