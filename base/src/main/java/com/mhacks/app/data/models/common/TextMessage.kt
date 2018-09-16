package com.mhacks.app.data.models.common

import android.support.annotation.StringRes

data class StringMessage(
        @StringRes val textResId: Int?,
        val text: String?)