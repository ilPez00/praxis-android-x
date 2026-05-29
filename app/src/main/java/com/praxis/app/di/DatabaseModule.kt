package com.praxis.app.di

import android.content.Context
import androidx.room.Room
import com.praxis.app.data.local.PendingSyncDao
import com.praxis.app.data.local.PraxisDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): PraxisDatabase {
        return Room.databaseBuilder(
            context,
            PraxisDatabase::class.java,
            "praxis_db",
        ).fallbackToDestructiveMigration(false)
            .build()
    }

    @Provides
    @Singleton
    fun providePendingSyncDao(database: PraxisDatabase): PendingSyncDao {
        return database.pendingSyncDao()
    }
}
