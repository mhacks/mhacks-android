package com.mhacks.app.ui.announcement.view

import android.content.Context
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.mhacks.app.R
import com.mhacks.app.data.kotlin.Announcement
import com.mhacks.app.ui.announcement.presenter.AnnouncementPresenter
import com.mhacks.app.ui.common.NavigationFragment
import kotlinx.android.synthetic.main.fragment_announcements.*
import javax.inject.Inject

/**
 * Created by jeffreychang on 5/26/17.
 */
class AnnouncementFragment : NavigationFragment(), AnnouncementView {

    private var announcementList: ArrayList<Announcement> = ArrayList()

    override var setTransparent = false

    override var appBarTitle = R.string.title_announcements

    override var layoutResourceID = R.layout.fragment_announcements

    @Inject lateinit var announcementPresenter: AnnouncementPresenter

    private lateinit var adapter: AnnouncementsAdapter

    private var snackbar: Snackbar? = null

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = AnnouncementsAdapter(context, announcementList)
        announcements_recycler_view.adapter = adapter
        announcements_recycler_view.layoutManager = LinearLayoutManager(context)
        announcementPresenter.loadAnnouncements()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        announcementPresenter.onAttach()
    }

    override fun onDetach() {
        super.onDetach()
        announcementPresenter.onDetach()
    }

    override fun onGetAnnouncementsSuccess(announcements: List<Announcement>) {
        snackbar?.dismiss()
        snackbar = null
        announcementList.clear()
        announcementList.addAll(announcements)
        adapter.notifyDataSetChanged()
    }

    override fun onGetAnnouncementsFailure(error: Throwable) {
        if (snackbar == null) {
            snackbar = Snackbar.make(view!!,
                    getString(R.string.lost_internet_connection),
                    Snackbar.LENGTH_INDEFINITE)
            snackbar?.show()
        }
    }

    companion object {
        val instance
            get() = AnnouncementFragment()
    }
}

