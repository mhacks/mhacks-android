package com.mhacks.android.ui.common

import android.os.Build
import android.os.Bundle
import android.support.annotation.ColorRes
import android.support.annotation.StringRes
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.mhacks.android.R


/**
 * The base which Fragments in this project will extend.
 */

abstract class BaseFragment : Fragment() {

    private var mCallback: OnNavigationChangeListener? = null

    abstract var setTransparent: Boolean set
    abstract var AppBarTitle: Int set
    abstract var LayoutResourceID: Int set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mCallback = activity as OnNavigationChangeListener
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        changeColors()
        return inflater?.inflate(LayoutResourceID, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun changeColors() {
        mCallback?.setFragmentTitle(AppBarTitle)
        mCallback?.setActionBarColor(android.R.color.transparent)

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (setTransparent) {
                mCallback?.setActionBarColor(android.R.color.transparent)
                mCallback?.setStatusBarColor(android.R.color.transparent)
                mCallback?.removePadding()

            } else {
                mCallback?.setActionBarColor(R.color.colorPrimary)
                mCallback?.setStatusBarColor(R.color.colorPrimaryDark)
                mCallback?.addPadding()
            }

        } else {

        }
    }

    fun setCustomActionBarColor(@ColorRes res: Int) {
        mCallback?.setActionBarColor(res)
    }

    interface OnNavigationChangeListener {

        fun setFragmentTitle(@StringRes title: Int)

        fun setActionBarColor(@ColorRes color: Int)

        fun setStatusBarColor(color: Int)

        fun setTransparentStatusBar()

        fun clearTransparentStatusBar()

        fun addPadding()

        fun removePadding()
    }
}