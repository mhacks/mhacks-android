package com.mhacks.android.ui.countdown


import android.view.View
import com.mhacks.android.ui.common.BaseFragment
import com.mhacks.android.ui.common.NavigationColor
import org.mhacks.android.R

/**
 * This Fragment is the entry fragment and contains information about where the user is.
 */

class WelcomeFragment : BaseFragment() {

    override var setTransparent: Boolean = false
    override var AppBarTitle: Int = R.string.welcome
    override var LayoutResourceID: Int = R.layout.fragment_welcome
    override var configureView: (view: View) -> Unit? = fun(view: View) {
//                val f = view.findViewById(R.id.ef) as RelativeLayout
//                f.setBackgroundColor(R.color.md_indigo_800)

    }

    companion object {
        val instance get() = WelcomeFragment()
    }

}

