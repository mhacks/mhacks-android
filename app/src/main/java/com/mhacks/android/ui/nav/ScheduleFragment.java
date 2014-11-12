package com.mhacks.android.ui.nav;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.Toast;

import com.alamkanak.weekview.WeekView;
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
                                                          WeekView.EventClickListener,
                                                          WeekView.EventLongPressListener,
                                                          WeekView.MonthChangeListener{

    public static final String TAG = "ScheduleFragment";

    private View      mScheduleFragView;
    private WeekView  mWeekView;
    private ActionBar actionBar;

    private ParseUser mUser;

    private List<Event> events;
    private List<WeekViewEvent> finalEvents;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        mScheduleFragView = inflater.inflate(R.layout.fragment_schedule, container, false);

        actionBar = getActivity().getActionBar();

        // Adds tabs to the action bar. Not sure if this is the best way to do that.
        if (actionBar != null) {
            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
            if (actionBar.getTabCount() != 3) {
                actionBar.removeAllTabs();
                actionBar.addTab(actionBar.newTab().setText("Fri").setTabListener(this));
                actionBar.addTab(actionBar.newTab().setText("Sat").setTabListener(this));
                actionBar.addTab(actionBar.newTab().setText("Sun").setTabListener(this));
            }
        }

        mUser = ParseUser.getCurrentUser();

        // Week View set up.
        mWeekView = (WeekView) mScheduleFragView.findViewById(R.id.week_view);
        mWeekView.setOnEventClickListener(this);
        mWeekView.setEventLongPressListener(this);
        mWeekView.setMonthChangeListener(this);
        mWeekView.setBackgroundColor(Color.WHITE);

        // List of events to be displayed on the WeekView
        finalEvents = new ArrayList<WeekViewEvent>();

        getEvents();

        return mScheduleFragView;
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
                } else {
                    Toast.makeText(getActivity(), "No events found.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void createEvents(List<Event> events) {
        for(Event event : events) {
            Calendar startTime = Calendar.getInstance();
            startTime.setTime(event.getStartTime());
            //LOL don't look at the id code its sketch.
            long id = (event.getObjectId().charAt(0) + event.getObjectId().charAt(1) + event.getObjectId().charAt(2)) * event.getObjectId().charAt(3);
            WeekViewEvent weekViewEvent = new WeekViewEvent(id, event.getTitle(), startTime, startTime);
            weekViewEvent.setColor(getResources().getColor(R.color.palette_2));
            finalEvents.add(weekViewEvent);
        }
    }

    @Override
    public void onEventClick(WeekViewEvent weekViewEvent, RectF rectF) {
    }

    @Override
    public void onEventLongPress(WeekViewEvent weekViewEvent, RectF rectF) {
    }

    @Override
    public List<WeekViewEvent> onMonthChange(int newYear, int newMonth) {
        getEvents();
        return finalEvents;
    }


}
