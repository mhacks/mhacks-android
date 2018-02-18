package com.mhacks.app.ui.announcement.view

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.mhacks.app.R
import com.mhacks.app.data.kotlin.Announcements
import com.mhacks.app.ui.common.NavigationFragment
import kotlinx.android.synthetic.main.fragment_announcements.*

/**
 * Created by jeffreychang on 5/26/17.
 */
class AnnouncementFragment : NavigationFragment(), AnnouncementView {

    private var announcementList: ArrayList<Announcements> = ArrayList()

    override var setTransparent = false

    override var appBarTitle = R.string.title_announcements

    override var layoutResourceID = R.layout.fragment_announcements

    private lateinit var adapter: AnnouncementsAdapter

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = AnnouncementsAdapter(context, announcementList)
        announcements_recycler_view.adapter = adapter
        announcements_recycler_view.layoutManager = LinearLayoutManager(context)
    }

    companion object {
        val instance
            get() = AnnouncementFragment()
    }
}

