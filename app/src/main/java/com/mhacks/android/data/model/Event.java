package com.mhacks.android.data.model;

import java.util.Date;

/**
 * Created by boztalay on 6/3/15.
 */
public class Event extends ModelObject {
    public String   title;
    public String   info;
    public String[] locations;
    public Date     startTime;
    public Date     endTime;
    /*
    0 - Logistics (blue)
        (opening ceremony, expo, buses etc.)
    1 - Social (red)
    2 - Food (maize mother fucker)
    3 - Tech Talk (purple)
    4 - Other (brown)
     */
    public int      category;
    public boolean  approved;

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

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
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
