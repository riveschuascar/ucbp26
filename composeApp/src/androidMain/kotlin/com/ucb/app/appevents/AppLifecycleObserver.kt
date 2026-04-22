package com.ucb.app.appevents

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import com.ucb.app.appevents.data.datasource.AppEventDao
import com.ucb.app.appevents.data.datasource.AppEventFirebaseDatasource
import com.ucb.app.appevents.data.mapper.toDto
import com.ucb.app.appevents.data.mapper.toModel
import com.ucb.app.appevents.domain.model.AppEvent
import com.ucb.app.appevents.domain.usecase.SaveAppEventUseCase
import com.ucb.app.commonutils.connectivity.ConnectivityChecker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class AppLifecycleObserver : DefaultLifecycleObserver, KoinComponent {

    private val saveAppEventUseCase: SaveAppEventUseCase by inject()
    private val dao: AppEventDao by inject()
    private val firebaseDatasource: AppEventFirebaseDatasource by inject()
    private val connectivityChecker: ConnectivityChecker by inject()

    init {
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        // App came to foreground
        CoroutineScope(Dispatchers.IO).launch {
            saveAppEventUseCase(AppEvent(System.currentTimeMillis(), "open"))
            // Sync unsynced events
            if (connectivityChecker.isConnected()) {
                syncUnsyncedEvents()
            }
        }
    }

    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)
        // App went to background
        CoroutineScope(Dispatchers.IO).launch {
            saveAppEventUseCase(AppEvent(System.currentTimeMillis(), "close"))
        }
    }

    private suspend fun syncUnsyncedEvents() {
        val unsyncedEvents = dao.getUnsyncedEvents()
        for (event in unsyncedEvents) {
            try {
                firebaseDatasource.saveEvent("app_events", event.toModel().toDto())
                dao.updateEvent(event.copy(isSynced = true))
            } catch (e: Exception) {
                // Log error
            }
        }
    }
}