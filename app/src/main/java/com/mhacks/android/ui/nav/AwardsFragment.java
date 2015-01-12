package com.mhacks.android.ui.nav;

import android.app.Dialog;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import com.mhacks.android.data.model.Award;
import com.mhacks.iv.android.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Omkar Moghe on 10/25/2014.
 */
public class AwardsFragment extends Fragment{

    private View mAwardsFragView;
    private List<Award> awardList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        mAwardsFragView = inflater.inflate(R.layout.fragment_awards, container, false);

        //Put code for instantiating views, etc here. (before the return statement.)

        awardList = new ArrayList<>();

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Award");
        query.include("sponsor");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objectList, ParseException e) {
                if (e == null) {
                    Log.d("Awards", "Retrieved " + objectList.size() + " awards");
                    for (ParseObject p : objectList) {
                        Award a = (Award) p;
                        awardList.add(a);
                        Log.d("Awards", a.getTitle() + ": " + a.getPrize());
                    }
                    Log.d("Awards", "awardList size in onCreateView = " + awardList.size());
                    CustomGrid adapter = new CustomGrid(mAwardsFragView.getContext(), awardList);
                    GridView gridView = (GridView) mAwardsFragView.findViewById(R.id.gridView);
                    gridView.setAdapter(adapter);
                    gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            final Dialog dialog = new Dialog(mAwardsFragView.getContext());
                            dialog.setContentView(R.layout.award_dialog);
                            dialog.setTitle(awardList.get(position).getTitle());
                            TextView prizeTextView = (TextView) dialog.findViewById(R.id.prizeTextView);
                            prizeTextView.setText(awardList.get(position).getPrize());
                            TextView descriptionTextView = (TextView) dialog.findViewById(R.id.descriptionTextView);
                            descriptionTextView.setText(awardList.get(position).getDescription());
                            Button okButton = (Button) dialog.findViewById(R.id.okButton);
                            okButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog.dismiss();
                                }
                            });
                            dialog.show();
                        }
                    });
                }
                else {
                    Log.d("Awards", "Error: " + e.getMessage());
                }
            }
        });

        return mAwardsFragView;
    }

}
