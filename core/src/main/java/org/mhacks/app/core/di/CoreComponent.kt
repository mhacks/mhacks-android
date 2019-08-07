package org.mhacks.app.core.di

import dagger.Component
import org.mhacks.app.core.di.module.DataModule
import org.mhacks.app.core.di.module.ViewModelModule
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * Component that injects into Android members (e.g. Activities and Fragments) with various
 * modules that provide tasks such as networking and caching in a database.
 */
@Singleton
@Component(
        modules = [
            AppModule::class,
            DataModule::class,
            ViewModelModule::class
        ])
interface CoreComponent {

    fun retrofit(): Retrofit

    @Component.Builder
    interface Builder {

        fun appModule(appModule: AppModule): Builder

        fun build(): CoreComponent
    }
}
