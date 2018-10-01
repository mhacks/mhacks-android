package com.mhacks.app.ui.common

import android.content.Context
import android.graphics.Color
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.view_progress.view.*
import org.mhacks.mhacksui.R

/**
 * View that displays a progress bar and a message that is the rationale for the progress bar.
 */
class ProgressBarView(context: Context) : ConstraintLayout(context) {

    var loadingText: String? = null
        set(text) {
            progress_textview_loading.text = text
        }

    var isLightMode: Boolean = false
        set(value) {
            if (value) {
                val colorPrimary = ContextCompat.getColor(context, R.color.colorPrimary)
                setBackgroundColor(Color.WHITE)
                progress_progressbar.progressDrawable
                        .setColorFilter(colorPrimary, android.graphics.PorterDuff.Mode.SRC_IN)
                progress_progressbar.indeterminateDrawable
                        .setColorFilter(colorPrimary, android.graphics.PorterDuff.Mode.SRC_IN)
            } else {
                val colorPrimary = ContextCompat.getColor(context, R.color.colorPrimary)

                setBackgroundColor(colorPrimary)

                progress_progressbar.progressDrawable
                        .setColorFilter(Color.WHITE, android.graphics.PorterDuff.Mode.SRC_IN)
                progress_progressbar.indeterminateDrawable
                        .setColorFilter(Color.WHITE, android.graphics.PorterDuff.Mode.SRC_IN)
            }
        }

    init {
        inflate(context, R.layout.view_progress, this)
        isLightMode = false
    }
}