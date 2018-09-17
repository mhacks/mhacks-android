package com.mhacks.app.ui.welcome.usecase

import com.mhacks.app.data.models.Configuration
import com.mhacks.app.data.repository.ConfigRepository
import com.mhacks.app.di.SingleUseCase
import javax.inject.Inject

class GetAndCacheConfigSingleUseCase @Inject constructor(
        private val configRepository: ConfigRepository)
    : SingleUseCase<Unit, Configuration>() {

    override fun getSingle(parameters: Unit) =
            configRepository
                    .getConfigLocal()
                    .onErrorResumeNext {
                        configRepository.getConfigRemote()
                                .map { configResponse ->
                                    configResponse.configuration
                                }
                    }
                    .doOnSuccess {
                        configRepository.putConfigLocal(it)
                    }!!
}