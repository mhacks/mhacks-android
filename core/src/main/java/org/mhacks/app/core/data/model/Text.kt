package org.mhacks.app.core.data.model

import android.view.View
import androidx.annotation.StringRes
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar

sealed class Text {
    data class TextString(val text: String) : Text()
    data class Res(@StringRes val textResId: Int) : Text()
}

fun Text.showSnackBar(
        view: View,
        @BaseTransientBottomBar.Duration length: Int = BaseTransientBottomBar.LENGTH_SHORT
) {
    if (this is Text.TextString) {
        Snackbar.make(view, text, length).show()
    } else if (this is Text.Res) {
        Snackbar.make(view, textResId, length).show()
    }
}