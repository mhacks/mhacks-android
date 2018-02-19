package com.mhacks.app.ui.common

import android.content.Context
import android.graphics.Color
import android.widget.RelativeLayout
import com.mhacks.app.R
import kotlinx.android.synthetic.main.view_progress.view.*

/**
 * View that shows a ProgressBar. Used when loading content.
 */

/**
 * View that displays a progress bar and a message that is the rationale for the progress bar.
 */
class ProgressBarView(context: Context) : RelativeLayout(context) {

    var loadingText: String? = null
        set(text) {
            progress_textview_loading.text = text
        }

    init {
        inflate(context, R.layout.view_progress, this)
        progress_progressbar.progressDrawable
                .setColorFilter(Color.WHITE, android.graphics.PorterDuff.Mode.SRC_IN)
        progress_progressbar.indeterminateDrawable
                .setColorFilter(Color.WHITE, android.graphics.PorterDuff.Mode.SRC_IN)

    }
}