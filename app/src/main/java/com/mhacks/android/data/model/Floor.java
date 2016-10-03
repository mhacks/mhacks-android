package com.mhacks.android.data.model;

import java.net.URL;

/**
 * Created by Omkar Moghe on 10/2/2016.
 */

public class Floor extends ModelObject {
    public String name;
    public URL image;
    public int index;

    public Floor() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public URL getImage() {
        return image;
    }

    public void setImage(URL image) {
        this.image = image;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
