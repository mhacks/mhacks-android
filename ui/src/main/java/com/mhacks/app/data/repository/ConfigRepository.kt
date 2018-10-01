package com.mhacks.app.data.repository

import com.mhacks.app.data.models.Configuration
import com.mhacks.app.data.room.dao.ConfigurationDao
import com.mhacks.app.data.service.ConfigService
import io.reactivex.Single
import javax.inject.Inject

class ConfigRepository @Inject constructor(
        private val configService: ConfigService,
        private val configDao: ConfigurationDao) {

    fun getConfigLocal() = configDao.getConfig()

    fun putConfigLocal(config: Configuration) =
            Single.fromCallable {
                configDao.insertConfig(config)
                return@fromCallable config
            }!!

    fun getConfigRemote() =
            configService.getConfigResponse()

}