package org.mhacks.app.welcome

import org.mhacks.app.welcome.data.model.Configuration
import org.mhacks.app.welcome.data.db.ConfigurationDao
import org.mhacks.app.welcome.data.service.ConfigService
import io.reactivex.Single
import javax.inject.Inject

class ConfigRepository @Inject constructor(
        private val configService: ConfigService,
        private val configDao: ConfigurationDao) {

    fun getConfigCache() = configDao.getConfig()

    fun putConfigCache(config: Configuration) =
            Single.fromCallable {
                configDao.insertConfig(config)
                return@fromCallable config
            }

    fun getConfigRemote() =
            configService.getConfigResponse()

}