package com.mhacks.android.ui.common.parse;

import android.view.View;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Damian Wieczorek <damianw@umich.edu> on 8/21/14.
 */
public class ViewHolder {

    public final View root;
    private final Map<Integer, View> mMap = new ConcurrentHashMap<>();

    ViewHolder(View root) {
        this.root = root;
        this.root.setTag(this);
    }

    public static ViewHolder from(View view) {
        ViewHolder holder = (ViewHolder) view.getTag();
        if (holder == null) {
            view.setTag(holder = new ViewHolder(view));
        }
        return holder;
    }

    @SuppressWarnings("unchecked")
    public <T extends View> T get(int id) {
        if (!mMap.containsKey(id)) {
            mMap.put(id, root.findViewById(id));
        }
        return (T) mMap.get(id);
    }
}
