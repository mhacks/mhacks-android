package com.mhacks.android.ui.nav;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.mhacks.android.data.model.Sponsor;
import com.mhacks.android.ui.common.ImageAdapter;
import com.mhacks.android.ui.common.ImageLoader;
import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.GetCallback;
import com.parse.ParseException;

import com.mhacks.iv.android.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Omkar Moghe on 10/25/2014.
 */
public class SponsorsFragment extends Fragment{

    private View mSponsorsFragView;
    private ImageAdapter adapter;
    private Context context;
    private ArrayList<Sponsor> sponsors;
    //private GridView sponsorView;
    private ImageAdapter imageAdapter;
    //private ArrayList<Sponsor> section;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        mSponsorsFragView = inflater.inflate(R.layout.fragment_sponsors, container, false);
        //sponsorView = (GridView) mSponsorsFragView.findViewById(R.id.sponsor_view);
        sponsors = new ArrayList<Sponsor>();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Sponsor");
        query.include("tier");
        query.include("location");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> object, ParseException e) {
                if (e == null) {
                    // sort by tier
                    for (int x = 0; x < 3; x++) {
                        for (int i = 0; i < object.size(); i++) {
                            if (object.get(i) != null) {
                                Sponsor c = (Sponsor) object.get(i);
                                if (c.getTier().getLevel() == x){
                                    sponsors.add(c);
                                }
                            }
                        }
                    }
                            /*
                             GridView Item Click Listener
            listView.setOnItemClickListener(new OnItemClickListener() {
                             */
                }
                else {
                    Log.e("Error", e.getMessage());
                    e.printStackTrace();
                }

                //gridview.setAdapter(new ImageAdapter(this));
                /*ArrayList <String> list_urls= new ArrayList<String>();
                for (int i = 0; i < sponsors.size(); ++i){
                    list_urls.add((sponsors.get(i).getLogo().getUrl()));
                }*/
                buildAdapter();
                /*sponsorView.setAdapter(new ImageAdapter(mSponsorsFragView.getContext(), sponsors));
                sponsorView.setOnItemClickListener(new OnItemClickListener() {
                    public void onItemClick(AdapterView parent, View v, int position, long id) {
                        DialogFragment dialogsponsor = new DialogSponsor().newInstance(position);
                        dialogsponsor.show(getFragmentManager(), sponsors.get(position).getName());
                    }
                });*/

            }

        });
        return mSponsorsFragView;
    }

    @Override
    public void onActivityCreated (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);


    }

    public class DialogSponsor extends DialogFragment {
        View profile;
        String mName;
        String mTier;
        String mDesc;
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            // Get the layout inflater
            LayoutInflater inflater = getActivity().getLayoutInflater();

            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            profile = inflater.inflate(R.layout.sponsor_profile, null);
            TextView sponsorname = (TextView) profile.findViewById(R.id.sponsor_title);
            TextView sponsordesc = (TextView) profile.findViewById(R.id.sponsor_desc);
            final ImageView sponsorImage = (ImageView) profile.findViewById(R.id.sponsor_pic);
            TextView sponsortier = (TextView) profile.findViewById(R.id.sponsor_tier);
            sponsortier.setText(mTier);
            sponsorname.setText(mName);
            sponsordesc.setText(mDesc);
            int mNum = 0;
            for (int i = 0; i < sponsors.size(); i++){
                if (mName == sponsors.get(i).getName()){
                    mNum = i;
                }
            }
            final String weblink = sponsors.get(mNum).getWebsite();
            TextView sponsorwebsite = (TextView) profile.findViewById(R.id.sponsor_website);
            sponsorwebsite.setText(sponsors.get(mNum).getWebsite());
            sponsorwebsite.setTextColor(R.color.blue);
            sponsorwebsite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse(weblink));
                    startActivity(intent);
                }
            });
            TextView sponsorloc = (TextView) profile.findViewById(R.id.sponsor_location);
            sponsorloc.setText(sponsors.get(mNum).getLocation().getName());
            sponsors.get(mNum).getLogo().getDataInBackground(new GetDataCallback() {
                @Override
                public void done(byte[] bytes, ParseException e) {
                    sponsorImage.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
                }
            });

            builder.setView(profile)
                    // Add action buttons
                    .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            DialogSponsor.this.getDialog().cancel();
                        }
                    });
            return builder.create();
        }

        DialogSponsor newInstance(String name, String tier, String desc) {
            DialogSponsor f = new DialogSponsor();

            // Supply num input as an argument.
            Bundle args = new Bundle();
            args.putString("name", name);
            args.putString("tier", tier);
            args.putString("desc", desc);
            f.setArguments(args);

            return f;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            mName = getArguments().getString("name");
            mTier = getArguments().getString("tier");
            mDesc = getArguments().getString("desc");
        }
    }

    private void buildAdapter() {
        for (int i = 0; i < 3; i++){
            final ArrayList<Sponsor> section = new ArrayList<Sponsor>();
            // seperates into sponsor sections
            for(int x = 0; x < sponsors.size(); x++){
                if (i == sponsors.get(x).getTier().getLevel()){

                    section.add(sponsors.get(x));
                }
            }
            // keeps track of the index of sponsors
            //creates a new adapter
            imageAdapter = new ImageAdapter(getActivity(), section);

      // create the view
            LinearLayout applayout = (LinearLayout) getActivity()
                    .findViewById(R.id.sponsor_view);

      // create the tier header
            TextView appSectionHeader = new TextView(getActivity());
            appSectionHeader.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            appSectionHeader.setText(section.get(0).getTier().getName());
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) appSectionHeader
                    .getLayoutParams();
            params.setMargins(15, 8, 15, 8); // left, top, right, bottom
            appSectionHeader.setLayoutParams(params);
            appSectionHeader.setVisibility(View.VISIBLE);
            appSectionHeader.setTextSize(20);
            appSectionHeader.setTextColor(R.color.black);

      // create GridView
            GridView appSectionGridView = new GridView(getActivity());
            appSectionGridView.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            appSectionGridView.setVerticalSpacing(10);
            appSectionGridView.setHorizontalSpacing(10);
            appSectionGridView.setNumColumns(4);
            appSectionGridView.setGravity(Gravity.CENTER);
            appSectionGridView.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
            appSectionGridView.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View v, int position,
                                        long id) {
          /* position of clicked app and adapter it was located in */
                    DialogFragment dialogsponsor = new DialogSponsor().newInstance(section.get(position).getName(),section.get(position).getTier().getName(),section.get(position).getDescription());
                    dialogsponsor.show(getFragmentManager(), section.get(position).getName());
                }
            });
            appSectionGridView.setAdapter(imageAdapter);
            applayout.addView(appSectionHeader);
            applayout.addView(appSectionGridView);
        }
    }
}



