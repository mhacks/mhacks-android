package org.mhacks.app.core.ktx

import android.view.View
import androidx.annotation.StringRes
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import org.mhacks.app.core.data.model.Text

fun View.showSnackBar(
        text: Text,
        @BaseTransientBottomBar.Duration duration: Int = BaseTransientBottomBar.LENGTH_SHORT) {
    when (text) {
        is Text.TextString -> {
            Snackbar.make(
                    this,
                    text.text,
                    duration
            ).show()
        }
        is Text.Res -> {
            Snackbar.make(
                    this,
                    text.textResId,
                    duration
            ).show()
        }
    }
}

fun View.showSnackBar(
        @BaseTransientBottomBar.Duration duration: Int,
        text: Text,
        @StringRes actionText: Int,
        actionCallback: (() -> Unit)) {
    val snackBar: Snackbar = when (text) {
        is Text.TextString -> {
            Snackbar.make(
                    this,
                    text.text,
                    duration
            )
        }
        is Text.Res -> {
            Snackbar.make(
                    this,
                    text.textResId,
                    duration
            )
        }
    }
    snackBar.setAction(actionText) {
        actionCallback()
    }.show()
}



