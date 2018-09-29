package com.mhacks.app.ui.info

import com.mhacks.app.ui.info.model.Info
import org.mhacks.mhacksui.R

fun getInfoList() =
        arrayListOf(
                Info(
                        "wifi",
                        R.string.info_wifi_header,
                        R.string.info_wifi_sub_header,
                        R.string.info_wifi_desc,
                        R.drawable.ic_wifi_white_24dp
                ),
                Info(
                        "address",
                        R.string.info_address_header,
                        R.string.info_address_sub_header,
                        R.string.info_address_desc,
                        R.drawable.ic_place_black_24dp
                ),
                Info(
                        "slack",
                        R.string.info_slack_header,
                        R.string.info_slack_sub_header,
                        R.string.info_slack_desc,
                        R.drawable.ic_slack
                ),
                Info(
                        "email",
                        R.string.info_email_header,
                        R.string.info_email_sub_header,
                        R.string.info_email_desc,
                        R.drawable.ic_email_black_24dp
                )
        )