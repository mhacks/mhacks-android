package com.mhacks.android.data.model;

import java.util.List;

/**
 * Created by Omkar Moghe on 10/5/2016.
 */

public class Scan extends ModelObject {
    public boolean        scanned;
    public List<ScanData> data;

    public Scan() {
    }

    public boolean isScanned() {
        return scanned;
    }

    public void setScanned(boolean scanned) {
        this.scanned = scanned;
    }

    public List<ScanData> getData() {
        return data;
    }

    public void setData(List<ScanData> data) {
        this.data = data;
    }
}
