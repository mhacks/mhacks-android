package org.mhacks.app.welcome.di.component

import dagger.Component
import org.mhacks.app.core.di.BaseFragmentComponent
import org.mhacks.app.core.di.CoreComponent
import org.mhacks.app.core.di.module.FeatureScope
import org.mhacks.app.eventlibrary.di.EventLibraryComponent
import org.mhacks.app.welcome.di.module.WelcomeDataModule
import org.mhacks.app.welcome.di.module.WelcomeModule
import org.mhacks.app.welcome.widget.WelcomeFragment

/**
 * Component binding injections for the :welcome feature module.
 */
@Component(
        modules = [WelcomeModule::class, WelcomeDataModule::class],
        dependencies = [CoreComponent::class, EventLibraryComponent::class]
)
@FeatureScope
interface WelcomeComponent : BaseFragmentComponent<WelcomeFragment> {

    @Component.Builder
    interface Builder {

        fun coreComponent(component: CoreComponent): Builder

        fun eventLibraryComponent(component: EventLibraryComponent): Builder

        fun build(): WelcomeComponent
    }
}

