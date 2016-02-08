package com.mhacks.android.ui.events;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.alamkanak.weekview.WeekViewEvent;
import com.mhacks.android.data.model.Event;
import com.mhacks.android.data.network.HackathonCallback;
import com.mhacks.android.data.network.NetworkManager;
import com.mhacks.android.ui.MainActivity;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.mhacks.android.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;


/**
 * Created by Omkar Moghe on 10/25/2014.
 * <p/>
 * Builds schedule with events pulled from the Parse database. Uses the EventDetailsFragment to
 * create event details.
 */
public class ScheduleFragment extends Fragment {

    public static final String TAG = "ScheduleFragment";

    // Declaring Views
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private LinearLayout mScheduleContainer;

    // Event data structures
    private ArrayList<Event>         mEvents;

    // Booleans
    private boolean eventDetailsOpen = false; //Prevents multiple EventDetailFragments from opening.

    // Declares the EventDetailsFragment
    private EventDetailsFragment eventDetailsFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NetworkManager networkManager = NetworkManager.getInstance();
        networkManager.getEvents(new HackathonCallback<List<Event>>() {
            @Override
            public void success(List<Event> response) {
                mEvents = new ArrayList<Event>(response);
                //TODO: build views
            }

            @Override
            public void failure(Throwable error) {
                mEvents = new ArrayList<Event>();
            }
        });
    }

    @Override
    public View onCreateView(final LayoutInflater inflater,
                             final ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schedule, container, false);

        tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        viewPager = (ViewPager) view.findViewById(R.id.view_pager);
        mScheduleContainer = (LinearLayout) view.findViewById(R.id.schedule_container);

        // Add tabs
        tabLayout.addTab(tabLayout.newTab().setText("Fri"));
        tabLayout.addTab(tabLayout.newTab().setText("Sat"));
        tabLayout.addTab(tabLayout.newTab().setText("Sun"));

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //TODO: cancel query
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

    public boolean getEventDetailsOpened () {
        return eventDetailsOpen;
    }

    public void setEventDetailsOpened(Boolean bool) {
        eventDetailsOpen = bool;
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
        //Forces a remote Event query.
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
}
