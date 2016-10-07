package com.mhacks.android.data.model;

import java.net.URL;

/**
 * Created by Omkar Moghe on 10/2/2016.
 */

public class Floor extends ModelObject {
    public String name;
    public String image;
    public int index;
    public String description;

    public Floor() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
