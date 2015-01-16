package com.mhacks.android.ui.nav;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mhacks.android.data.model.Award;
import com.mhacks.iv.android.R;

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
    private ArrayList<Integer> mLastColors;

    public CustomGrid(Context context, List<Award> awardList) {
        mContext = context;
        mAwardList = awardList;
        mRand = new Random();
        mLastColors = new ArrayList<Integer>();
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

        view.setBackgroundColor(getRandomColor());

        return view;
    }

    private int getRandomColor() {
        int colorIndex;
        int color;

        do {
            colorIndex = mRand.nextInt(5);
        } while(!mLastColors.isEmpty() && mLastColors.contains(colorIndex));

        switch (colorIndex) {
            case 0: //Red
                color = mContext.getResources().getColor(R.color.event_red);
                break;
            case 1: //Orange
                color = mContext.getResources().getColor(R.color.event_orange);
                break;
            case 2: //Yellow
                color = mContext.getResources().getColor(R.color.event_yellow);
                break;
            case 3: //Green
                color = mContext.getResources().getColor(R.color.event_green);
                break;
            default: //Blue
                color = mContext.getResources().getColor(R.color.event_blue);
        }

        if(mLastColors.size() >= NUM_COLORS_TO_KEEP) {
            mLastColors.remove(0);
        }
        mLastColors.add(colorIndex);

        return 0xAA000000 | (color & 0x00FFFFFF);
    }
}