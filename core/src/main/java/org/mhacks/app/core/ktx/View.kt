package org.mhacks.app.core.ktx

import android.view.View
import com.google.android.material.snackbar.Snackbar
import org.mhacks.app.core.data.model.Text

fun View.showSnackBar(text: Text) {
    when (text) {
        is Text.String -> {
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



