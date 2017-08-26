package com.mhacks.android.ui.common

import android.os.Build
import android.os.Bundle
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

    override final   fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        changeColors()
        val view = inflater!!.inflate(LayoutResourceID, container, false)
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    /* http://mrtn.me/blog/2012/03/17/get-the-height-of-the-status-bar-in-android/
        Copied code to get the length of the status bar.
     */

    fun getStatusBarHeight(): Int {
        var result = 0
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

    private fun changeColors() {
        mCallback!!.setFragmentTitle(AppBarTitle)
        mCallback!!.setActionBarColor(android.R.color.transparent)

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (setTransparent) {
                mCallback!!.setActionBarColor(android.R.color.transparent)
                mCallback!!.setStatusBarColor(android.R.color.transparent)
                mCallback!!.removePadding()

            } else {
                mCallback!!.setActionBarColor(R.color.primary)
                mCallback!!.setStatusBarColor(R.color.primary_dark)
                mCallback!!.addPadding()
            }

        } else {

        }
    }

    interface OnNavigationChangeListener {

        fun setFragmentTitle(title: Int)

        fun setActionBarColor(color: Int)

        fun setStatusBarColor(color: Int)

        fun setTransparentStatusBar()

        fun clearTransparentStatusBar()

        fun addPadding()

        fun removePadding()
    }
}