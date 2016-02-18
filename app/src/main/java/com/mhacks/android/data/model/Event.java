package com.mhacks.android.data.model;

import java.util.Date;

/**
 * Created by boztalay on 6/3/15.
 */
public class Event extends ModelObject {
    public String   name;
    public String   info;
    public String[] locationIds;
    public String locationName;
    public Date     startTime;
    public Date     endTime;
    public String   userId;

    /*
    0 - Logistics (blue)
        (opening ceremony, expo, buses etc.)
    1 - Social (red)
    2 - Food (maize mother fucker)
    3 - Tech Talk (purple)
    4 - Other (brown)
     */
    public int      category;
    public boolean  isApproved;

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

    public String[] getLocationIds() {
        return locationIds;
    }

    public void setLocationIds(String[] locationIds) {
        this.locationIds = locationIds;
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
        return isApproved;
    }

    public void setApproved(boolean approved) {
        this.isApproved = approved;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }
}
