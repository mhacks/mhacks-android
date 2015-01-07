package com.mhacks.android.ui.nav;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.mhacks.android.data.model.Sponsor;
import com.mhacks.android.ui.common.ImageAdapter;
import com.mhacks.android.ui.common.ImageLoader;
import com.parse.FindCallback;
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
    private GridView sponsorView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        mSponsorsFragView = inflater.inflate(R.layout.fragment_sponsors, container, false);
        sponsorView = (GridView) mSponsorsFragView.findViewById(R.id.sponsor_list);
        sponsors = new ArrayList<Sponsor>();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Sponsor");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> object, ParseException e) {
                if (e == null) {
                    for (int i = 0; i < object.size(); i++) {
                        if (object.get(i) != null) {
                            Sponsor c = (Sponsor) object.get(i);
                            sponsors.add(c);
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
                ArrayList <String> list_urls= new ArrayList<String>();
                for (int i = 0; i < sponsors.size(); ++i){
                    list_urls.add((sponsors.get(i).getLogo().getUrl()));
                    //Didn't use levels in case we scrap unobtanium
                }
                sponsorView.setAdapter(new ImageAdapter(mSponsorsFragView.getContext(), sponsors));
                sponsorView.setOnItemClickListener(new OnItemClickListener() {
                    public void onItemClick(AdapterView parent, View v, int position, long id) {
                        DialogFragment dialogsponsor = new DialogSponsor().newInstance(position);
                        dialogsponsor.show(getFragmentManager(), sponsors.get(position).getName());
                    }
                });

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
        int mNum;
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
            ImageView sponsorImage = (ImageView) profile.findViewById(R.id.sponsor_pic);
            TextView sponsortier = (TextView) profile.findViewById(R.id.sponsor_tier);
            /*sponsortier.setText(sponsors.get(mNum).getTier().getName());*/
            sponsorname.setText(sponsors.get(mNum).getName());
            sponsordesc.setText(sponsors.get(mNum).getDescription());
            new ImageLoader(mSponsorsFragView.getContext()).DisplayImage((sponsors.get(mNum).getLogo().getUrl()), sponsorImage);

            builder.setView(profile)
                    // Add action buttons
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            DialogSponsor.this.getDialog().cancel();
                        }
                    });
            return builder.create();
        }

        DialogSponsor newInstance(int index) {
            DialogSponsor f = new DialogSponsor();

            // Supply num input as an argument.
            Bundle args = new Bundle();
            args.putInt("index", index);
            f.setArguments(args);

            return f;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            mNum = getArguments().getInt("index");
        }
    }
}


