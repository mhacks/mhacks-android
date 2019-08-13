package org.mhacks.app.core.widget

import android.content.res.ColorStateList
import android.graphics.drawable.ColorDrawable
import android.widget.FrameLayout
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.mhacks.app.core.R
import org.mhacks.app.core.ktx.convertDpResToPixel

abstract class NavigationActivity : BaseActivity(), NavigationFragment.OnNavigationChangeListener {

    abstract val bottomNavigationView: BottomNavigationView

    abstract val containerView: FrameLayout

    fun setBottomNavigationColor(color: NavigationColor) {
        bottomNavigationView.itemIconTintList = color.getColorStateList()
        bottomNavigationView.itemTextColor = color.getColorStateList()
    }

    // Used for Map View to have a transparent Action Bar.
    override fun addPadding() {
        val height = convertDpResToPixel(res = R.dimen.toolbar_height)
        containerView.setPadding(0, height, 0, 0)
    }

    override fun removePadding() {
        containerView.setPadding(0, 0, 0, 0)
    }

    override fun setStatusBarColor(color: Int) {
        window.statusBarColor = ContextCompat.getColor(this, color)
    }

    override fun setActionBarColor(@ColorRes colorRes: Int) {
        val color = ContextCompat.getColor(this, colorRes)
        supportActionBar?.setBackgroundDrawable(ColorDrawable(color))
    }

    override fun setFragmentTitle(title: Int) = setTitle(title)

}

data class NavigationColor(val primaryColor: Int, val secondaryColor: Int) {
    fun getColorStateList(): ColorStateList {
        val states = arrayOf(
                intArrayOf(android.R.attr.state_checked),
                intArrayOf(-android.R.attr.state_checked)
        )
        val colors = intArrayOf(primaryColor, secondaryColor)
        return ColorStateList(states, colors)
    }
}