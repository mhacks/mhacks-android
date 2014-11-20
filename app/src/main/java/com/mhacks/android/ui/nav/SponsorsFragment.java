package com.mhacks.android.ui.nav;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;

import com.mhacks.android.data.model.Sponsor;
import com.mhacks.android.ui.common.ImageAdapter;
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
    private GridView Sponsorview;
    private ImageAdapter adapter;
    private Context context;
    private ArrayList<Sponsor> sponsors;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        sponsors = new ArrayList<Sponsor>();
        mSponsorsFragView = inflater.inflate(R.layout.fragment_sponsors, container, false);
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Sponsor");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> object, ParseException e) {
                Sponsorview = (GridView) getActivity().findViewById(R.id.sponsor_list);
                if (e == null) {
                    //sponsors.add("Hey");
                    Log.v("Parse",Integer.toString(object.size()));
                    for (int i = 0; i < object.size(); i++) {
                        if (object.get(i) != null) {
                            Sponsor c = (Sponsor) object.get(i);
                            Log.v("Parse","I'm inside this");
                            Log.v("Parse",c.getName());
                            sponsors.add(c);
                        }
                    }
                            /*
                             ListView Item Click Listener
            listView.setOnItemClickListener(new OnItemClickListener() {
                             */
                }
                else {
                    Log.e("Error", e.getMessage());
                    e.printStackTrace();
                }
                ArrayList <String> list_urls= new ArrayList<String>();
                for (int i = 0; i < sponsors.size(); ++i){
                    list_urls.add((sponsors.get(i).getLogo().getUrl()));
                }
                Sponsorview.setAdapter(new ImageAdapter(getActivity().getApplicationContext(), list_urls));
                //gridview.setAdapter(new ImageAdapter(this));
            }
        });



        return mSponsorsFragView;
    }
}
