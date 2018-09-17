package com.mhacks.app.extension

import android.support.design.widget.Snackbar
import android.view.View
import com.mhacks.app.data.models.common.TextMessage

fun View.showSnackBar(textMessage: TextMessage?) {
    textMessage?.textResId?.let {
        Snackbar.make(
                this,
                it,
                Snackbar.LENGTH_SHORT).show()
    }

    textMessage?.text?.let {
        Snackbar.make(
                this,
                it,
                Snackbar.LENGTH_INDEFINITE).show()
        }

}