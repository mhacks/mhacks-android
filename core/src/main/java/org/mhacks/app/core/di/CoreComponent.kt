package org.mhacks.app.core.di

import android.content.Context
import dagger.Component
import org.mhacks.app.core.di.module.DataModule
import org.mhacks.app.core.di.module.DomainModule
import org.mhacks.app.core.di.module.ViewModelModule
import org.mhacks.app.core.domain.auth.AuthRepository
import org.mhacks.app.core.domain.user.UserRepository
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
            DataModule::class,
            DomainModule::class,
            ViewModelModule::class
        ])
interface CoreComponent {

    fun retrofit(): Retrofit

    fun context(): Context

    fun authRepository(): AuthRepository

    fun userRepository(): UserRepository

    @Component.Builder
    interface Builder {

        fun appModule(appModule: AppModule): Builder

        fun build(): CoreComponent
    }
}
