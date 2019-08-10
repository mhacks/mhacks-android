package org.mhacks.app.ui.main.di

import dagger.Component
import org.mhacks.app.core.di.BaseActivityComponent
import org.mhacks.app.core.di.CoreComponent
import org.mhacks.app.core.di.module.FeatureScope
import org.mhacks.app.di.MainActivityModule
import org.mhacks.app.ui.main.MainActivity

/**
 * Component binding injections for the :events feature module.
 */
@Component(
        modules = [MainActivityModule::class],
        dependencies = [CoreComponent::class]
)
@FeatureScope
interface MainComponent : BaseActivityComponent<MainActivity> {

    @Component.Builder
    interface Builder {
        fun coreComponent(component: CoreComponent): Builder

        fun build(): MainComponent
    }
}