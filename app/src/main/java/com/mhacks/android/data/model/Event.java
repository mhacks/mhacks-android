package com.mhacks.android.data.model;

import java.util.Date;

/**
 * Created by boztalay on 6/3/15.
 */
public class Event extends ModelObject {

    public String name;
    public String info;
    public Date   startTime;
    public Date   endTime;
    public int    role;
    public int    hackathon_id;
    public int    location_id;

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

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public int getHackathon_id() {
        return hackathon_id;
    }

    public void setHackathon_id(int hackathon_id) {
        this.hackathon_id = hackathon_id;
    }

    public int getLocation_id() {
        return location_id;
    }

    public void setLocation_id(int location_id) {
        this.location_id = location_id;
    }
}
