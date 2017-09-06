package com.mhacks.android.data.model;

import java.util.List;

/**
 * Created by boztalay on 6/3/15.
 * Updated by omkarmoghe on 10/2/16.
 */
public class Event extends ModelObject {
    public String   name;
    public String   info;
    public long     start;
    public long     duration;
    public List<String> locations;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public long getStart() {
        return start * 1000;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public List<String> getLocations() {
        return locations;
    }

    public void setLocations(List<String> locations) {
        this.locations = locations;
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
