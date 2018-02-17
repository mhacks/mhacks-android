package com.mhacks.app.dagger.component

import com.mhacks.app.ui.login.LoginActivity
import dagger.Subcomponent
import dagger.android.AndroidInjector

/**
 * Component that injects dependencies into [LoginActivity] such as
 * its Presenter and View contracts.
 */
@Subcomponent
interface LoginActivityComponent : AndroidInjector<LoginActivity> {

    @Subcomponent.Builder
    abstract class Builder: AndroidInjector.Builder<LoginActivity>()
}