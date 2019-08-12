package org.mhacks.app.signin

import org.mhacks.app.coreComponent
import org.mhacks.app.signin.di.DaggerSignInComponent
import org.mhacks.app.signin.ui.SignInFragment

fun SignInFragment.inject() {
    DaggerSignInComponent.builder()
            .coreComponent(coreComponent())
            .build()
            .inject(this)
}