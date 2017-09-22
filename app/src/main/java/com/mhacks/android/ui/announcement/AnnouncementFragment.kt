package com.mhacks.android.ui.announcement

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.mhacks.android.data.kotlin.Announcements
import com.mhacks.android.ui.common.BaseFragment
import io.reactivex.Observable
import kotlinx.android.synthetic.main.fragment_announcements.*
import org.mhacks.x.R
import timber.log.Timber
import java.util.concurrent.TimeUnit

/**
 * Created by jeffreychang on 5/26/17.
 */
class AnnouncementFragment : BaseFragment() {

    private var announcementList: ArrayList<Announcements> = ArrayList()

    override var setTransparent = false

    override var AppBarTitle = R.string.title_announcements

    override var LayoutResourceID = R.layout.fragment_announcements

    private val callback by lazy { activity as Callback }

    private lateinit var adapter: AnnouncementsAdapter

    override fun onResume() {
        super.onResume()

        Observable.interval(0,  TimeUnit.MILLISECONDS)
        callback.fetchAnnouncements(
                { announcements ->
                    announcementList.clear()
                    announcementList.addAll(announcements)
                    adapter.notifyDataSetChanged()
                },
                { error -> Timber.e(error) })
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = AnnouncementsAdapter(context, announcementList)
        announcements_recycler_view.adapter = adapter
        announcements_recycler_view.layoutManager = LinearLayoutManager(context)
    }


    interface Callback {
        fun fetchAnnouncements(
                success: (announcements: List<Announcements>) -> Unit,
                failure: (error: Throwable) -> Unit)
    }

    companion object {
        val instance
            get() = AnnouncementFragment()
    }
}

