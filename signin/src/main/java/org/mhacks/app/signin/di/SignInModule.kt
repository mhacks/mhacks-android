package org.mhacks.app.signin.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import org.mhacks.app.core.di.ViewModelKey
import org.mhacks.app.signin.ui.SignInViewModel

/**
 * Module that provides dependencies needed for login sign-in.
 */
@Module
abstract class SignInModule {

    @Binds
    @IntoMap
    @ViewModelKey(SignInViewModel::class)
    abstract fun bindSignInViewModule(signInViewModel: SignInViewModel): ViewModel

}