package com.mhacks.android.data.model;

import java.util.Date;

/**
 * Created by boztalay on 6/3/15.
 */
public class Announcement extends ModelObject {
    public String name;
    public String info;
    public Date broadcastTime;
    public int role;
    public int hackathon_id;

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

    public void setBroadcastTime(Date broadcastTime) {
        this.broadcastTime = broadcastTime;
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
}
