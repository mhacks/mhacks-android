package com.mhacks.app.data.model

/**
 * Created by jeffreychang on 9/4/17.
 */



/**
 * Created by boztalay on 6/3/15.
 * Modified by omkarmoghe on 10/3/16
 *
 *
 * Using bits:
 * 1 - Emergency (red)
 * 2- Logistics (blue)
 * 4 - Food (maize mother fucker)
 * 8 - Swag (green)
 * 16 - Sponsor (purple)
 * 32 - Other (brown)
 */

data class Events(
        var title: String,
        var info: String,
        var broadcastAt: Long,
        var category: Int,
        var isApproved: Boolean,
        var isDeleted: Boolean) : ModelObject()