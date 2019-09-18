package org.mhacks.app.pref.di.component

import dagger.Component
import org.mhacks.app.core.di.BaseFragmentComponent
import org.mhacks.app.core.di.CoreComponent
import org.mhacks.app.core.di.scope.FeatureScope
import org.mhacks.app.pref.ui.PrefFragment

@FeatureScope
@Component(
        dependencies = [CoreComponent::class]
)
interface PrefComponent : BaseFragmentComponent<PrefFragment> {

    @Component.Builder
    interface Builder {
        fun coreComponent(component: CoreComponent): Builder
        fun build(): PrefComponent
    }
}
