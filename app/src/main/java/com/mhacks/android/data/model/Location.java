package com.mhacks.android.data.model;

/**
 * Created by boztalay on 6/3/15.
 * Updated by omkarmoghe on 3/19/17.
 */
public class Location extends ModelObject {
    public String name;
    public String floor;
    public double latitude;
    public double longitude;

    public Location() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
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
}
