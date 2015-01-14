package com.mhacks.android.ui.nav;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mhacks.android.data.model.Award;
import com.mhacks.iv.android.R;
import com.parse.GetDataCallback;
import com.parse.ParseFile;

import java.text.ParseException;
import java.util.List;

/**
 * Created by ash on 11/12/14.
 */
public class CustomGrid extends BaseAdapter {

    private Context mContext;
    private List<Award> awardList;
    private View grid;

    public CustomGrid(Context c, List<Award> awardList) {
        mContext = c;
        this.awardList = awardList;
        Log.d("CustomGrid", "awardList size in CustomGrid constructor = " + this.awardList.size());
    }

    @Override
    public int getCount() {
        Log.d("CustomGrid", "awardList size = " + awardList.size());
        return awardList.size();
    }

    @Override
    public Object getItem(int i) {
        return awardList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            grid = inflater.inflate(R.layout.award_grid_item, null);
            TextView titleTextView = (TextView) grid.findViewById(R.id.titleTextView);
            Log.d("CustomGrid", "Position " + position + ": title " + awardList.get(position).getTitle());
            titleTextView.setText(awardList.get(position).getTitle());
            TextView prizeTextView = (TextView) grid.findViewById(R.id.prizeTextView);
            Log.d("CustomGrid", "Position " + position + ": prize " + awardList.get(position).getPrize());
            prizeTextView.setText(awardList.get(position).getPrize());
            ParseFile logo = awardList.get(position).getSponsor().getLogo();
            logo.getDataInBackground(new GetDataCallback() {
                @Override
                public void done(byte[] bitmapdata, com.parse.ParseException e) {
                    ImageView sponsorImageView = (ImageView) grid.findViewById(R.id.sponsorImageView);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapdata, 0, bitmapdata.length);
                    sponsorImageView.setImageBitmap(bitmap);
                }
            });
        }
        else {
            grid = convertView;
        }
        return grid;
    }

}