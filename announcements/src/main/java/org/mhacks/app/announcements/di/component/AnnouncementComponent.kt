package org.mhacks.app.announcements.di.component

import dagger.Component
import org.mhacks.app.core.di.BaseFragmentComponent
import org.mhacks.app.core.di.CoreComponent
import org.mhacks.app.core.di.module.FeatureScope
import org.mhacks.app.announcements.di.module.AnnouncementDataModule
import org.mhacks.app.announcements.di.module.AnnouncementModule
import org.mhacks.app.announcements.widget.AnnouncementFragment

/**
 * Component binding injections for the :announcements feature module.
 */
@Component(
        modules = [AnnouncementModule::class, AnnouncementDataModule::class],
        dependencies = [CoreComponent::class]
)
@FeatureScope
interface AnnouncementComponent : BaseFragmentComponent<AnnouncementFragment> {

    @Component.Builder
    interface Builder {
        fun coreComponent(component: CoreComponent): Builder
        fun build(): AnnouncementComponent
    }
}
