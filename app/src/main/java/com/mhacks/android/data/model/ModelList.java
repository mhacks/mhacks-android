package com.mhacks.android.data.model;

import java.util.Date;
import java.util.List;

/**
 * Created by Omkar Moghe on 10/2/2016.
 */

public class ModelList<T extends ModelObject> {
    public long    dateUpdated;
    public List<T> results;

    public ModelList() {
    }

    public long getDateUpated() {
        return dateUpdated;
    }

    public void setDateUpated(long dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

    public List<T> getResults() {
        return results;
    }

    public void setResults(List<T> results) {
        this.results = results;
    }
}
