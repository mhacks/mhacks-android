package org.mhacks.app.core.data.model

import androidx.annotation.StringRes

sealed class Text {
    data class String(val text: kotlin.String): Text()
    data class Res(@StringRes val textResId: Int): Text()
}