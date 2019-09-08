package org.mhacks.app.info.widget

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import org.mhacks.app.core.ktx.convertDpToPixel

class SpacingItemDecoration(
        private val context: Context,
        private val marginInDp: Int): RecyclerView.ItemDecoration() {


    override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State) {

        val dp = context.convertDpToPixel(marginInDp)
        outRect.set(dp, dp, dp, dp)
    }


}