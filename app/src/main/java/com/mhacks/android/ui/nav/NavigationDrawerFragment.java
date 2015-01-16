package com.mhacks.android.ui.nav;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.mhacks.iv.android.R;

import java.util.ArrayList;

/**
 * Created by Omkar Moghe on 10/22/2014.
 */
public class NavigationDrawerFragment extends Fragment {
    private static final String TAG = "MD/NavigationDrawerFragment";

    private NavigationDrawerCallbacks mCallbacks;
    private int mCurrentSelectedPosition;

    private View mNavDrawerView;

    private ListView mListViewNav;
    private MainNavAdapter mListAdapter;

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
        mListAdapter = new MainNavAdapter(getActivity());
        mListViewNav.setAdapter(mListAdapter);

        // Set the ListView adapter to only allow one item to be "selected" at a time
        mListViewNav.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        // Set the first item to already have been selected
        mListViewNav.setItemChecked(0, true);

        // Set up the selecting behaviour on the listView
        mListViewNav.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Double check that the user didn't click on the current item again
                if(position == mCurrentSelectedPosition) {
                    // Try to close the drawer
                    if(mCallbacks != null)
                        mCallbacks.closeDrawer();

                    return;
                }

                // Set the new position as the highlighted, selected row
                mListViewNav.setItemChecked(position, true);

                // "Activate" the correct icon
                mListAdapter.highlightNewIcon(position);

                // Change the official fragment position
                setPosition(position);
            }
        });
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
        void closeDrawer();
    }

    class MainNavAdapter extends BaseAdapter {
        private Context mContext;

        // Holds the titles of every row
        private String[] rowTitles;

        // Holds all the icon drawables
        private ArrayList<Drawable> iconDrawables;

        // Colors for different states of the icons
        private int neutralColor;
        private int activeColor;

        // Holds a reference to the last active row
        private int lastActiveRow = -1;

        // List of all the cached icons
        private ArrayList<ImageView> cachedIcons;

        // Default constructor
        MainNavAdapter(Context context) {
            this.mContext = context;

            // Get a reference to the resources to get all the data for the rows
            Resources res = context.getResources();

            // Get the rowTitles - the necessary data for now - from resources
            rowTitles = res.getStringArray(R.array.nav_items);

            // Get all the drawables and put them in order in an array
                // NOTE: Order currently is Countdown -> Announcements -> Schedule ->
                //              Sponsors -> Awards -> Map
            iconDrawables = new ArrayList<>(rowTitles.length);
            iconDrawables.add(res.getDrawable(R.drawable.nav_drawable_countdown));
            iconDrawables.add(res.getDrawable(R.drawable.nav_drawable_announcements));
            iconDrawables.add(res.getDrawable(R.drawable.nav_drawable_schedule));
            iconDrawables.add(res.getDrawable(R.drawable.nav_drawable_sponsors));
            iconDrawables.add(res.getDrawable(R.drawable.nav_drawable_awards));
            iconDrawables.add(res.getDrawable(R.drawable.nav_drawable_map));

            // Cache the two colors used for the icon color filters
            neutralColor = res.getColor(R.color.black);
            activeColor = res.getColor(R.color.blue);

            // Initialize the list of cached icons, for later
            cachedIcons = new ArrayList<>(rowTitles.length);
            // Add duds to the list, for ease of use
            for(int i = 0; i < rowTitles.length; ++i) cachedIcons.add(null);
        }

        // Highlights the new icon's position
        public void highlightNewIcon(int position) {
            // First, if there is a previous active ImageView, reset its color filter
            if(lastActiveRow != -1) {
                ImageView lastActiveIcon = cachedIcons.get(lastActiveRow);
                lastActiveIcon.clearColorFilter();
                lastActiveIcon.setColorFilter(neutralColor, PorterDuff.Mode.SRC_ATOP);
            }
            // Get and set the active color filter to the correct icon
            ImageView correctIcon = cachedIcons.get(position);
            correctIcon.clearColorFilter();
            correctIcon.setColorFilter(activeColor, PorterDuff.Mode.SRC_ATOP);

            // Finally, cache the new position
            lastActiveRow = position;
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

            // Set the icon drawable of this row
            holder.rowIcon.setImageDrawable(iconDrawables.get(position));

            // If the initial highlighting has not been done yet and this is position 0, then add the active color filter
            if(lastActiveRow == -1 && position == 0) {
                holder.rowIcon.clearColorFilter();
                holder.rowIcon.setColorFilter(activeColor, PorterDuff.Mode.SRC_ATOP);

                lastActiveRow = position;
            }
            // If this position is supposed to be an active row, then highlight its icon
            else if(lastActiveRow == position) {
                holder.rowIcon.clearColorFilter();
                holder.rowIcon.setColorFilter(activeColor, PorterDuff.Mode.SRC_ATOP);
            }
            // Otherwise, create a default black color filter for the icon
            else {
                holder.rowIcon.clearColorFilter();
                holder.rowIcon.setColorFilter(neutralColor, PorterDuff.Mode.SRC_ATOP);
            }

            // Cache the icon for later use in its proper position in the list
            cachedIcons.set(position, holder.rowIcon);

            return row;
        }

        // Simple class that holds all the views that need to be reused
        class ViewHolder {
            TextView rowTitle; // The title of this item
            ImageView rowIcon; // The icon imageView of this item
        }
    }
}
