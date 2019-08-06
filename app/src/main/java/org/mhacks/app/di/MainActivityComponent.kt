package org.mhacks.app.di

import dagger.Subcomponent
import dagger.android.AndroidInjector
import org.mhacks.app.MainActivity

@Subcomponent
interface MainActivityComponent: AndroidInjector<MainActivity> {

    @Subcomponent.Builder
    abstract class Builder: AndroidInjector.Builder<MainActivity>()
}