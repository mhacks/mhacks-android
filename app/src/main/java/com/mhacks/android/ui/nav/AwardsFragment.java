package com.mhacks.android.ui.nav;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.mhacks.iv.android.R;

/**
 * Created by Omkar Moghe on 10/25/2014.
 */
public class AwardsFragment extends Fragment{

    private View mAwardsFragView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        mAwardsFragView = inflater.inflate(R.layout.fragment_awards, container, false);

        //Put code for instantiating views, etc here. (before the return statement.)

        return mAwardsFragView;
    }

    private class CustomGrid extends BaseAdapter {

        private Context mContext;

        public CustomGrid(Context c) {
            mContext = c;
        }

        @Override
        public int getCount() {
            return 0;
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
                grid = new View(mContext);
            }
            grid = inflater.inflate(R.layout.award_grid_item, null);
        }

    }
}
