package com.mhacks.android.ui.nav;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mhacks.iv.android.R;

/**
 * Created by ash on 11/12/14.
 */
public class CustomGrid extends BaseAdapter {

    private Context mContext;
    private String[] descriptions;
    private String[] prizes;
    private String[] values;
    private String[] titles;

    public CustomGrid(Context c, String[] descriptions, String[] prizes, String[] values, String[] titles) {
        mContext = c;
        this.descriptions = descriptions;
        this.prizes = prizes;
        this.values = values;
        this.titles = titles;
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View grid;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            grid = inflater.inflate(R.layout.award_grid_item, null);
            TextView titleTextView = (TextView) grid.findViewById(R.id.titleTextView);
            titleTextView.setText(titles[position]);
            TextView prizeTextView = (TextView) grid.findViewById(R.id.prizeTextView);
            prizeTextView.setText(prizes[position]);
            TextView valueTextView = (TextView) grid.findViewById(R.id.valueTextView);
            valueTextView.setText(values[position]);
            TextView descriptionTextView = (TextView) grid.findViewById(R.id.descriptionTextView);
            descriptionTextView.setText(descriptions[position]);
        }
        else {
            grid = convertView;
        }
        return grid;
    }

}