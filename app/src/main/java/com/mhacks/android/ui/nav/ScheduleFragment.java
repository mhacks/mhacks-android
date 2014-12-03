package com.mhacks.android.ui.nav;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.graphics.RectF;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alamkanak.weekview.WeekView;
import com.mhacks.android.ui.weekview.WeekViewModified;
import com.alamkanak.weekview.WeekViewEvent;
import com.mhacks.android.data.model.Event;
import com.mhacks.iv.android.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * Created by Omkar Moghe on 10/25/2014.
 */
public class ScheduleFragment extends Fragment implements ActionBar.TabListener,
                                                          WeekViewModified.EventClickListener,
                                                          WeekViewModified.EventLongPressListener,
                                                          WeekViewModified.MonthChangeListener,
                                                          WeekView.MonthChangeListener {

    public static final String TAG = "ScheduleFragment";

    private View      mScheduleFragView;
    private WeekViewModified  mWeekView;

    private ParseUser mUser;

    private List<Event> events;
    private List<WeekViewEvent> finalEvents;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        mScheduleFragView = inflater.inflate(R.layout.fragment_schedule, container, false);

        mUser = ParseUser.getCurrentUser();

        // Week View set up.
        mWeekView = (WeekViewModified) mScheduleFragView.findViewById(R.id.week_view);
        setUpWeekView();

        // List of events to be displayed on the WeekView
        finalEvents = new ArrayList<WeekViewEvent>();

        getEvents();

        return mScheduleFragView;
    }

    private void setUpWeekView() {
        Calendar today = Calendar.getInstance();
        today.set(Calendar.YEAR, 2015);
        today.set(Calendar.MONTH, Calendar.JANUARY);
        today.set(Calendar.DATE, 16);
        mWeekView.setOnEventClickListener(this);
        mWeekView.setEventLongPressListener(this);
        mWeekView.setMonthChangeListener((WeekView.MonthChangeListener) this);
        mWeekView.setMonthChangeListener((WeekViewModified.MonthChangeListener) this);
        mWeekView.setBackgroundColor(Color.WHITE);
        //mWeekView.setHorizontalScrollEnabled(false);
        mWeekView.setToday(today);
        onMonthChange(today.get(Calendar.YEAR), today.get(Calendar.MONTH));
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        //TODO set scroll to appropriate position on the schedule.
        switch (tab.getPosition()) {
            case 0:
                break;
            case 1:
                break;
            case 2:
                break;
        }
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }

    /*
    Queries events from Parse and assigns the list to the global List<Event> events.
     */
    public void getEvents() {
        ParseQuery<Event> query = ParseQuery.getQuery("Event");
        query.findInBackground(new FindCallback<Event>() {
            public void done(List<Event> eventList, ParseException e) {
                if (e == null) {
                    events = eventList;
                    createEvents(events);
                    Toast.makeText(getActivity(), "" + events.size(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "No events found.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void createEvents(List<Event> events) {
        finalEvents.clear();
        for(Event event : events) {
            //Create start event.
            Calendar startTime = Calendar.getInstance();
            startTime.setTime(event.getStartTime());
            //Create end event.
            Calendar endTime = (Calendar) startTime.clone();
            int hourDuration = event.getDuration() / 3600;      //getDuration returns seconds as an int. Need to convert to hours.
            int minuteDuration = event.getDuration() % 3600;    //Converting remainder of minutes to int minutes.
            endTime.set(Calendar.HOUR_OF_DAY, startTime.get(Calendar.HOUR_OF_DAY) + hourDuration);
            endTime.set(Calendar.MINUTE, startTime.get(Calendar.MINUTE) + minuteDuration);

            //LOL don't look at the id code its sketch.
            long id = (event.getObjectId().charAt(0) + event.getObjectId().charAt(1) + event.getObjectId().charAt(2)) * event.getObjectId().charAt(3);

            //Create a WeekViewEvent using the startTime and endTime
            WeekViewEvent weekViewEvent = new WeekViewEvent(id, event.getTitle(), startTime, endTime);
            //TODO get color for the event using event.getColor() and select from the lsit of colors.
            weekViewEvent.setColor(getResources().getColor(R.color.palette_3));
            finalEvents.add(weekViewEvent);
        }
        onMonthChange(2015, 1);
    }

    @Override
    public void onEventClick(WeekViewEvent event, RectF eventRect) {
    }

    @Override
    public void onEventLongPress(WeekViewEvent event, RectF eventRect) {

    }

    @Override
    public List<WeekViewEvent> onMonthChange(int newYear, int newMonth) {
        return finalEvents;
    }
}
