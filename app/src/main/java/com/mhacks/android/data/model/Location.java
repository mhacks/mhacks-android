package com.mhacks.android.data.model;

/**
 * Created by boztalay on 6/3/15.
 */
public class Location extends ModelObject {
    public String name;
    public String latitude;
    public String longitude;
    public int hackathon_id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public int getHackathon_id() {
        return hackathon_id;
    }

    public void setHackathon_id(int hackathon_id) {
        this.hackathon_id = hackathon_id;
    }
}
