package com.mhacks.android.data.model;

/**
 * Created by boztalay on 6/3/15.
 * Modified by omkarmoghe on 10/3/16
 */
public class Announcement extends ModelObject {
    public String title;
    public String info;
    public long   broadcastAt;

    /*
    Using bits:
    1 - Emergency (red)
    2- Logistics (blue)
    4 - Food (maize mother fucker)
    8 - Swag (green)
    16 - Sponsor (purple)
    32 - Other (brown)
    */
    public int     category;
    public boolean approved;
    public boolean deleted;

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

    public long getBroadcastAt() {
        return broadcastAt;
    }

    public void setBroadcastAt(long date) {
        this.broadcastAt = date;
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

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
