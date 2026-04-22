package com.ucb.app.commonutils.data.repository

import com.ucb.app.commonutils.domain.repository.ConfigRepository
import com.ucb.app.commonutils.data.datasource.remote.RemoteConfigManager

class FirebaseConfigRepositoryImp(
    private val remoteConfig: RemoteConfigManager
) : ConfigRepository {
    override suspend fun sync() {
        remoteConfig.fetchAndActivate()
    }

    override fun getGreetingText(): String {
        return remoteConfig.getString("greeting_text")
    }
}