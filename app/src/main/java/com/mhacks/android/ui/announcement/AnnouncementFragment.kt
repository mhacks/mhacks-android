package com.mhacks.android.ui.announcement

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.mhacks.android.data.model.Announcement
import com.mhacks.android.ui.common.BaseFragment
import kotlinx.android.synthetic.main.fragment_announcements.*
import org.mhacks.android.R

/**
 * Created by jeffreychang on 5/26/17.
 */
class AnnouncementFragment : BaseFragment() {

    override var setTransparent = false
    override var AppBarTitle = R.string.title_announcements
    override var LayoutResourceID = R.layout.fragment_announcements

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        announcements_recycler_view.adapter = AnnouncementAdapter(context, getMockData())
        announcements_recycler_view.layoutManager = LinearLayoutManager(context)
    }


    fun getMockData(): ArrayList<Announcement> {
        val mock = ArrayList<Announcement>()
        mock.add(Announcement("Hello World", "11/13/2011", "This is " +
                "hello world. And some sample text."))
        mock.add(Announcement("Hello World", "11/13/2011", "This is " +
                "hello world. And some sample text."))
        mock.add(Announcement("Hello World", "11/13/2011", "This is " +
                "hello world. And some sample text."))

        return mock
    }
    companion object {
        val instance
            get() = AnnouncementFragment()
    }
}

