package org.mhacks.app

import dagger.Component
import org.mhacks.app.core.di.CoreComponent
import org.mhacks.app.core.di.scope.AppScope

/**
 * Component binding injections for dependencies required in the MHacksApplication
 */
@Component(dependencies = [CoreComponent::class])
@AppScope
interface ApplicationComponent {

    fun inject(mHacksApplication: MHacksApplication)

    @Component.Builder
    interface Builder {
        fun coreComponent(component: CoreComponent): Builder

        fun build(): ApplicationComponent
    }
}
