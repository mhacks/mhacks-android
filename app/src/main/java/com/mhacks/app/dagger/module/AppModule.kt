package com.mhacks.app.dagger.module

import android.app.Application
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by jeffreychang on 9/2/17.
 */


@Module class AppModule(internal var application: Application) {

    @Provides
    @Singleton internal fun provideApplication(): Application {
        return application
    }
}
