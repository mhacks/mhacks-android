package com.mhacks.app.data.model;

import android.graphics.Color;

/**
 * Created by Omkar Moghe on 10/5/2016.
 */

public class ScanData extends ModelObject {
    public String color;
    public String value;
    public String label;

    public ScanData() {
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getColorHex() {
        return Color.parseColor("#" + getColor());
    }
}
