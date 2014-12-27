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
import com.mhacks.android.data.model.Location;
import com.mhacks.android.data.model.Sponsor;
import com.mhacks.iv.android.R;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

/**
 * Created by Omkar Moghe on 12/25/2014.
 */
public class EventDetailsFragment extends Fragment {

    private static final String TAG = "EventDetailsFragment";

    private View mEventDetailsFragView;

    private TextView eventTitle, eventTime, eventLocation, eventDescription, eventHost;
    private View colorBlock;

    public static EventDetailsFragment newInstance(Event event, int color) {
        EventDetailsFragment f = new EventDetailsFragment();

        Bundle args = new Bundle();
        args.putParcelable("event", event);
        args.putInt("color", color);
        f.setArguments(args);

        return f;
    }

    public Event getEvent () {
        return getArguments().getParcelable("event");
    }

    public int getColor () {
        return getArguments().getInt("color");
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

        //Instantiate color header block
        colorBlock = mEventDetailsFragView.findViewById(R.id.header_color_block);
        colorBlock.setBackgroundColor(getColor());

        setEventDetails();

        return mEventDetailsFragView;
    }

    public void setEventDetails() {
        Event event = getEvent();

        eventTitle.setText(event.getTitle());
        eventTime.setText(formatDate(event.getStartTime().toString()));
        //TODO get locations and display the location.
        eventDescription.setText(event.getDetails());

        //Null pointer check for Sponsor.
        if (event.getHost() != null)
            eventHost.setText(event.getHost().getName());
        else
            eventHost.setText("The MHacks Team <3");
    }

    public String formatDate (String rawDate) {
        String finalDate;
        String[] splitString = rawDate.split(" ");
        finalDate = splitString[0] + ", " + splitString[1] + " " + splitString[2] + ", "
                    + splitString[5] + "\n" + splitString[3];

        return finalDate;
    }
}
