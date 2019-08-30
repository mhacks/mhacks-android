package org.mhacks.app.welcome.usecase

import org.mhacks.app.welcome.data.model.Configuration
import org.mhacks.app.welcome.ConfigRepository
import org.mhacks.app.core.usecase.SingleUseCase
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class GetAndCacheConfigUseCase @Inject constructor(
        private val configRepository: ConfigRepository)
    : SingleUseCase<Unit, Configuration>() {

    override fun getSingle(parameters: Unit) =
            configRepository
                    .getConfigCache()
                    .delay(400, TimeUnit.MILLISECONDS)
                    .onErrorResumeNext {
                        configRepository.getConfigRemote()
                                .map { configResponse ->
                                    configResponse.configuration
                                }
                    }
                    .doOnSuccess {
                        configRepository.putConfigCache(it)
                    }
}