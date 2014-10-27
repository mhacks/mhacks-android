package com.mhacks.android.ui.nav;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mhacks.iv.android.R;

/**
 * Created by Omkar Moghe on 10/22/2014.
 */
public class NavigationDrawerFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "NavigationDrawerFragment";

    private NavigationDrawerCallbacks mCallbacks;
    private int                       mCurrentSelectedPosition;
    private ActionBarDrawerToggle     mDrawerToggle;

    private View mNavDrawerView;

    private TextView mAnnouncementsTextView, mScheduleTextView, mSponsorsTextView, mAwardsTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        mNavDrawerView = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);

        mAnnouncementsTextView =
                (TextView) mNavDrawerView.findViewById(R.id.nav_drawer_announcements);
        mScheduleTextView = (TextView) mNavDrawerView.findViewById(R.id.nav_drawer_schedule);
        mSponsorsTextView = (TextView) mNavDrawerView.findViewById(R.id.nav_drawer_sponsors);
        mAwardsTextView = (TextView) mNavDrawerView.findViewById(R.id.nav_drawer_awards);

        mAnnouncementsTextView.setOnClickListener(this);
        mScheduleTextView.setOnClickListener(this);
        mSponsorsTextView.setOnClickListener(this);
        mAwardsTextView.setOnClickListener(this);

        return mNavDrawerView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.nav_drawer_announcements:
                setPosition(0);
                break;
            case R.id.nav_drawer_schedule:
                setPosition(1);
                break;
            case R.id.nav_drawer_sponsors:
                setPosition(2);
                break;
            case R.id.nav_drawer_awards:
                setPosition(3);
                break;
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallbacks = (NavigationDrawerCallbacks) activity;
        } catch (ClassCastException e) {
            Log.e(TAG, "Activity must implement NavigationDrawerCallbacks", e);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    public void setPosition(int position) {
        mCurrentSelectedPosition = position;
        if (mCallbacks != null) {
            mCallbacks.onNavigationDrawerItemSelected(position);
        }
    }

    public static interface NavigationDrawerCallbacks {
        void onNavigationDrawerItemSelected(int position);
    }
}
