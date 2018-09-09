package com.mhacks.app.ui.announcement.view

import android.content.Context
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import org.mhacks.mhacksui.R
import com.mhacks.app.data.models.Announcement
import com.mhacks.app.ui.announcement.presenter.AnnouncementPresenter
import com.mhacks.app.ui.common.NavigationFragment
import kotlinx.android.synthetic.main.fragment_announcements.*
import javax.inject.Inject

/**
 * Fragment to display and update announcements.
 */
class AnnouncementFragment : NavigationFragment(), AnnouncementView {

    private var announcementList: ArrayList<Announcement> = ArrayList()

    override var setTransparent = false

    override var appBarTitle = R.string.title_announcements

    override var layoutResourceID = R.layout.fragment_announcements

    @Inject lateinit var announcementPresenter: AnnouncementPresenter

    private lateinit var adapter: AnnouncementsAdapter

    private var snackBar: Snackbar? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = AnnouncementsAdapter(context!!, announcementList)
        announcements_recycler_view.adapter = adapter
        announcements_recycler_view.layoutManager = LinearLayoutManager(context)
        showProgressBar(getString(R.string.loading_announcements))
        announcementPresenter.loadAnnouncements()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        announcementPresenter.onAttach()
    }

    override fun onDetach() {
        super.onDetach()
        snackBar?.dismiss()
        announcementPresenter.onDetach()
    }

    override fun onGetAnnouncementsSuccess(announcements: List<Announcement>) {
        snackBar?.dismiss()
        showMainContent()
        snackBar = null
        announcementList.addAll(announcements)
        adapter.notifyDataSetChanged()
    }

    override fun onGetAnnouncementsFailure(error: Throwable) {
        showMainContent()
        adapter.notifyDataSetChanged()
        if ((snackBar == null) and (!announcementList.isEmpty())) {
            snackBar = Snackbar.make(view!!,
                    getString(R.string.lost_internet_connection),
                    Snackbar.LENGTH_INDEFINITE)
            snackBar?.show()
        } else showErrorView(R.string.announcement_network_failure) {
            showProgressBar(getString(R.string.loading_announcements))
            announcementPresenter.loadAnnouncements()
        }
    }

    companion object {
        val instance
            get() = AnnouncementFragment()
    }
}

