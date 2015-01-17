package com.mhacks.android.ui.nav;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.mhacks.android.data.model.Award;
import com.mhacks.android.data.model.Sponsor;
import com.mhacks.android.ui.MainActivity;
import org.mhacks.android.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.squareup.picasso.Picasso;

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
    private ParseQuery<Award> currentQuery;

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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(currentQuery != null) currentQuery.cancel();
    }

    private void setUpGridView() {
        GridView gridView = (GridView) mAwardsFragView.findViewById(R.id.gridView);
        mAdapter = new CustomGrid(mAwardsFragView.getContext(), mAwardList);
        gridView.setAdapter(mAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DialogFragment sponsorDialog = new AwardDialog().newInstance(mAwardList.get(position));
                sponsorDialog.show(getFragmentManager(), mAwardList.get(position).getTitle());
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

    public static class AwardDialog extends DialogFragment {
        View mProfile;
        String mImageLocation;
        String mTitle;
        String mPrize;
        String mSponsor;
        String mWebsite;
        String mDesc;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = getActivity().getLayoutInflater();
            mProfile = inflater.inflate(R.layout.award_dialog, null);

            ImageView sponsorImage = (ImageView) mProfile.findViewById(R.id.award_sponsor_pic);
            if (mImageLocation != null) {
                Picasso.with(getActivity()).load(mImageLocation).into(sponsorImage);
            } else {
                sponsorImage.setImageResource(R.drawable.launcher_icon);
            }

            TextView awardTitle = (TextView) mProfile.findViewById(R.id.award_title);
            awardTitle.setText(mTitle);

            TextView awardDesc = (TextView) mProfile.findViewById(R.id.award_desc);
            if(mDesc != null && mDesc.length() <= 0) {
                awardDesc.setVisibility(View.GONE);
            } else {
                awardDesc.setText(mDesc);
            }

            TextView awardPrize = (TextView) mProfile.findViewById(R.id.award_prize);
            awardPrize.setText(mPrize);

            TextView awardWebsite = (TextView) mProfile.findViewById(R.id.award_website);
            if(mWebsite != null && mWebsite.length() > 0) {
                awardWebsite.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(mWebsite));
                        startActivity(intent);
                    }
                });
            } else {
                awardWebsite.setVisibility(View.INVISIBLE);
            }

            TextView awardSponsor = (TextView) mProfile.findViewById(R.id.award_sponsor);
            awardSponsor.setText(mSponsor);

            Button okButton = (Button) mProfile.findViewById(R.id.award_detail_ok);
            okButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AwardDialog.this.getDialog().cancel();
                }
            });

            builder.setView(mProfile);

            return builder.create();
        }

        AwardDialog newInstance(Award award) {
            AwardDialog f = new AwardDialog();
            Sponsor sponsor = award.getSponsor();

            Bundle args = new Bundle();
            if (sponsor != null) args.putString("image", sponsor.getLogo().getUrl());
            args.putString("title", award.getTitle());
            args.putString("prize", award.getPrize());
            if (sponsor != null) args.putString("sponsor", award.getSponsor().getName());
            args.putString("website", award.getWebsite());
            args.putString("desc", award.getDescription());
            f.setArguments(args);

            return f;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            mImageLocation = getArguments().getString("image");
            mTitle = getArguments().getString("title");
            mPrize = getArguments().getString("prize");
            mSponsor = getArguments().getString("sponsor");
            mWebsite = getArguments().getString("website");
            mDesc = getArguments().getString("desc");

            if (mSponsor == null) mSponsor = "MHacks";
        }
    }
}
