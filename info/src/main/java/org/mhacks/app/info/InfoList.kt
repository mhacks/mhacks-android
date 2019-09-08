package org.mhacks.app.info

import org.mhacks.app.info.data.model.Info

fun getInfoList() =
        arrayListOf(
                Info(
                        Info.TYPE.WIFI,
                        R.string.info_wifi_header,
                        R.string.info_wifi_sub_header,
                        R.string.info_wifi_desc,
                        R.drawable.ic_wifi_white_24dp
                ),
                Info(
                        Info.TYPE.ADDRESS,
                        R.string.info_address_header,
                        R.string.info_address_sub_header,
                        R.string.info_address_desc,
                        R.drawable.ic_place_black_24dp
                ),
                Info(
                        Info.TYPE.SLACK,
                        R.string.info_slack_header,
                        R.string.info_slack_sub_header,
                        R.string.info_slack_desc,
                        R.drawable.ic_slack
                ),
                Info(
                        Info.TYPE.EMAIL,
                        R.string.info_email_header,
                        R.string.info_email_sub_header,
                        R.string.info_email_desc,
                        R.drawable.ic_email_black_24dp
                )
        )