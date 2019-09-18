package org.mhacks.app.core.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.facebook.flipper.plugins.network.NetworkFlipperPlugin
import dagger.BindsInstance
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
            DomainModule::class,
            ViewModelModule::class
        ])
interface CoreComponent {

    fun retrofit(): Retrofit

    fun context(): Context

    fun authRepository(): AuthRepository

    fun userRepository(): UserRepository

    fun sharedPreferences(): SharedPreferences

    fun networkFlipperPlugin(): NetworkFlipperPlugin

    @Component.Builder
    interface Builder {

        @BindsInstance fun application(application: Application): Builder

        fun build(): CoreComponent
    }
}
