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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Omkar Moghe on 12/25/2014.
 *
 * Uses and Event object to create a detailed view with all the info about the event.
 * Called from the WeekView's onEventClicked listener.
 */
public class EventDetailsFragment extends Fragment {

    private static final String TAG = "EventDetailsFragment";

    //Decalre Views.
    private View mEventDetailsFragView;

    private TextView eventTitle, eventTime, eventLocation, eventDescription, eventHost;
    private View colorBlock; //Header color. Matches color of event in calendar.

    /**
     * Creates a new instance of the EventDetailsFragment.
     * @param event Event to display in detailed view.
     * @param color Color of the event.
     * @return An EventDetailsFragment with the passed Event and color.
     */
    public static EventDetailsFragment newInstance(Event event, int color) {
        EventDetailsFragment f = new EventDetailsFragment();

        Bundle args = new Bundle();
        args.putParcelable("event", event);
        args.putInt("color", color);
        f.setArguments(args);

        return f;
    }

    /**
     * Returns the Event for this EventDetailsView.
     * @return Event that was passed in when newInstance() was called.
     */
    public Event getEvent () {
        return getArguments().getParcelable("event");
    }

    /**
     * Returns the color used to draw this event.
     * @return Color of the event.
     */
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

        //Add correct details to the details view.
        setEventDetails();

        return mEventDetailsFragView;
    }

    /**
     * Method to use the Event object to populate the view using the appropriate info.
     */
    public void setEventDetails() {
        Event event = getEvent();
        ArrayList<Location> locations = getLocations(event);

        eventTitle.setText(event.getTitle());
        eventTime.setText(formatDate(event.getStartTime().toString()));
        eventLocation.setText(locations.get(0).getName());
        eventDescription.setText(event.getDetails());

        //Null pointer check for Sponsor. Null Sponsor == The MHacks Team is hosting the event.
        if (event.getHost() != null)
            eventHost.setText(event.getHost().getName());
        else
            eventHost.setText("The MHacks Team <3");
    }

    /**
     * Takes the raw Date.toString() string and formats it into a more common format.
     * Before:  Sun Jan 18 05:00:00 PST 2015
     * After:   Sun, Jan 18, 2015
     *          05:00:00
     * @param rawDate String to formate into a more readable date.
     * @return Readable string from Date.toString().
     */
    public String formatDate (String rawDate) {
        String finalDate;
        String[] splitString = rawDate.split(" ");
        finalDate = splitString[0] + ", " + splitString[1] + " " + splitString[2] + ", "
                    + splitString[5] + "\n" + splitString[3];

        return finalDate;
    }

    /**
     * Gets all the locations for a given Event based on the objectId's of the location.
     * @param event Event object for which locations are to be queried.
     * @return ArrayList of Location objects for the given Event.
     */
    public ArrayList<Location> getLocations (Event event) {
        JSONArray JSONlocations = event.getLocations(); //Locations stored in Parse as a JSON array.
        ArrayList<Location> locations = new ArrayList<>(); //Array list to be returned.
        try { //JSON Exception
            for (int i = 0; i < JSONlocations.length(); i++) {
                ParseQuery<ParseObject> query = ParseQuery.getQuery("Location");
                try { //ParseException
                    //Using query.get() instead of getInBackground() to force each query before moving on.
                    locations.add((Location) query.get(JSONlocations.getJSONObject(i).getString("objectId")));
                } catch (ParseException p) {
                    Log.e(TAG, "Parse done goofed.", p);
                }
            }
        } catch (JSONException e) {
            Log.e(TAG, "JSON exception", e);
        }

        return locations;
    }
}
