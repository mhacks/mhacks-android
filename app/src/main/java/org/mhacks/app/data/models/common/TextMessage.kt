package org.mhacks.app.data.models.common

import androidx.annotation.StringRes

data class TextMessage(
        @StringRes val textResId: Int?,
        val text: String?)