package org.mhacks.app.eventlibrary.di

import dagger.Subcomponent
import org.mhacks.app.eventlibrary.EventViewModel
import org.mhacks.app.eventlibrary.di.module.EventDataModule
import org.mhacks.app.eventlibrary.di.module.EventModule

/**
 * dependencies for events
 */
@Subcomponent(modules = [EventModule::class, EventDataModule::class])
interface EventLibraryComponent {

    fun eventViewModel(): EventViewModel

    @Subcomponent.Builder
    interface Builder {

        fun build(): EventLibraryComponent

    }
}