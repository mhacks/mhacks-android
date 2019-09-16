package org.mhacks.app.data.network.registration

import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import org.mhacks.app.core.di.scope.FeatureScope
import org.mhacks.app.data.network.services.RegistrationService
import retrofit2.Retrofit

@Module
class RegistrationModule {

    @Provides
    @FeatureScope
    fun provideRegistrationService(retrofit: Retrofit): RegistrationService =
            retrofit.create(RegistrationService::class.java)


    @Provides
    @FeatureScope
    fun provideRegistrationPrefProvider(sharedPreferences: SharedPreferences) =
            RegistrationPrefProvider(sharedPreferences)

    @Provides
    @FeatureScope
    fun provideRegistrationRepository(
            registrationService: RegistrationService,
            registrationPrefProvider: RegistrationPrefProvider
    ) =
            RegistrationRepository(registrationService, registrationPrefProvider)

}