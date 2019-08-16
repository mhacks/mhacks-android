package org.mhacks.app.postannouncement.di

import dagger.Component
import org.mhacks.app.postannouncement.ui.PostAnnouncementDialogFragment
import org.mhacks.app.core.di.BaseFragmentComponent
import org.mhacks.app.core.di.CoreComponent
import org.mhacks.app.core.di.module.FeatureScope

/**
 * Component binding injections for the :qrscan feature module.
 */
@Component(
        modules = [PostAnnouncementModule::class, PostAnnouncementDataModule::class],
        dependencies = [CoreComponent::class]
)
@FeatureScope
interface PostAnnouncementComponent : BaseFragmentComponent<PostAnnouncementDialogFragment> {

    @Component.Builder
    interface Builder {
        fun coreComponent(component: CoreComponent): Builder
        fun build(): PostAnnouncementComponent
    }
}