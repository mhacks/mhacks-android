package com.mhacks.android.ui.nav;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.mhacks.android.data.model.AnnouncementDud;
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
public class AnnouncementsFragment extends Fragment{
    private static final String TAG = "MD/Announcements";

    // Caches all the Announcements found
    ArrayList<AnnouncementDud> mAnnouncementsList;

    // Caches the listView layout
    ListView mTestList;
    // Adapter for the listView
    TestListAdapter mTestAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_announcements, container, false);
        mTestList = (ListView) view.findViewById(R.id.list);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Initialize the list that holds all the announcements
        mAnnouncementsList = new ArrayList<AnnouncementDud>();

        // Initialize the test ListView
        initTestList();

        // Get Parse data of announcements for the first time
        initParseData();
    }

    // Set up the test listView for displaying announcements
    private void initTestList() {
        // Set up the adapter
        mTestAdapter = new TestListAdapter(getActivity());

        // Make the listView use the adapter
        mTestList.setAdapter(mTestAdapter);
    }

    private void initParseData() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Announcement");
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
        mTestAdapter.notifyDataSetChanged();
    }

    private class TestListAdapter extends BaseAdapter {
        Context mContext;

        // Default constructor
        public TestListAdapter(Context context) {
            this.mContext = context;
        }

        @Override
        public int getCount() {
            return mAnnouncementsList.size();
        }

        @Override
        public Object getItem(int position) {
            return mAnnouncementsList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row;
            ViewHolder holder;

            if(convertView == null) {
                // Then gotta set up this row for the first time
                LayoutInflater inflater =
                        (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = inflater.inflate(R.layout.announcement_list_item, parent, false);

                // Create a ViewHolder to save all the different parts of the row
                holder = new ViewHolder();
                holder.titleView = (TextView) row.findViewById(R.id.info_text);
                holder.descriptionView = (TextView) row.findViewById(R.id.info_description);

                // Make the row reuse the ViewHolder
                row.setTag(holder);
            }
            else { // Otherwise, use the recycled view
                row = convertView;
                holder = (ViewHolder) row.getTag();
            }

            // Set the data for this item
            String title = mAnnouncementsList.get(position).getTitle();
            String description = mAnnouncementsList.get(position).getMessage();
            holder.titleView.setText(title);
            holder.descriptionView.setText(description);

            return row;
        }

        // Contains the view's objects
        class ViewHolder {
            public TextView titleView;
            public TextView descriptionView;
        }
    }
}
