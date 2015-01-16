package com.mhacks.android.ui.nav;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;

import com.mhacks.android.data.model.Sponsor;
import com.mhacks.android.data.model.SponsorTier;
import com.mhacks.android.ui.MainActivity;
import com.parse.*;

import com.mhacks.iv.android.R;
import com.squareup.picasso.Picasso;
import com.tonicartos.widget.stickygridheaders.StickyGridHeadersBaseAdapter;
import com.tonicartos.widget.stickygridheaders.StickyGridHeadersGridView;

import java.util.*;


/**
 * Created by Omkar Moghe on 10/25/2014.
 */
public class SponsorsFragment extends Fragment implements OnItemClickListener {

    private static final String TAG = "Sponsors";
    private static final String SPONSOR_PIN = "sponsorPin";

    private View mSponsorsFragView;
    private StickyGridHeadersGridView mSponsorsView;
    private SponsorsAdapter mAdapter;

    // This model for the data is a little wonky, but the grid view adapter is also wonky
    private ArrayList<Sponsor> mSponsors;
    private ArrayList<Integer> mSponsorsPerTier;
    private ArrayList<SponsorTier> mTiers;

    //Current query
    private ParseQuery<Sponsor> currentQuery;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        mSponsorsFragView = inflater.inflate(R.layout.fragment_sponsors, container, false);
        mSponsorsView = (StickyGridHeadersGridView) mSponsorsFragView.findViewById(R.id.sponsor_view);

        mSponsors = new ArrayList<Sponsor>();
        mSponsorsPerTier = new ArrayList<Integer>();
        mTiers = new ArrayList<SponsorTier>();

        setUpGridView();
        getLatestParseData();

