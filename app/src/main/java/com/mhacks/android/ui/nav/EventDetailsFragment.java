package com.mhacks.android.ui.nav;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alamkanak.weekview.WeekViewEvent;
import com.mhacks.android.data.model.Event;
import com.mhacks.android.data.model.Sponsor;
import com.mhacks.iv.android.R;

import java.util.List;

/**
 * Created by Omkar Moghe on 12/25/2014.
 */
public class EventDetailsFragment extends Fragment {
    private View mEventDetailsFragView;

    private TextView eventTitle, eventTime, eventLocation, eventDescription, eventHost;

    public static EventDetailsFragment newInstance(Event event) {
        EventDetailsFragment f = new EventDetailsFragment();

        Bundle args = new Bundle();
        args.putParcelable("event", event);

        f.setArguments(args);

        return f;
    }

    public Event getEvent () {
        return getArguments().getParcelable("event");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        mEventDetailsFragView = inflater.inflate(R.layout.fragment_event_details, container, false);

        //Instantiate TextViews
        eventTitle = (TextView) mEventDetailsFragView.findViewById(R.id.event_title);
        eventTime = (TextView) mEventDetailsFragView.findViewById(R.id.details_time);
        eventLocation = (TextView) mEventDetailsFragView.findViewById(R.id.details_location);
        eventDescription = (TextView) mEventDetailsFragView.findViewById(R.id.details_description);
        eventHost = (TextView) mEventDetailsFragView.findViewById(R.id.details_host);

        setEventDetails();

        return mEventDetailsFragView;
    }

    public void setEventDetails() {
        Event event = getEvent();

        eventTitle.setText(event.getTitle());
        eventTime.setText(event.getStartTime().toString());
        //TODO get location and update the textview
        eventDescription.setText(event.getDetails());
        //eventHost.setText(event.getHost().getName());
    }
}
