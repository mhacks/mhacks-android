package org.mhacks.app.game.di.component

import dagger.Component
import org.mhacks.app.core.di.BaseActivityComponent
import org.mhacks.app.core.di.CoreComponent
import org.mhacks.app.core.di.scope.FeatureScope
import org.mhacks.app.game.widget.GameActivity
import org.mhacks.app.game.GameViewModel
import org.mhacks.app.game.di.module.GameDataModule
import org.mhacks.app.game.di.module.GameModule

/**
 * Component binding injections for the :events feature module.
 */
@Component(
        modules = [GameModule::class, GameDataModule::class],
        dependencies = [CoreComponent::class]
)
@FeatureScope
interface GameComponent : BaseActivityComponent<GameActivity> {

    fun gameViewModel(): GameViewModel

    @Component.Builder
    interface Builder {
        fun coreComponent(component: CoreComponent): Builder

        fun build(): GameComponent

    }
}

