package com.mhacks.app.ui.welcome.usecase

import com.mhacks.app.data.models.Configuration
import com.mhacks.app.data.repository.ConfigRepository
import com.mhacks.app.mvvm.SingleUseCase
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class GetAndCacheConfigUseCase @Inject constructor(
        private val configRepository: ConfigRepository)
    : SingleUseCase<Unit, Configuration>() {

    override fun getSingle(parameters: Unit) =
            configRepository
                    .getConfigLocal()
                    .delay(400, TimeUnit.MILLISECONDS)
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