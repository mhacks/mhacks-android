package org.mhacks.app.core.ktx

import android.view.View
import androidx.annotation.StringRes
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import org.mhacks.app.core.R
import org.mhacks.app.core.data.model.Text

fun View.showSnackBar(text: Text) {
    when (text) {
        is Text.TextString -> {
            Snackbar.make(
                    findViewById(android.R.id.content),
                    text.text,
                    Snackbar.LENGTH_SHORT
            ).show()
        }
        is Text.Res -> {
            Snackbar.make(
                    findViewById(android.R.id.content),
                    text.textResId,
                    Snackbar.LENGTH_SHORT
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
                    findViewById(R.id.content),
                    text.text,
                    duration
            )
        }
        is Text.Res -> {
            Snackbar.make(
                    findViewById(R.id.content),
                    text.textResId,
                    Snackbar.LENGTH_SHORT
            )
        }
    }
    snackBar.setAction(actionText) {
        actionCallback()
    }.show()
}



