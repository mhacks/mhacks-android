package com.mhacks.app.ui.common

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.mhacks.app.util.ResourceUtil

class SpacingItemDecoration(
        private val context: Context,
        private val marginInDp: Int): RecyclerView.ItemDecoration() {


    override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State) {

        val dp = ResourceUtil.convertDpToPixel(context, marginInDp)
        outRect.set(dp, dp, dp, dp)
    }


}