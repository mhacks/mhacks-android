package com.mhacks.app.data.models.common

import android.support.annotation.StringRes

data class TextMessage(
        @StringRes val textResId: Int?,
        val text: String?)