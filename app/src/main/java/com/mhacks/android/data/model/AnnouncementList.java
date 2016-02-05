package com.mhacks.android.data.model;

import java.util.List;

/**
 * Created by Omkar Moghe on 2/4/2016.
 *
 * ONLY USED FOR GETTING THE LIST OF ANNOUNCEMENTS FROM GSON'S AUTOMATIC DESERIALIZATION
 */
public class AnnouncementList {

    public List<Announcement> results;

    public AnnouncementList() {
    }

    public List<Announcement> getResults() {
        return results;
    }

    public void setResults(List<Announcement> results) {
        this.results = results;
    }
}
