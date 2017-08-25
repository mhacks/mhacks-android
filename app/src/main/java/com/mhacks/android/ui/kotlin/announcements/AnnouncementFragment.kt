package com.mhacks.android.ui.kotlin.announcements

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.mhacks.android.data.model.Announcement
import com.mhacks.android.data.network.NetworkManager
import com.mhacks.android.ui.common.BaseFragment
import com.mhacks.android.ui.common.NavigationColor
import org.mhacks.android.R
import java.util.*

/**
 * Created by Omkar Moghe on 10/25/2014.
 * Made Kotlin by Tristan on 10/6/2017
 */

class AnnouncementFragment : BaseFragment() {

    override var setTransparent: Boolean = false
    override var AppBarTitle: Int = R.string.title_announcements
    override var LayoutResourceID: Int = R.layout.fragment_announcements
    override var configureView: (view: View) -> Unit? = fun(view: View) {
        mRecyclerView = view.findViewById<View>(R.id.list_cards) as RecyclerView

    }
    override var NavigationColor: NavigationColor = NavigationColor(R.color.colorPrimary, R.color.colorPrimaryDark)

    //Current query
    private val networkManager = NetworkManager.getInstance()

    // Caches all the Announcements found
    internal lateinit var mAnnouncementsList: ArrayList<Announcement>

    // Caches the listView layout
    internal lateinit var mRecyclerView: RecyclerView

    // Adapter for the listView
    internal lateinit var mListAdapter: MainNavAdapter

    override fun onDestroyView() {
        super.onDestroyView()
        // TODO cancel active requests
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // Only reset this if we need to
        mAnnouncementsList = ArrayList<Announcement>()


        // Initialize the test ListView
        initList()

        // Get Parse data of announcements for the first time
        getAnnouncements()
    }

    // Set up the test listView for displaying announcements
    private fun initList() {
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true)

        // use a linear layout manager
        val layoutManager = LinearLayoutManager(activity)
        mRecyclerView.layoutManager = layoutManager

        // Create and set the adapter for this recyclerView
        mListAdapter = MainNavAdapter(activity)
        mRecyclerView.adapter = mListAdapter
    }

    private fun getAnnouncements() {
        mAnnouncementsList.add(Announcement("Test Announcement", "I LOVE MHACKS", 1501997324, 1, true, false))
        mAnnouncementsList.add(Announcement("Test Announcement", "I LOVE MHACKS", 1501997324, 1, true, false))
        mAnnouncementsList.add(Announcement("Test Announcement", "I LOVE MHACKS", 1501997324, 1, true, false))
        mAnnouncementsList.add(Announcement("Test Announcement", "I LOVE MHACKS", 1501997324, 1, true, false))
        updateAnnouncements()
//        networkManager.getAnnouncements(object : HackathonCallback<List<Announcement>> {
//            override fun success(response: List<Announcement>) {
//                mAnnouncementsList = ArrayList<Announcement>()
//                for (announcement in response) {
//                    val currentTime = Calendar.getInstance()
//                    val announcementTime = Calendar.getInstance()
//                    announcementTime.time = Date(announcement.broadcastAt)
//                    if (currentTime.compareTo(announcementTime) != -1) mAnnouncementsList.add(announcement)
//
//                    updateAnnouncements()
//                }
//            }
//
//            override fun failure(error: Throwable) {
//                Log.e(TAG, "Couldn't get announcements", error)
//            }
//        })
    }

    // Update the announcements shown
    private fun updateAnnouncements() {
        // Notify the adapter that the data changed
        mListAdapter.notifyDataSetChanged()
    }


    internal inner class MainNavAdapter// Default constructor
    (var mContext: Context) : RecyclerView.Adapter<MainNavAdapter.ViewHolder>() {

        // Create new views (invoked by the layout manager)
        override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
            // Create the view for this row
            val row = LayoutInflater.from(mContext)
                    .inflate(R.layout.announcement_list_item, viewGroup, false)

            // Create a new viewHolder which caches all the views that needs to be saved
            val viewHolder = ViewHolder(row)

            return viewHolder
        }

        // Replace the contents of a view (invoked by the layout manager)
        override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
            // - get element from your dataset at this position
            // - replace the contents of the view with that element

            // Get the current announcement item
            val announcement = mAnnouncementsList[i]

            // Set this item's views based off of the announcement data
            viewHolder.titleView.text = announcement.title
            viewHolder.descriptionView.text = announcement.info

            val category = announcement.category
            var current = 1
            for (a in 0..4) {
                current = 1 shl a
                if (category and current != 0) break
            }
            when (current) {
                1 -> {
                }
                2 -> {
                }
                4 -> {
                }
                8 -> {
                }
                16 -> {
                }
                32 -> {
                }
            }
            //                    viewHolder.colorView.setBackgroundColor(getResources().getColor(R.color.event_red));
            //                    viewHolder.colorView.setBackgroundColor(getResources().getColor(R.color.event_blue));
            //                    viewHolder.colorView.setBackgroundColor(getResources().getColor(R.color.event_yellow));
            //                    viewHolder.colorView.setBackgroundColor(getResources().getColor(R.color.event_green));
            //                    viewHolder.colorView.setBackgroundColor(getResources().getColor(R.color.event_purple));
            //                    viewHolder.colorView.setBackgroundColor(getResources().getColor(R.color.md_brown_500));

            // Get the date from this announcement and set it as a relative date
            val date = Date(announcement.broadcastAt)
            val relativeDate = DateUtils.getRelativeTimeSpanString(date.time)
            viewHolder.dateView.text = relativeDate
        }

        // Return the size of your dataset (invoked by the layout manager)
        override fun getItemCount(): Int {
            return mAnnouncementsList.size
        }

        // Simple class that holds all the views that need to be reused
        internal inner class ViewHolder// Default constructor, itemView holds all the views that need to be saved
        (itemView: View) : RecyclerView.ViewHolder(itemView) {
            var titleView: TextView = itemView.findViewById<View>(R.id.info_title) as TextView
            var dateView: TextView = itemView.findViewById<View>(R.id.info_date) as TextView
            var descriptionView: TextView = itemView.findViewById<View>(R.id.info_description) as TextView
        }
    }

    companion object {
        //Local datastore pin title.
        val ANNOUNCEMENT_PIN = "announcementPin"

        private val TAG = "MD/Announcements"

        val instance: AnnouncementFragment
            get() = AnnouncementFragment()
    }
}
