package com.mhacks.android.data.model;

import java.util.Date;

/**
 * Created by boztalay on 6/3/15.
 */
public class Announcement extends ModelObject {
    public String title;
    public String info;
    public Date broadcastTime;
    /*
    Using bits:
    1 - Emergency (red)
    2- Logistics (blue)
    4 - Food (maize mother fucker)
    8 - Swag (green)
    16 - Sponsor (purple)
    32 - Other (brown)
     */
    public int category;
    public String ownerId;
    public boolean approved;

    public Announcement() {
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

    public Date getBroadcastTime() {
        return broadcastTime;
    }

    public void setBroadcastTime(Date broadcastTime) {
        this.broadcastTime = broadcastTime;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }
}
