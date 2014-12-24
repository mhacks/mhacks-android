package com.mhacks.android.ui.nav;

import android.app.Fragment;
import android.graphics.Color;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;
import com.mhacks.android.data.model.Event;
import com.mhacks.android.ui.weekview.WeekViewModified;
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
public class ScheduleFragment extends Fragment implements WeekViewModified.EventClickListener,
                                                          WeekViewModified.EventLongPressListener,
                                                          WeekViewModified.MonthChangeListener {

    public static final String TAG = "ScheduleFragment";

    private View             mScheduleFragView;
    private WeekViewModified mWeekView;
    private LinearLayout mScheduleContainer;

    private ParseUser mUser;

    private List<WeekViewEvent> finalWeekViewEvents;
    private List<Event> finalEvents;

    private boolean firstRun = true;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        mScheduleFragView = inflater.inflate(R.layout.fragment_schedule, container, false);

        mUser = ParseUser.getCurrentUser();

        getEvents(Calendar.JANUARY);

        return mScheduleFragView;
    }

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
        mWeekView.setEventTextColor(Color.BLACK);
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

        //Add the view to the LinearLayout
        mScheduleContainer.addView(mWeekView);
    }

    /*
    Queries events from Parse and assigns the list to the global List<Event> events.
     */
    public void getEvents(final int newMonth) {
        ParseQuery<Event> query = ParseQuery.getQuery("Event");
        query.findInBackground(new FindCallback<Event>() {
            public void done(List<Event> eventList, ParseException e) {
                if (e == null) {
                    createEvents(eventList, newMonth);
                } else {
                    Toast.makeText(getActivity(), "No events found.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void createEvents(List<Event> eventList, int newMonth) {
        finalWeekViewEvents = new ArrayList<WeekViewEvent>();
        finalEvents = new ArrayList<Event>(eventList);

        //Id for the events. Doubles as the position of the event in both lists.
        long id = 0;
        for(Event event : eventList) {
            //Create start event.
            Calendar startTime = Calendar.getInstance();
            startTime.setTime(event.getStartTime());

            //Create end event.
            Calendar endTime = (Calendar) startTime.clone();
            int hourDuration = event.getDuration() / 3600;      //getDuration returns seconds as an int. Need to convert to hours.
            int minuteDuration = event.getDuration() % 3600;    //Converting remainder of minutes to int minutes.
            endTime.add(Calendar.HOUR, hourDuration);
            endTime.add(Calendar.MINUTE, minuteDuration);

            //Create a WeekViewEvent using the startTime and endTime
            WeekViewEvent weekViewEvent = new WeekViewEvent(id, event.getTitle(), startTime, endTime);
            //TODO get color for the event using event.getColor() and select from the list of colors.
            weekViewEvent.setColor(getResources().getColor(R.color.palette_3));

            //Add the WeekViewEvent to the list.
            finalWeekViewEvents.add(weekViewEvent);

            //Increment the id
            id++;
        }
        //Sets boolean to true when all WeekViewEvent objects have been created.
        if (firstRun) {
            setUpWeekView();
            firstRun = false;
        }
    }

    @Override
    public void onEventClick(WeekViewEvent event, RectF eventRect) {
        Toast.makeText(getActivity(), event.getName() + " - " + event.getId(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEventLongPress(WeekViewEvent event, RectF eventRect) {
    }


    @Override
    public List<WeekViewEvent> onMonthChange(int newYear, int newMonth) {
        Log.d(TAG, finalWeekViewEvents.size() + " events" + newMonth);
        if (newMonth == 1) {
            getEvents(newMonth);
            return finalWeekViewEvents;
        } else {
            return new ArrayList<WeekViewEvent>();
        }
    }
}
