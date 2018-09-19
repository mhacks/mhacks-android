package com.mhacks.app.di.component

import com.mhacks.app.ui.signin.SignInActivity
import dagger.Subcomponent
import dagger.android.AndroidInjector

/**
 * Component that injects dependencies into [SignInActivity] such as
 * its Presenter and View contracts.
 */
@Subcomponent
interface LoginActivityComponent : AndroidInjector<SignInActivity> {

    @Subcomponent.Builder
    abstract class Builder: AndroidInjector.Builder<SignInActivity>()
}