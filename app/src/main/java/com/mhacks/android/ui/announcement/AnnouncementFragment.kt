package com.mhacks.android.ui.announcement

import android.os.Bundle
import android.view.View
import com.mhacks.android.ui.common.BaseFragment
import org.mhacks.android.R

/**
 * Created by jeffreychang on 5/26/17.
 */
class AnnouncementFragment : BaseFragment() {

    override var setTransparent: Boolean = true
    override var AppBarTitle: Int = R.string.title_announcements

    override var LayoutResourceID: Int = R.layout.fragment_announcements

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    companion object {
        val instance
            get() = AnnouncementFragment()
    }
}

