package com.mhacks.app.ui.welcome

import dagger.Binds
import dagger.Module

/**
 * Provides dependencies for special blend module.
 */
@Module
abstract class WelcomeFragmentModule {

    @Binds
    abstract fun bindsWelcomeFragment(specialBlendFragment: WelcomeFragment): WelcomeFragment
}