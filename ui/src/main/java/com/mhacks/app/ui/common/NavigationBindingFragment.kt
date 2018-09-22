package com.mhacks.app.ui.common

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.support.annotation.ColorRes
import android.support.annotation.StringRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.mhacks.mhacksui.R

/**
 * The base which Fragments in this project will extend.
 */
abstract class NavigationBindingFragment : BaseBindingFragment() {

    private var callback: NavigationFragment.OnNavigationChangeListener? = null

    abstract var setTransparent: Boolean

    abstract var appBarTitle: Int

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        callback = activity as? NavigationFragment.OnNavigationChangeListener
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        changeColors()
    }

    private fun changeColors() {
        callback?.setFragmentTitle(appBarTitle)
        callback?.setActionBarColor(android.R.color.transparent)

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (setTransparent) {
                callback?.setActionBarColor(android.R.color.transparent)
                callback?.setStatusBarColor(android.R.color.transparent)
                callback?.removePadding()

            } else {
                callback?.setActionBarColor(R.color.colorPrimary)
                callback?.setStatusBarColor(R.color.colorPrimaryDark)
                callback?.addPadding()
            }
        }
    }

    fun setCustomActionBarColor(@ColorRes res: Int) {
        callback?.setActionBarColor(res)
    }

}