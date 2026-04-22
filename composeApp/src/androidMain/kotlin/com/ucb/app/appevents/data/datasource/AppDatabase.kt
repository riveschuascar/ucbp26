package com.ucb.app.appevents.data.datasource

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ucb.app.appevents.data.entity.AppEventEntity

@Database(entities = [AppEventEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun appEventDao(): AppEventDao
}