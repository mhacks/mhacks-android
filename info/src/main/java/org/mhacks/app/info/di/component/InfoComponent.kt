package org.mhacks.app.info.di.component

import dagger.Component
import org.mhacks.app.core.di.BaseFragmentComponent
import org.mhacks.app.core.di.CoreComponent
import org.mhacks.app.core.di.scope.FeatureScope

import org.mhacks.app.info.di.module.InfoModule

import org.mhacks.app.info.widget.InfoFragment

/**
 * Component binding injections for the :events feature module.
 */
@Component(
        modules = [InfoModule::class ],
        dependencies = [CoreComponent::class]
)
@FeatureScope
interface InfoComponent : BaseFragmentComponent<InfoFragment> {

    @Component.Builder
    interface Builder {
        fun coreComponent(component: CoreComponent): Builder

        fun build(): InfoComponent

    }
}

