package com.monstermind.di

import android.content.Context
import com.monstermind.data.dao.MemoryDao
import com.monstermind.data.database.MemoryDatabase
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
    fun provideMemoryDatabase(
        @ApplicationContext context: Context,
    ): MemoryDatabase {
        return MemoryDatabase.getInstance(context)
    }

    @Provides
    @Singleton
    fun provideMemoryDao(database: MemoryDatabase): MemoryDao {
        return database.memoryDao()
    }
}
