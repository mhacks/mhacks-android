package com.mhacks.android.data.model;

/**
 * Created by boztalay on 6/3/15.
 * Updated by omkarmoghe on 10/2/16.
 */
public class Location extends ModelObject{
    public String name;
    public double latitude;
    public double longitude;
    public String floor;

    public Location() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }
}
