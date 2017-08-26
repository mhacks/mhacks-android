package com.mhacks.android.ui.common

import android.content.res.ColorStateList


data class NavigationColor(val primaryColor: Int, val secondaryColor: Int) {
    companion object {
        fun getColorStateList(primaryColor: Int, secondaryColor: Int): ColorStateList {
            val states = arrayOf(intArrayOf(android.R.attr.state_checked), intArrayOf(-android.R.attr.state_checked))
            val colors = intArrayOf(primaryColor, secondaryColor)
            return ColorStateList(states, colors)
        }
    }
}