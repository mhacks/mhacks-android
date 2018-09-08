package com.mhacks.app.ui.ticket.view

import com.mhacks.app.data.models.User

/**
 * Created by jeffreychang on 2/16/18.
 */

interface TicketDialogView {

    fun onGetUserSuccess(user: User)

    fun onGetUserFailure(error: Throwable)
}