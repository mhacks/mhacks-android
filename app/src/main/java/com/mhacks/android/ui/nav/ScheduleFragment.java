package com.mhacks.android.ui.nav;

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
import com.mhacks.android.data.model.Event;
import com.mhacks.android.ui.MainActivity;
import com.mhacks.android.ui.weekview.WeekViewModified;
import com.mhacks.iv.android.R;
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

    // Month number for which to get events
    private static final int JANUARY_MONTH = 1;

    //Declaring Views
    private View mScheduleFragView;
    private WeekViewModified mWeekView;
    private LinearLayout mScheduleContainer;

    //Parse user
    private ParseUser mUser;

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

        mUser = ParseUser.getCurrentUser();
        hasWeekViewBeenSetUp = false;
        getLocalEvents(JANUARY_MONTH); //Called initially to build the schedule view and query events

        return mScheduleFragView;
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
        //Set Jan 16th as "today"
        Calendar today = Calendar.getInstance();
        today.set(Calendar.YEAR, 2015);
        today.set(Calendar.MONTH, Calendar.JANUARY);
        today.set(Calendar.DATE, 16);
        mWeekView.setToday(today);
        //Set up visuals of the calendar
        mWeekView.setBackgroundColor(Color.WHITE);
        mWeekView.setEventTextColor(Color.WHITE);
        mWeekView.setNumberOfVisibleDays(3);
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
        mWeekView.setOverlappingEventGap(4);
        //Prevent scrolling.
        mWeekView.setHorizontalScrollEnabled(false);
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
        query.include("location"); //Pulls Location JSON array.
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
            switch (event.getCategory().getColor()) {
                case 0: //Red
                    color = getResources().getColor(R.color.event_red);
                    break;
                case 1: //Orange
                    color = getResources().getColor(R.color.event_orange);
                    break;
                case 2: //Yellow
                    color = getResources().getColor(R.color.event_yellow);
                    break;
                case 3: //Green
                    color = getResources().getColor(R.color.event_green);
                    break;
                case 4: //Blue
                    color = getResources().getColor(R.color.event_blue);
                    break;
                case 5: //Purple
                    color = getResources().getColor(R.color.event_purple);
                    break;
                default:
                    color = getResources().getColor(R.color.mh_purple);
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

    @Override
    public void onEventClick(WeekViewEvent event, RectF eventRect) {
        if (!eventDetailsOpen) {
            eventDetailsFragment =
                    EventDetailsFragment.newInstance(finalEvents.get((int) event.getId()), event.getColor());
            getActivity().getFragmentManager()
                    .beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .add(R.id.drawer_layout, eventDetailsFragment)
                    .addToBackStack(null) //IMPORTANT. Allows the EventDetailsFragment to be closed.
                    .commit();
            //Hide the toolbar so the event details are full screen.
            ((ActionBarActivity) getActivity()).getSupportActionBar().hide();
            //Prevents other events from being clicked while one event's details are being shown.
            eventDetailsOpen = true;
        }
    }

    @Override
    public void onEventLongPress(WeekViewEvent event, RectF eventRect) {
    }

    @Override
    public List<WeekViewEvent> onMonthChange(int newYear, int newMonth) {
        /*Checks to see if the month being called is January (1). Not the best way to handle it but
        it works for this case since we only care about 3 days (Jan 16-18, 2015).*/
        if (newMonth == JANUARY_MONTH) {
            //getRemoteEvents(newMonth);
            return finalWeekViewEvents;
        } else {
            /*onMonthChange is called 3 times by the view when it is created. This results in
            duplicate events being created. Thus, I return an empty ArrayList of WeekViewEvent
             objects if the month is not 1 (January).*/
            return new ArrayList<WeekViewEvent>();
        }
    }

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
        getRemoteEvents(JANUARY_MONTH); //Forces a remote Event query.
    }

    public void closeEventDetails() {
        //Close the EventDetailsFragment
        getActivity().getFragmentManager().beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .remove(eventDetailsFragment).commit();
        //Show the toolbar
        ((ActionBarActivity) getActivity()).getSupportActionBar().show();
        eventDetailsOpen = false;
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