        return mSponsorsFragView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (currentQuery != null) currentQuery.cancel();
    }

    private void setUpGridView() {
        mSponsorsView.setOnItemClickListener(this);
        mSponsorsView.setNumColumns(5);
        mSponsorsView.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);

        mAdapter = new SponsorsAdapter(mSponsors, mSponsorsPerTier, mTiers);
        mSponsorsView.setAdapter(mAdapter);
    }

    private void getLatestParseData() {
        getLocalParseData();
    }

    private ParseQuery<Sponsor> getBaseQuery() {
        ParseQuery<Sponsor> query = ParseQuery.getQuery("Sponsor");
        query.include("tier");
        query.include("location");
        currentQuery = query;
        return query;
    }

    private void getLocalParseData() {
        ParseQuery<Sponsor> query = getBaseQuery();
        query.fromPin(SPONSOR_PIN);
        query.findInBackground(new FindCallback<Sponsor>() {
            @Override
            public void done(List<Sponsor> parseObjects, ParseException e) {
                if(e != null || parseObjects == null || parseObjects.size() <= 0) {
                    Log.e(TAG, "Couldn't get the local sponsors, falling back on remote");
                } else {
                    Log.d(TAG, "Got the local sponsors");
                    processAndDisplaySponsors(parseObjects);
                }

                getRemoteParseData();
            }
        });
    }

    private void getRemoteParseData() {
        ParseQuery<Sponsor> query = getBaseQuery();
        query.findInBackground(new FindCallback<Sponsor>() {
            @Override
            public void done(List<Sponsor> parseObjects, ParseException e) {
                if(e != null || parseObjects == null || parseObjects.size() <= 0) {
                    Log.e(TAG, "Couldn't get the remote sponsors");

                    // We don't have anything locally, let the user know we need internet
                    if(mSponsors == null || mSponsors.size() <= 0) {
                        if(getActivity() != null) ((MainActivity)getActivity()).showNoInternetOverlay();
                    } else {
                        // We do have local stuff, no make sure we aren't in the way
                        if(getActivity() != null) ((MainActivity)getActivity()).hideNoInternetOverlay();
                    }
                } else {
                    Log.d(TAG, "Got the remote sponsors, displaying them");

                    // We got the remote maps, so unpin the old ones and pin the new ones
                    ParseObject.unpinAllInBackground(SPONSOR_PIN);
                    ParseObject.pinAllInBackground(SPONSOR_PIN, parseObjects);

                    // Display the new maps and get outta the way
                    processAndDisplaySponsors(parseObjects);
                    if(getActivity() != null) ((MainActivity)getActivity()).hideNoInternetOverlay();
                }
            }
        });
    }

    private void processAndDisplaySponsors(List<Sponsor> sponsorList) {
        // First, sort the sponsors alphabetically
        Collections.sort(sponsorList, new Comparator<ParseObject>() {
            @Override
            public int compare(ParseObject lhs, ParseObject rhs) {
                return lhs.getString("name").compareToIgnoreCase(rhs.getString("name"));
            }
        });

        // Then split them into tiers
        HashMap<Integer, ArrayList<Sponsor>> splitSponsors = new HashMap<Integer, ArrayList<Sponsor>>();
        HashSet<SponsorTier> tiers = new HashSet<SponsorTier>();

        for(ParseObject sponsor : sponsorList) {
            SponsorTier tier = (SponsorTier) sponsor.getParseObject("tier");

            if(splitSponsors.get(tier.getLevel()) == null) {
                splitSponsors.put(tier.getLevel(), new ArrayList<Sponsor>());
            }

            splitSponsors.get(tier.getLevel()).add((Sponsor) sponsor);
            tiers.add(tier);
        }

        // Sort the tiers
        mTiers.clear();
        mTiers.addAll(tiers);
        Collections.sort(mTiers, new Comparator<SponsorTier>() {
            @Override
            public int compare(SponsorTier lhs, SponsorTier rhs) {
                return lhs.getLevel() - rhs.getLevel();
            }
        });

        // Record the number of sponsors in each tier and add them to the main list
        mSponsors.clear();
        mSponsorsPerTier.clear();
        for(SponsorTier tier : mTiers) {
            mSponsorsPerTier.add(splitSponsors.get(tier.getLevel()).size());
            mSponsors.addAll(splitSponsors.get(tier.getLevel()));
        }

        // Let the adapter know we have new stuff
        mAdapter.notifyDataSetChanged();
    }

    private class SponsorsAdapter extends BaseAdapter implements StickyGridHeadersBaseAdapter {
        private ArrayList<Sponsor> mSponsors;
        private ArrayList<Integer> mSponsorsPerTier;
        private ArrayList<SponsorTier> mTiers;

        public SponsorsAdapter(ArrayList<Sponsor> sponsors, ArrayList<Integer> sponsorsPerTier, ArrayList<SponsorTier> tiers) {
            mSponsors = sponsors;
            mSponsorsPerTier = sponsorsPerTier;
            mTiers = tiers;
        }

        @Override
        public int getCountForHeader(int header) {
            return mSponsorsPerTier.get(header);
        }

        @Override
        public int getNumHeaders() {
            return mTiers.size();
        }

        @Override
        public View getHeaderView(int position, View convertView, ViewGroup parent) {
            final TextView textView;
            if(convertView == null) {
                textView = new TextView(getActivity());
                textView.setLayoutParams(new GridView.LayoutParams(GridView.LayoutParams.MATCH_PARENT, GridView.LayoutParams.WRAP_CONTENT));
                textView.setBackgroundColor(getResources().getColor(R.color.adapter_card_shadow_step_2));
                textView.setTextColor(getResources().getColor(R.color.mh_palette_2));
                textView.setTextSize(20);
                textView.setPadding(12, 0, 0, 0);
            } else {
                textView = (TextView) convertView;
            }

            textView.setText(mTiers.get(position).getName());

            return textView;
        }

        @Override
        public int getCount() {
            return mSponsors.size();
        }

        @Override
        public Object getItem(int position) {
            return mSponsors.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ImageView imageView;
            if(convertView == null) {
                imageView = new ImageView(getActivity());
                imageView.setLayoutParams(new GridView.LayoutParams(GridView.LayoutParams.MATCH_PARENT, 175)); // Yay hardcoding
                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                imageView.setPadding(26, 26, 26, 26);
            } else {
                imageView = (ImageView) convertView;
                Picasso.with(getActivity()).cancelRequest(imageView);
            }

            // Set the image to the sponsor's logo
            ParseFile logo = mSponsors.get(position).getLogo();
            Picasso.with(getActivity()).load(logo.getUrl()).into(imageView);

            return imageView;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        DialogFragment sponsorDialog = new DialogSponsor().newInstance(mSponsors.get(position));
        sponsorDialog.show(getFragmentManager(), mSponsors.get(position).getName());
    }

    public class DialogSponsor extends DialogFragment {
        View mProfile;
        String mImageLocation;
        String mName;
        String mTier;
        String mDesc;
        String mWebsite;
        String mLocation;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = getActivity().getLayoutInflater();
            mProfile = inflater.inflate(R.layout.sponsor_profile, null);

            ImageView sponsorImage = (ImageView) mProfile.findViewById(R.id.sponsor_pic);
            Picasso.with(getActivity()).load(mImageLocation).into(sponsorImage);

            TextView sponsorName = (TextView) mProfile.findViewById(R.id.sponsor_title);
            sponsorName.setText(mName);

            TextView sponsorDesc = (TextView) mProfile.findViewById(R.id.sponsor_desc);
            if(mDesc.length() <= 0) {
                sponsorDesc.setVisibility(View.GONE);
            } else {
                sponsorDesc.setText(mDesc);
            }

            TextView sponsorTier = (TextView) mProfile.findViewById(R.id.sponsor_tier);
            sponsorTier.setText(mTier);

            TextView sponsorWebsite = (TextView) mProfile.findViewById(R.id.sponsor_website);
            sponsorWebsite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(mWebsite));
                    startActivity(intent);
                }
            });

            TextView sponsorLoc = (TextView) mProfile.findViewById(R.id.sponsor_location);
            sponsorLoc.setText(mLocation);

            Button okButton = (Button) mProfile.findViewById(R.id.sponsor_profile_ok);
            okButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogSponsor.this.getDialog().cancel();
                }
            });

            builder.setView(mProfile);

            return builder.create();
        }

        DialogSponsor newInstance(Sponsor sponsor) {
            DialogSponsor f = new DialogSponsor();

            Bundle args = new Bundle();
            args.putString("image", sponsor.getLogo().getUrl());
            args.putString("name", sponsor.getName());
            args.putString("tier", sponsor.getTier().getName());
            args.putString("desc", sponsor.getDescription());
            args.putString("website", sponsor.getWebsite());
            args.putString("location", sponsor.getLocation().getName());
            f.setArguments(args);

            return f;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            mImageLocation = getArguments().getString("image");
            mName = getArguments().getString("name");
            mTier = getArguments().getString("tier");
            mDesc = getArguments().getString("desc");
            mWebsite = getArguments().getString("website");
            mLocation = getArguments().getString("location");
        }
    }
}



