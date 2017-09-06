package com.mhacks.android.data.kotlin

import com.mhacks.android.data.model.Login

/**
 * Created by jeffreychang on 9/6/17.
 */
interface NetworkCallback<in T> {

    fun onResponseSuccess(response: T)

    fun onResponseFailure(error: Throwable)
}