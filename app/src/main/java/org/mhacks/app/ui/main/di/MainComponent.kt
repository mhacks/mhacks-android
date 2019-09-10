package org.mhacks.app.ui.main.di

import dagger.Component
import org.mhacks.app.core.di.BaseActivityComponent
import org.mhacks.app.core.di.CoreComponent
import org.mhacks.app.core.di.scope.FeatureScope
import org.mhacks.app.ui.main.MainActivity
import org.mhacks.app.ui.main.di.module.MainDataModule
import org.mhacks.app.ui.main.di.module.MainModule

/**
 * Component binding injections for the :events feature module.
 */
@Component(
        modules = [MainModule::class, MainDataModule::class],
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