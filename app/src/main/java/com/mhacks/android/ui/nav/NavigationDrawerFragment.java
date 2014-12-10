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
import android.widget.TextView;
import android.widget.Toast;

import com.mhacks.iv.android.R;

/**
 * Created by Omkar Moghe on 10/22/2014.
 */
public class NavigationDrawerFragment extends Fragment {
    private static final String TAG = "MD/NavigationDrawerFragment";

    private NavigationDrawerCallbacks mCallbacks;
    private int                       mCurrentSelectedPosition;
    private ActionBarDrawerToggle     mDrawerToggle;

    private View mNavDrawerView;

    private RecyclerView mMainRecyclerNav;

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
        mMainRecyclerNav = (RecyclerView) mNavDrawerView.findViewById(R.id.list_navmain);

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
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mMainRecyclerNav.setHasFixedSize(true);

        // use a linear layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mMainRecyclerNav.setLayoutManager(layoutManager);

        // Create and set the adapter for this recyclerView
        MainNavAdapter adapter = new MainNavAdapter(getActivity());
        mMainRecyclerNav.setAdapter(adapter);
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

    class MainNavAdapter extends RecyclerView.Adapter<MainNavAdapter.ViewHolder> {
        Context mContext;

        // Holds the titles of every row
        String[] rowTitles;

        // Default constructor
        MainNavAdapter(Context context) {
            this.mContext = context;

            // Get the rowTitles - the necessary data for now - from resources
            rowTitles = context.getResources().getStringArray(R.array.nav_items);
        }

        // Simple class that holds all the views that need to be reused
        class ViewHolder extends RecyclerView.ViewHolder{
            View parentView; // The view which holds all the other views
            TextView rowTitle; // The title of this item

            // Default constructor, itemView holds all the views that need to be saved
            public ViewHolder(View itemView) {
                super(itemView);

                // Save the entire itemView, for setting listeners and usch later
                this.parentView = itemView;

                // Save the TextView- all that's supported at the moment
                this.rowTitle = (TextView) itemView.findViewById(R.id.row_title);
            }
        }

        // Create new views (invoked by the layout manager)
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            // Create the view for this row
            View row = LayoutInflater.from(mContext)
                    .inflate(R.layout.list_navmain_row, viewGroup, false);

            // Create a new viewHolder which caches all the views that needs to be saved
            ViewHolder viewHolder = new ViewHolder(row);

            return viewHolder;
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int i) {
            // - get element from your dataset at this position
            // - replace the contents of the view with that element
            viewHolder.rowTitle.setText(rowTitles[i]);

            // TODO: Make a better workaround for passing in the position to the listener
            final int position = i;



            // Set a listener for this entire view
            viewHolder.parentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Set the position of the current navigation
                    setPosition(position);
                }
            });
        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return rowTitles.length;
        }
    }
}
