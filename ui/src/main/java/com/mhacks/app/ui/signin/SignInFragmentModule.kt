package com.mhacks.app.ui.signin

import androidx.lifecycle.ViewModel
import com.mhacks.app.di.ViewModelKey
import com.mhacks.app.ui.signin.SignInViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Module that provides dependencies needed for login sign-in.
 */

@Module
abstract class SignInFragmentModule {

    @Binds
    @IntoMap
    @ViewModelKey(SignInViewModel::class)
    abstract fun bindSignInViewModule(signInViewModel: SignInViewModel): ViewModel

}