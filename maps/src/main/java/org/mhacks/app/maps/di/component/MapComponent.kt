package org.mhacks.app.maps.di.component

import dagger.Component
import org.mhacks.app.core.di.BaseFragmentComponent
import org.mhacks.app.core.di.CoreComponent
import org.mhacks.app.core.di.scope.FeatureScope
import org.mhacks.app.maps.di.module.MapDataModule
import org.mhacks.app.maps.di.module.MapModule
import org.mhacks.app.maps.widget.MapViewFragment

/**
 * Component binding injections for the :maps feature module.
 */
@Component(
        modules = [MapModule::class, MapDataModule::class],
        dependencies = [CoreComponent::class]
)
@FeatureScope
interface MapComponent : BaseFragmentComponent<MapViewFragment> {

    @Component.Builder
    interface Builder {
        fun coreComponent(component: CoreComponent): Builder
        fun build(): MapComponent
    }
}
