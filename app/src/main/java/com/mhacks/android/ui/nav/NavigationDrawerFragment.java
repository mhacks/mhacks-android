package com.mhacks.android.ui.nav;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.mhacks.iv.android.R;

/**
 * Created by Omkar Moghe on 10/22/2014.
 */
public class NavigationDrawerFragment extends Fragment {
    private static final String TAG = "MD/NavigationDrawerFragment";

    private NavigationDrawerCallbacks mCallbacks;
    private int mCurrentSelectedPosition;
    private ActionBarDrawerToggle mDrawerToggle;

    private View mNavDrawerView;

    private ListView mListViewNav;

    private TextView mAnnouncementsTextView, mScheduleTextView, mSponsorsTextView, mAwardsTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        mNavDrawerView = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);

        // Cache the main nav's RecyclerView
        mListViewNav = (ListView) mNavDrawerView.findViewById(R.id.listview);

        return mNavDrawerView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Setup the main nav now
        setupMainNav();
    }

    // Sets up the main navigation
    private void setupMainNav() {
        // Set up the main listView nav with the specified adapter
        MainNavAdapter adapter = new MainNavAdapter(getActivity());
        mListViewNav.setAdapter(adapter);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallbacks = (NavigationDrawerCallbacks) activity;
        } catch (ClassCastException e) {
            Log.e(TAG, "Activity must implement NavigationDrawerCallbacks", e);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    public void setPosition(int position) {
        mCurrentSelectedPosition = position;
        if (mCallbacks != null) {
            mCallbacks.onNavigationDrawerItemSelected(position);
        }
    }

    public static interface NavigationDrawerCallbacks {
        void onNavigationDrawerItemSelected(int position);
    }

    class MainNavAdapter extends BaseAdapter {
        Context mContext;

        // Holds the titles of every row
        String[] rowTitles;

        // Default constructor
        MainNavAdapter(Context context) {
            this.mContext = context;

            // Get the rowTitles - the necessary data for now - from resources
            rowTitles = context.getResources().getStringArray(R.array.nav_items);
        }

        @Override
        public int getCount() {
            return rowTitles.length;
        }

        @Override
        public Object getItem(int position) {
            return rowTitles[position];
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = null;
            ViewHolder holder;

            if (convertView == null) {
                // Then gotta set up this row for the first time
                LayoutInflater inflater =
                        (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = inflater.inflate(R.layout.list_navmain_row, parent, false);

                // Create a ViewHolder to save all the different parts of the row
                holder = new ViewHolder();
                holder.rowTitle = (TextView) row.findViewById(R.id.row_title);
                holder.rowIcon = (ImageView) row.findViewById(R.id.row_icon);

                // Make the row reuse the ViewHolder
                row.setTag(holder);
            } else { // Otherwise, use the recycled view
                row = convertView;
                holder = (ViewHolder) row.getTag();
            }   

            // Set the title of this row
            holder.rowTitle.setText(rowTitles[position]);

            // TODO: Set the icon of this row

            return row;
        }

        // Simple class that holds all the views that need to be reused
        class ViewHolder {
            TextView rowTitle; // The title of this item
            ImageView rowIcon; // The icon imageView of this item
        }
    }
}
