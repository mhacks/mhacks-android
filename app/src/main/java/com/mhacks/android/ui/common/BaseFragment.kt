package com.mhacks.android.ui.common

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


/**
 * The base which Fragments in this project will extend.
 */

abstract class BaseFragment constructor(fragmentID: Int) : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(setViewLayout(), container, savedInstanceState)

    }

    abstract fun setViewLayout(): Int

}