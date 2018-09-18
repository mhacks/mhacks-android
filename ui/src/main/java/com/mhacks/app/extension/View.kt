package com.mhacks.app.extension

import android.support.design.widget.Snackbar
import android.view.View
import com.mhacks.app.data.models.common.TextMessage

fun View.showSnackBar(duration: Int, textMessage: TextMessage?) {
    textMessage?.textResId?.let {
        Snackbar.make(
                this,
                it,
                duration).show()
    }

    textMessage?.text?.let {
        Snackbar.make(
                this,
                it,
                duration).show()
        }

}