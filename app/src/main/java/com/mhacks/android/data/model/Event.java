package com.mhacks.android.data.model;

import java.util.Date;

/**
 * Created by boztalay on 6/3/15.
 */
public class Event extends ModelObject {
    public String title;
    public String info;
    public String[] locations;
    public Date start_time;
    public Date end_time;
    /*
    0 - Logistics (blue)
        (opening ceremony, expo, buses etc.)
    1 - Social (red)
    2 - Food (maize mother fucker)
    3 - Tech Talk (purple)
    4 - Other (brown)
     */
    public int category;
    public boolean approved;

    public Event() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String[] getLocations() {
        return locations;
    }

    public void setLocations(String[] locations) {
        this.locations = locations;
    }

    public Date getStart_time() {
        return start_time;
    }

    public void setStart_time(Date start_time) {
        this.start_time = start_time;
    }

    public Date getEnd_time() {
        return end_time;
    }

    public void setEnd_time(Date end_time) {
        this.end_time = end_time;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }
}
