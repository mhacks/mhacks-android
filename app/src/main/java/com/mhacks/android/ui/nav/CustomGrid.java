package com.mhacks.android.ui.nav;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mhacks.android.data.model.Award;
import com.mhacks.iv.android.R;

import java.util.List;

/**
 * Created by ash on 11/12/14.
 */
public class CustomGrid extends BaseAdapter {

    private Context mContext;
    private List<Award> awardList;

    public CustomGrid(Context c, List<Award> awardList) {
        mContext = c;
        this.awardList = awardList;
    }

    @Override
    public int getCount() {
        return awardList.size();
    }

    @Override
    public Object getItem(int i) {
        return awardList.get(i);
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
            Log.d("CustomGrid", "Position " + position + ": title " + awardList.get(position).getTitle());
            titleTextView.setText(awardList.get(position).getTitle());
            TextView prizeTextView = (TextView) grid.findViewById(R.id.prizeTextView);
            Log.d("CustomGrid", "Position " + position + ": prize " + awardList.get(position).getPrize());
            prizeTextView.setText(awardList.get(position).getPrize());
            TextView valueTextView = (TextView) grid.findViewById(R.id.valueTextView);
            Log.d("CustomGrid", "Position " + position + ": value " + awardList.get(position).getValue());
            valueTextView.setText(awardList.get(position).getValue());
            TextView descriptionTextView = (TextView) grid.findViewById(R.id.descriptionTextView);
            Log.d("CustomGrid", "Position " + position + ": description " + awardList.get(position).getDescription());
            descriptionTextView.setText(awardList.get(position).getDescription());
        }
        else {
            grid = convertView;
        }
        return grid;
    }

}