package com.ucb.app.di

import androidx.room.Room
import com.ucb.app.appevents.data.datasource.AppDatabase
import com.ucb.app.appevents.data.datasource.AppEventDao
import com.ucb.app.appevents.data.datasource.AppEventFirebaseDatasource
import com.ucb.app.appevents.data.repository.AppEventRepositoryImp
import com.ucb.app.appevents.domain.repository.AppEventRepository
import com.ucb.app.commonutils.connectivity.ConnectivityChecker
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val appEventDataModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "app_events.db"
        ).build()
    }
    single<AppEventDao> { get<AppDatabase>().appEventDao() }
    singleOf(::AppEventFirebaseDatasource)
    singleOf(::ConnectivityChecker)
    singleOf(::AppEventRepositoryImp).bind<AppEventRepository>()
}