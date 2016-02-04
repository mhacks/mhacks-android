package com.mhacks.android.data.model;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by boztalay on 6/3/15.
 */
public class Announcement extends ModelObject {
    private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";


    public String  name;
    public String  info;
    public Date    broadcastTime;

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
    public String  userId;
    public boolean isApproved;

    public Announcement() {
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

    public Date getBroadcastTime() {
        return broadcastTime;
    }

    public void setBroadcastTime(String date) {
        DateFormat df = new SimpleDateFormat(DATE_FORMAT);
        try {
        this.broadcastTime = df.parse(date);
        } catch (ParseException p) {
            Log.e("Announcment", "shit", p);
        }
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isApproved() {
        return isApproved;
    }

    public void setApproved(boolean approved) {
        this.isApproved = approved;
    }
}
