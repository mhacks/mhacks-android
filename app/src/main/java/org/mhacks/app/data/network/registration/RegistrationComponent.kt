package org.mhacks.app.data.network.registration

import dagger.Component
import org.mhacks.app.core.di.BaseServiceComponent
import org.mhacks.app.core.di.CoreComponent
import org.mhacks.app.core.di.scope.FeatureScope

/**
 * Component binding injections for the :announcements feature module.
 */
@Component(
        modules = [RegistrationModule::class],
        dependencies = [CoreComponent::class]
)
@FeatureScope
interface RegistrationComponent : BaseServiceComponent<RegistrationIntentService> {

    @Component.Builder
    interface Builder {
        fun coreComponent(component: CoreComponent): Builder
        fun build(): RegistrationComponent
    }
}
