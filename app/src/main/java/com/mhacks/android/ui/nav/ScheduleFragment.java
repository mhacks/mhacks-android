package com.mhacks.android.ui.nav;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.Toast;
import com.mhacks.iv.android.R;
import java.util.Calendar;


/**
 * Created by Omkar Moghe on 10/25/2014.
 */
public class ScheduleFragment extends Fragment implements ActionBar.TabListener{

    private View         mScheduleFragView;
    private CalendarView scheduleCalendarView;
    private ActionBar actionBar;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        mScheduleFragView = inflater.inflate(R.layout.fragment_schedule, container, false);

        actionBar = getActivity().getActionBar();
        if (actionBar != null) {
            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
            actionBar.addTab(actionBar.newTab().setText("Fri").setTabListener(this));
            actionBar.addTab(actionBar.newTab().setText("Sat").setTabListener(this));
            actionBar.addTab(actionBar.newTab().setText("Sun").setTabListener(this));
        }

/*
        Calendar calander = Calendar.getInstance();
        scheduleCalendarView =
                (CalendarView) mScheduleFragView.findViewById(R.id.schedule_calendar);
        scheduleCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView calendarView, int year, int month, int day) {
                Toast.makeText(getActivity(), month + "/" + day + "/" + year, Toast.LENGTH_SHORT).show();
            }
        });
*/

        return mScheduleFragView;
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }
}
