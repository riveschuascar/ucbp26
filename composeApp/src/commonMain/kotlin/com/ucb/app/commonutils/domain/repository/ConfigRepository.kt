package com.ucb.app.commonutils.domain.repository

interface ConfigRepository {
    suspend fun sync()
    fun getGreetingText(): String
}