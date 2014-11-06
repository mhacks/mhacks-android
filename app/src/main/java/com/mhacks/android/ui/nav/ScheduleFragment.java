package com.mhacks.android.ui.nav;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import com.mhacks.android.data.model.Event;
import com.mhacks.iv.android.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.Calendar;
import java.util.List;


/**
 * Created by Omkar Moghe on 10/25/2014.
 */
public class ScheduleFragment extends Fragment implements ActionBar.TabListener{
    public static final String TAG = "ScheduleFragment";

    private View         mScheduleFragView;
    private CalendarView scheduleCalendarView;
    private ActionBar actionBar;

    private ParseUser mUser;

    private List<Event> events;

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
                    printEvents(events);
                } else {
                    Toast.makeText(getActivity(), "No events found.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    /*
    FOR TESTING ONLY.
     */
    private void printEvents(List<Event> events) {
        String eventsString = "";
        TextView eventsView = (TextView) mScheduleFragView.findViewById(R.id.events_view);
        for(Event event : events) {
            eventsString = eventsString + event.getTitle() + "\n";
        }
        eventsView.setText(eventsString);
    }
}
