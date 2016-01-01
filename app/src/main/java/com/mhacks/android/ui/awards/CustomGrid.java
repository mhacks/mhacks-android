package com.mhacks.android.ui.awards;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mhacks.android.data_old.model.Award;
import org.mhacks.android.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by ash on 11/12/14.
 */
public class CustomGrid extends BaseAdapter {

    private final static int NUM_COLORS_TO_KEEP = 3;

    private Context mContext;
    private List<Award> mAwardList;
    private Random mRand;
    private ArrayList<Integer> mColors;

    public CustomGrid(Context context, List<Award> awardList) {
        mContext = context;
        mAwardList = awardList;
        mRand = new Random();
        mColors = new ArrayList<Integer>();

        mColors.add(R.color.event_red);
        mColors.add(R.color.event_orange);
        mColors.add(R.color.event_green);
        mColors.add(R.color.event_blue);
        mColors.add(R.color.mh_purple);
    }

    @Override
    public int getCount() {
        return mAwardList.size();
    }

    @Override
    public Object getItem(int i) {
        return mAwardList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.award_grid_item, null);
            view.setMinimumHeight(350);
        } else {
            view = convertView;
        }

        TextView titleTextView = (TextView) view.findViewById(R.id.titleTextView);
        titleTextView.setText(mAwardList.get(position).getTitle());

        TextView prizeTextView = (TextView) view.findViewById(R.id.prizeTextView);
        prizeTextView.setText(mAwardList.get(position).getPrize());

        view.setBackgroundColor(getColor(position));

        return view;
    }

    private int getColor(int position) {
        int color = mContext.getResources().getColor(this.mColors.get(position % this.mColors.size()));
        return 0xCC000000 | (color & 0x00FFFFFF);
    }
}