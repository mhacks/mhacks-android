package com.mhacks.android.data.model;

/**
 * Created by Omkar Moghe on 1/21/2016.
 */
public class Map {
    public String imageUrl;
    public double southWestLat;
    public double southWestLon;
    public double northEastLat;
    public double northEastLon;

    public Map() {
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public double getSouthWestLat() {
        return southWestLat;
    }

    public void setSouthWestLat(double southWestLat) {
        this.southWestLat = southWestLat;
    }

    public double getSouthWestLon() {
        return southWestLon;
    }

    public void setSouthWestLon(double southWestLon) {
        this.southWestLon = southWestLon;
    }

    public double getNorthEastLat() {
        return northEastLat;
    }

    public void setNorthEastLat(double northEastLat) {
        this.northEastLat = northEastLat;
    }

    public double getNorthEastLon() {
        return northEastLon;
    }

    public void setNorthEastLon(double northEastLon) {
        this.northEastLon = northEastLon;
    }
}
