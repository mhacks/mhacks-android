package com.mhacks.android.ui.announcements

import android.util.Log
import com.mhacks.android.data.model.Announcement
import com.mhacks.android.ui.announcements.AnnouncementSection.TimeLineType
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter

/**
 * Created by jeffreychang on 9/1/17.
 */
class SectionedAnnouncementAdapter(announcementSectionList: ArrayList<AnnouncementSection.AnnouncementSectionModel>)
    : SectionedRecyclerViewAdapter() {
    init {
        for (it in 0 until announcementSectionList.size) {

            val type: TimeLineType = when (it) {
                0 -> {
                    TimeLineType.START
                }
                announcementSectionList.size - 1 -> {
                    TimeLineType.END
                }
                else -> {
                    TimeLineType.NORMAL
                }
            }

            addSection(AnnouncementSection(
                    time = announcementSectionList[it].time,
                    type = type,
                    announcements = announcementSectionList[it].announcements))
        }
    }
}
