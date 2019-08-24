package org.mhacks.app.announcements.di

import org.mhacks.app.coreComponent
import org.mhacks.app.announcements.di.component.DaggerAnnouncementComponent
import org.mhacks.app.announcements.widget.AnnouncementFragment

fun AnnouncementFragment.inject() {
    DaggerAnnouncementComponent.builder()
            .coreComponent(coreComponent())
            .build()
            .inject(this)
}