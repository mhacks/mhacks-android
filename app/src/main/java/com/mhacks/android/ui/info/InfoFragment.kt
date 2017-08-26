package com.mhacks.android.ui.info

import android.view.View
import com.mhacks.android.ui.common.BaseFragment
import com.mhacks.android.ui.common.NavigationColor
import org.mhacks.android.R

/**
 * Created by jeffreychang on 5/26/17.
 */
class InfoFragment : BaseFragment() {

    override var setTransparent: Boolean = true
    override var AppBarTitle: Int = R.string.title_info

    override var LayoutResourceID: Int = R.layout.fragment_info


    /*override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        email_sign_in_button.setOnClickListener(this);
    }
    override fun onClick(v: View?) {
        (activity as LoginActivity)
                .switchFragment(LoginViewPagerFragment.instance)
    }*/


    companion object {
        val instance
            get() = InfoFragment()
    }
}

