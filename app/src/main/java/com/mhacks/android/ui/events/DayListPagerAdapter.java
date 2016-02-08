package com.mhacks.android.ui.events;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Omkar Moghe on 2/8/2016.
 */
public class DayListPagerAdapter extends FragmentPagerAdapter {

    public DayListPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
