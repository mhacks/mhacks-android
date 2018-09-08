package com.mhacks.app.ui.common

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.support.annotation.ColorRes
import android.support.annotation.StringRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mhacks.app.R

/**
 * The base which Fragments in this project will extend.
 */
abstract class NavigationFragment : BaseFragment() {

    private var callback: OnNavigationChangeListener? = null

    abstract var setTransparent: Boolean

    abstract var appBarTitle: Int

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        callback = activity as? OnNavigationChangeListener
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        changeColors()
        return super.onCreateView(inflater, container, savedInstanceState)
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

    interface OnNavigationChangeListener {

        fun setFragmentTitle(@StringRes title: Int)

        fun setActionBarColor(@ColorRes color: Int)

        fun setStatusBarColor(color: Int)

        fun addPadding()

        fun removePadding()
    }
}