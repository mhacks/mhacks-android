package com.mhacks.android.data.model;

import java.util.List;

/**
 * Created by Omkar Moghe on 2/4/2016.
 *
 * ONLY USED FOR GETTING THE LIST OF EVENTS FROM GSON'S AUTOMATIC DESERIALIZATION
 */
public class EventList {

    public List<Event> results;

    public EventList() {
    }

    public List<Event> getResults() {
        return results;
    }

    public void setResults(List<Event> results) {
        this.results = results;
    }
}
