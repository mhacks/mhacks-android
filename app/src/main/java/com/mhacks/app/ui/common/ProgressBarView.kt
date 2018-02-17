package com.mhacks.app.ui.common

import android.content.Context
import android.widget.RelativeLayout
import com.mhacks.app.R
import kotlinx.android.synthetic.main.view_progress.view.*

/**
 * Created by jeffreychang on 2/17/18.
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
    }
}