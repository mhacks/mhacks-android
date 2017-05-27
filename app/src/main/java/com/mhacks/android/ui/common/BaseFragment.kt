package com.mhacks.android.ui.common

import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


/**
 * The base which Fragments in this project will extend.
 */

abstract class BaseFragment : Fragment() {

    private var mCallback: OnNavigationChangeListener? = null

    abstract var FragmentColor: Int set
    abstract var AppBarTitle: Int set
    abstract var NavigationStateList: Int set
    abstract var LayoutResourceID: Int set
    abstract var configureView: (view: View) -> View? set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mCallback = activity as OnNavigationChangeListener
    }

    override final fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        changeColors()
        val view = inflater!!.inflate(LayoutResourceID, container, false)
        configureView(view)
        return view
    }

    private fun changeColors() {
        mCallback!!.setFragmentTitle(AppBarTitle)
        mCallback!!.setActionBarColor(FragmentColor)
//        if (colorResource == android.R.color.transparent) {
//            mCallback.setBottomNavigationColor(setNavigationStateList())
//        }

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            mCallback.setBottomNavigationColor(setNavigationStateList())
            if (FragmentColor == android.R.color.transparent) {
                mCallback!!.setTransparentStatusBar()
            } else {
                mCallback!!.clearTransparentStatusBar()
                mCallback!!.setStatusBarColor(FragmentColor)
                mCallback!!.setLayoutFullScreen()


            }
        }


    }

    interface OnNavigationChangeListener {

        fun setFragmentTitle(title: Int)

        fun setActionBarColor(color: Int)

        fun setTransparentStatusBar()

        fun clearTransparentStatusBar()

        fun setLayoutFullScreen()

        fun setStatusBarColor(color: Int)
    }
}