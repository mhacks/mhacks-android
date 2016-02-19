package com.mhacks.android.ui.announcements;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.mhacks.android.data.model.Announcement;
import com.mhacks.android.data.network.HackathonCallback;
import com.mhacks.android.data.network.NetworkManager;

import org.mhacks.android.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Omkar Moghe on 10/25/2014.
 */
public class AnnouncementsFragment extends Fragment {
    private static final String TAG = "MD/Announcements";

    //Local datastore pin name.
    public static final String ANNOUNCEMENT_PIN = "announcementPin";

    // Caches all the Announcements found
    ArrayList<Announcement> mAnnouncementsList;

    // Caches the listView layout
    RecyclerView mRecyclerView;
    // Adapter for the listView
    MainNavAdapter mListAdapter;

    //Current query
    private final NetworkManager networkManager = NetworkManager.getInstance();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_announcements, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.list_cards);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // TODO cancel active requests
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Only reset this if we need to
        if(mAnnouncementsList == null) {
            mAnnouncementsList = new ArrayList<Announcement>();
        }

        // Initialize the test ListView
        initList();

        // Get Parse data of announcements for the first time
        getAnnouncements();
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

    private void getAnnouncements() {
        networkManager.getAnnouncements(new HackathonCallback<List<Announcement>>() {
            @Override
            public void success(List<Announcement> response) {
                mAnnouncementsList = new ArrayList<Announcement>();
                for (Announcement announcement : response) {
                    Calendar currentTime = Calendar.getInstance();
                    Calendar announcementTime = Calendar.getInstance();
                    if (announcement.getBroadcastTime() != null) announcementTime.setTime(announcement.getBroadcastTime());
                    if (currentTime.compareTo(announcementTime) != -1) mAnnouncementsList.add(announcement);

                    updateAnnouncements();
                }
            }

            @Override
            public void failure(Throwable error) {
                Log.e(TAG, "Couldn't get announcements", error);
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
            public FrameLayout colorView;

            // Default constructor, itemView holds all the views that need to be saved
            public ViewHolder(View itemView) {
                super(itemView);

                // Save the TextViews
                this.titleView = (TextView) itemView.findViewById(R.id.info_title);
                this.dateView = (TextView) itemView.findViewById(R.id.info_date);
                this.descriptionView = (TextView) itemView.findViewById(R.id.info_description);
                this.colorView = (FrameLayout) itemView.findViewById(R.id.announcement_color);
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
            Announcement announcement = mAnnouncementsList.get(i);

            // Set this item's views based off of the announcement data
            viewHolder.titleView.setText(announcement.getName());
            viewHolder.descriptionView.setText(announcement.getInfo());

            int category = announcement.getCategory();
            int current = 1;
            for (int a = 0; a < 5; ++a) {
                current = 1 << a;
                if ((category & current) != 0) break;
            }
            switch (current) {
                case 1:
                    viewHolder.colorView.setBackgroundColor(getResources().getColor(R.color.event_red));
                    break;
                case 2:
                    viewHolder.colorView.setBackgroundColor(getResources().getColor(R.color.event_blue));
                    break;
                case 4:
                    viewHolder.colorView.setBackgroundColor(getResources().getColor(R.color.event_yellow));
                    break;
                case 8:
                    viewHolder.colorView.setBackgroundColor(getResources().getColor(R.color.event_green));
                    break;
                case 16:
                    viewHolder.colorView.setBackgroundColor(getResources().getColor(R.color.event_purple));
                    break;
                case 32:
                    viewHolder.colorView.setBackgroundColor(getResources().getColor(R.color.md_brown_500));
                    break;
            }

            // Get the date from this announcement and set it as a relative date
            Date date = announcement.getBroadcastTime();
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
