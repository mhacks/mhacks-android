package com.mhacks.android.ui.info

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.mhacks.android.R
<<<<<<< HEAD
=======
import kotlinx.android.synthetic.*

>>>>>>> 25d20084221796ac0f9aab2625b86871f6730f93

/**
 * Created by jeffreychang on 5/26/17.
 */
class InfoFragment : Fragment() {
//
//    override var FragmentColor: Int = android.R.color.holo_green_light
//    override var AppBarTitle: Int = R.string.welcome
//    override var NavigationStateList: Int = R.color.darkBlue
//    override var LayoutResourceID: Int = R.layout.fragment_info
//    override var configureView: (view: View) -> Unit? = fun(view: View) {
//
//
//    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_info, container, false)

        return view
    }

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

