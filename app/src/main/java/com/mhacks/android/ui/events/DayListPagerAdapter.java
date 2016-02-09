package com.mhacks.android.ui.events;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.mhacks.android.data.model.Event;

import java.util.ArrayList;

/**
 * Created by Omkar Moghe on 2/8/2016.
 */
public class DayListPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<Event> friday;
    private ArrayList<Event> saturday;
    private ArrayList<Event> sunday;

    public DayListPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void setEvents(ArrayList<Event> friday, ArrayList<Event> saturday, ArrayList<Event> sunday) {
        this.friday = friday;
        this.saturday = saturday;
        this.sunday = sunday;
    }

    @Override
    public Fragment getItem(int position) {
        DayListFragment dayListFragment = new DayListFragment();

        switch (position) {
            case 0:
                dayListFragment.setEvents(friday);
                break;
            case 1:
                dayListFragment.setEvents(saturday);
                break;
            case 2:
                dayListFragment.setEvents(sunday);
                break;
        }

        return dayListFragment;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
