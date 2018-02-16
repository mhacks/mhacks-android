package com.mhacks.app.dagger.component

import com.mhacks.app.ui.main.view.MainActivity
import dagger.Subcomponent
import dagger.android.AndroidInjector

/**
 * Component that injects dependencies into [MainActivity] such as
 * its Presenter and View contracts.
 */
@Subcomponent
interface MainActivityComponent: AndroidInjector<MainActivity> {

    @Subcomponent.Builder
    abstract class Builder: AndroidInjector.Builder<MainActivity>()
}