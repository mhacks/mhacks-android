package com.mhacks.app.data.models.common

import android.support.annotation.StringRes

data class SnackbarMessage(
        @StringRes val textResId: Int,
        @StringRes val actionResId: Int?)