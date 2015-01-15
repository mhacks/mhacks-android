package com.mhacks.android.ui.nav;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import com.mhacks.android.data.model.Award;
import com.mhacks.android.ui.MainActivity;
import com.mhacks.iv.android.R;
import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Omkar Moghe on 10/25/2014.
 */
public class AwardsFragment extends Fragment {

    public static final String TAG = "AwardsFragment";
    public static final String AWARD_PIN = "awardPin";

    private View mAwardsFragView;
    private List<Award> mAwardList;
    private CustomGrid mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        mAwardsFragView = inflater.inflate(R.layout.fragment_awards, container, false);
        mAwardList = new ArrayList<>();

        setUpGridView();
        getLatestParseData();

        return mAwardsFragView;
    }

    private void setUpGridView() {
        GridView gridView = (GridView) mAwardsFragView.findViewById(R.id.gridView);
        mAdapter = new CustomGrid(mAwardsFragView.getContext(), mAwardList);
        gridView.setAdapter(mAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(mAwardsFragView.getContext(), R.style.Base_Theme_AppCompat_Light_Dialog));
                LayoutInflater layoutInflater = getActivity().getLayoutInflater();
                final View dialogView = layoutInflater.inflate(R.layout.award_dialog, null);
                builder.setView(dialogView);
                builder.setTitle(mAwardList.get(position).getTitle());
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();

                ParseFile logo = mAwardList.get(position).getSponsor().getLogo();
                logo.getDataInBackground(new GetDataCallback() {
                    @Override
                    public void done(byte[] bitmapdata, com.parse.ParseException e) {
                        ImageView sponsorImageView = (ImageView) dialogView.findViewById(R.id.sponsorImageView);
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapdata, 0, bitmapdata.length);
                        sponsorImageView.setImageBitmap(bitmap);
                    }
                });

                TextView prizeTextView = (TextView) dialogView.findViewById(R.id.prizeTextView);
                prizeTextView.setText(mAwardList.get(position).getPrize());

                TextView descriptionTextView = (TextView) dialogView.findViewById(R.id.descriptionTextView);
                descriptionTextView.setText(mAwardList.get(position).getDescription());

                dialog.show();
            }
        });
    }

    private ParseQuery<ParseObject> getBaseQuery() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Award");
        query.include("sponsor");
        query.addDescendingOrder("value");
        return query;
    }

    private void getLatestParseData() {
        getLocalParseData();
    }

    private void getLocalParseData() {
        ParseQuery<ParseObject> query = getBaseQuery();
        query.fromPin(AWARD_PIN);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objectList, ParseException e) {
                if(e != null || objectList == null || objectList.size() <= 0) {
                    Log.e(TAG, "Couldn't get the local awards, falling back on remote");
                } else {
                    Log.d(TAG, "Got the local awards, displaying them then updating with remote");
                    processAwardsList(objectList);
                }

                getRemoteParseData();
            }
        });
    }

    private void getRemoteParseData() {
        ParseQuery<ParseObject> query = getBaseQuery();
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objectList, ParseException e) {
                if(e != null || objectList == null || objectList.size() <= 0) {
                    Log.e(TAG, "Couldn't get the remote awards");

                    // We don't have anything locally, let the user know we need internet
                    if(mAwardList == null || mAwardList.size() <= 0) {
                        if(getActivity() != null) ((MainActivity)getActivity()).showNoInternetOverlay();
                    } else {
                        // We do have local stuff, no make sure we aren't in the way
                        if(getActivity() != null) ((MainActivity)getActivity()).hideNoInternetOverlay();
                    }
                } else {
                    Log.d(TAG, "Got the remote awards, displaying them");

                    // We got the remote awards, so unpin the old ones and pin the new ones
                    ParseObject.unpinAllInBackground(AWARD_PIN);
                    ParseObject.pinAllInBackground(AWARD_PIN, objectList);

                    // Display the new awards and get outta the way
                    processAwardsList(objectList);
                    if(getActivity() != null) ((MainActivity)getActivity()).hideNoInternetOverlay();
                }
            }
        });
    }

    private void processAwardsList(List<ParseObject> awardsList) {
        // Translate it into the actual list of awards
        mAwardList.clear();
        for (ParseObject parseObject : awardsList) {
            Award award = (Award) parseObject;
            mAwardList.add(award);
        }

        mAdapter.notifyDataSetChanged();
    }
}
