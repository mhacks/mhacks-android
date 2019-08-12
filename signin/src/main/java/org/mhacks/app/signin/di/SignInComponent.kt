package org.mhacks.app.signin.di

import dagger.Component
import org.mhacks.app.core.di.BaseFragmentComponent
import org.mhacks.app.core.di.CoreComponent
import org.mhacks.app.core.di.module.FeatureScope
import org.mhacks.app.signin.ui.SignInFragment

/**
 * Component binding injections for the :signin feature module.
 */
@Component(
        modules = [SignInModule::class],
        dependencies = [CoreComponent::class]
)
@FeatureScope
interface SignInComponent : BaseFragmentComponent<SignInFragment> {

    @Component.Builder
    interface Builder {
        fun coreComponent(component: CoreComponent): Builder
        fun build(): SignInComponent
    }
}