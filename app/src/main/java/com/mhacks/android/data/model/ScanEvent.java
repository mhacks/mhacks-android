package com.mhacks.android.data.model;

/**
 * Created by Omkar Moghe on 10/3/2016.
 */

public class ScanEvent extends ModelObject {
    public String name;
    public long   expiryDate;

    public ScanEvent() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(long expiryDate) {
        this.expiryDate = expiryDate;
    }
}
