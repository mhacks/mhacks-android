package org.mhacks.app.core.data.model

import androidx.annotation.StringRes

sealed class Text {
    data class TextString(val text: String): Text()
    data class Res(@StringRes val textResId: Int): Text()
}