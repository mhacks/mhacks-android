package com.mhacks.android.ui.announcements

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import com.mhacks.android.data.model.Announcement
import com.mhacks.android.data.network.NetworkManager
import com.mhacks.android.ui.common.BaseFragment
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter
import kotlinx.android.synthetic.main.fragment_announcements.*
import org.mhacks.android.R
import kotlin.collections.ArrayList

/**
 * Created by Omkar Moghe on 10/25/2014.
 * Made Kotlin by Tristan on 10/6/2017
 */

class AnnouncementFragment : BaseFragment() {

    override var setTransparent: Boolean = false
    override var AppBarTitle: Int = R.string.title_announcements
    override var LayoutResourceID: Int = R.layout.fragment_announcements

    //Current query
    private val networkManager = NetworkManager.getInstance()

    // Caches all the Announcements found

    internal lateinit var listAdapter: SectionedRecyclerViewAdapter

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // TODO cancel active requests
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        // Initialize the test ListView
        initList()

        // Get Parse data of announcements for the first time
        getAnnouncements()
    }

    // Set up the test listView for displaying announcements
    private fun initList() {
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        announcements_recycler_view.setHasFixedSize(true)

        announcements_recycler_view.layoutManager = LinearLayoutManager(activity)

        // Create and set the adapter for this recyclerView
        val list = getAnnouncementSectionModelList()
        listAdapter = SectionedAnnouncementAdapter(list)

        announcements_recycler_view.adapter = listAdapter
    }

    private fun getAnnouncementSectionModelList(): ArrayList<AnnouncementSection.AnnouncementSectionModel> {

        val sectionList = ArrayList<AnnouncementSection.AnnouncementSectionModel>();

        sectionList.add(AnnouncementSection.AnnouncementSectionModel("8:00", getAnnouncements()))
        sectionList.add(AnnouncementSection.AnnouncementSectionModel("8:00", getAnnouncements()))
        sectionList.add(AnnouncementSection.AnnouncementSectionModel("8:00", getAnnouncements()))
        sectionList.add(AnnouncementSection.AnnouncementSectionModel("8:00", getAnnouncements()))
        sectionList.add(AnnouncementSection.AnnouncementSectionModel("", getEmptyAnnouncements()))

        return sectionList

    }

    private fun getAnnouncements(): ArrayList<Announcement> {
        val announcementsList = ArrayList<Announcement>()
        announcementsList.add(Announcement("Test Announcement", "This is description 1.", 1501997324, 1, true, false))
        announcementsList.add(Announcement("New Announcement", "This is description 2.", 1501997324, 1, true, false))
        announcementsList.add(Announcement("New Announcement", "This is description 3.", 1501997324, 1, true, false))
        announcementsList.add(Announcement("New Announcement", "This is description 4.", 1501997324, 1, true, false))

        return announcementsList
//        networkManager.getAnnouncements(object : HackathonCallback<List<Announcement>> {
//            override fun success(response: List<Announcement>) {
//                announcementsList = ArrayList<Announcement>()
//                for (announcement in response) {
//                    val currentTime = Calendar.getInstance()
//                    val announcementTime = Calendar.getInstance()
//                    announcementTime.time = Date(announcement.broadcastAt)
//                    if (currentTime.compareTo(announcementTime) != -1) announcementsList.add(announcement)
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


    private fun getEmptyAnnouncements(): ArrayList<Announcement> {
        val announcementsList = ArrayList<Announcement>()
        announcementsList.add(Announcement("", "", 1501997324, 1, true, false))
        return announcementsList
//        networkManager.getAnnouncements(object : HackathonCallback<List<Announcement>> {
//            override fun success(response: List<Announcement>) {
//                announcementsList = ArrayList<Announcement>()
//                for (announcement in response) {
//                    val currentTime = Calendar.getInstance()
//                    val announcementTime = Calendar.getInstance()
//                    announcementTime.time = Date(announcement.broadcastAt)
//                    if (currentTime.compareTo(announcementTime) != -1) announcementsList.add(announcement)
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
        listAdapter.notifyDataSetChanged()
    }
    companion object {
        //Local datastore pin title.
        val ANNOUNCEMENT_PIN = "announcementPin"

        private val TAG = "MD/Announcements"

        val instance: AnnouncementFragment
            get() = AnnouncementFragment()
    }
}


