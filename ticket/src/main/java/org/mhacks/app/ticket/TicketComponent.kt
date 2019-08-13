package org.mhacks.app.ticket

import dagger.Component
import org.mhacks.app.core.di.BaseFragmentComponent
import org.mhacks.app.core.di.CoreComponent
import org.mhacks.app.core.di.module.FeatureScope

/**
 * Component binding injections for the :events feature module.
 */
@Component(
        modules = [TicketModule::class],
        dependencies = [CoreComponent::class]
)
@FeatureScope
interface TicketComponent : BaseFragmentComponent<TicketDialogFragment> {

    @Component.Builder
    interface Builder {
        fun coreComponent(component: CoreComponent): Builder
        fun build(): TicketComponent

    }
}