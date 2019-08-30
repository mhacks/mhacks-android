package org.mhacks.app.eventlibrary.di

import dagger.Subcomponent
import org.mhacks.app.core.domain.auth.data.dao.AuthDao
import org.mhacks.app.core.domain.auth.di.PrivateToAuth
import org.mhacks.app.eventlibrary.EventViewModel
import org.mhacks.app.eventlibrary.data.db.EventDao
import org.mhacks.app.eventlibrary.data.service.EventService
import org.mhacks.app.eventlibrary.di.module.EventDataModule
import org.mhacks.app.eventlibrary.di.module.EventModule

/**
 * dependencies for events
 */
@Subcomponent(modules = [EventModule::class, EventDataModule::class])
interface EventComponent {

    fun eventViewModel(): EventViewModel

    @Subcomponent.Builder
    interface Builder {

        fun build(): EventComponent

    }
}