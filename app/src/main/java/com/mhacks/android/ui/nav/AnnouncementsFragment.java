package com.mhacks.android.ui.nav;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.mhacks.android.data.model.Announcement;
import com.mhacks.android.data.model.AnnouncementDud;
import com.mhacks.iv.android.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Omkar Moghe on 10/25/2014.
 */
public class AnnouncementsFragment extends Fragment{
    private static final String TAG = "MD/Announcements";

    // Caches all the Announcements found
    ArrayList<AnnouncementDud> mAnnouncementsList;

    // Caches the listView layout
    RecyclerView mRecyclerView;
    // Adapter for the listView
    MainNavAdapter mListAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_announcements, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.list_cards);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Initialize the list that holds all the announcements
        mAnnouncementsList = new ArrayList<AnnouncementDud>();

        // Initialize the test ListView
        initList();

        // Get Parse data of announcements for the first time
        initParseData();
    }

    // Set up the test listView for displaying announcements
    private void initList() {
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);

        // Create and set the adapter for this recyclerView
        mListAdapter = new MainNavAdapter(getActivity());
        mRecyclerView.setAdapter(mListAdapter);
    }

    private void initParseData() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Announcement");
        query.addDescendingOrder(Announcement.DATE_COL);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                // Add the data to the announcements list
                for(ParseObject parseObject : parseObjects) {
                    mAnnouncementsList.add( new AnnouncementDud(parseObject) );
                }

                // Update the data
                updateAnnouncements();
            }
        });
    }

    // Update the announcements shown
    private void updateAnnouncements() {
        // Notify the adapter that the data changed
        mListAdapter.notifyDataSetChanged();
    }

    class MainNavAdapter extends RecyclerView.Adapter<MainNavAdapter.ViewHolder> {
        Context mContext;

        // Default constructor
        MainNavAdapter(Context context) {
            this.mContext = context;
        }

        // Simple class that holds all the views that need to be reused
        class ViewHolder extends RecyclerView.ViewHolder{
            public TextView titleView;
            public TextView dateView;
            public TextView descriptionView;

            // Default constructor, itemView holds all the views that need to be saved
            public ViewHolder(View itemView) {
                super(itemView);

                // Save the TextViews
                this.titleView = (TextView) itemView.findViewById(R.id.info_title);
                this.dateView = (TextView) itemView.findViewById(R.id.info_date);
                this.descriptionView = (TextView) itemView.findViewById(R.id.info_description);
            }
        }

        // Create new views (invoked by the layout manager)
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            // Create the view for this row
            View row = LayoutInflater.from(mContext)
                    .inflate(R.layout.announcement_list_item, viewGroup, false);

            // Create a new viewHolder which caches all the views that needs to be saved
            ViewHolder viewHolder = new ViewHolder(row);

            return viewHolder;
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int i) {
            // - get element from your dataset at this position
            // - replace the contents of the view with that element

            // Get the current announcement item
            AnnouncementDud announcement = mAnnouncementsList.get(i);

            // Set this item's views based off of the announcement data
            viewHolder.titleView.setText(announcement.getTitle());
            viewHolder.descriptionView.setText(announcement.getMessage());

            // Get the date from this announcement and set it as a relative date
            Date date = announcement.getDate();
            CharSequence relativeDate = DateUtils.getRelativeTimeSpanString(date.getTime());
            viewHolder.dateView.setText(relativeDate);
        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return mAnnouncementsList.size();
        }
    }
}
