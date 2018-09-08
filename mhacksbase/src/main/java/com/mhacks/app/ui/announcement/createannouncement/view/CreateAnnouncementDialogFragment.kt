package com.mhacks.app.ui.announcement.createannouncement.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mhacks.app.R
import com.mhacks.app.di.common.DaggerDialogFragment

/**
 * Fragment used to create and post a new announcement.
 */
class CreateAnnouncementDialogFragment: DaggerDialogFragment(), CreateAnnouncementView {


    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_create_announcements, container, false)
    }

    override fun onCreateAnnouncementSuccess() {

    }

    override fun onCreateAnnouncementFailure() {

    }

    companion object {
         val instance = CreateAnnouncementDialogFragment()

    }
}