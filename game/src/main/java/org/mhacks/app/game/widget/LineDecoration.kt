package org.mhacks.app.game.widget

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import org.mhacks.app.core.ktx.convertDpToPixel

class LineDecoration : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State) {
        val dp = view.context.convertDpToPixel(8)
        outRect.set(dp, dp, dp, dp)
    }

}