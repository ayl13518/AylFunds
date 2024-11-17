package com.aylmer.aylfunds.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.aylmer.aylfunds.workers.WorkManagerRepository
import com.aylmer.aylfunds.workers.WorkRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RoomModule {
    @Singleton
    @Provides
    fun provideDataBase(@ApplicationContext context: Context): BudgetDatabase {
        return Room.databaseBuilder(
            context,
            BudgetDatabase::class.java,
            "budgetdatabase.db"
        )
            .setJournalMode(RoomDatabase.JournalMode.TRUNCATE)
            .fallbackToDestructiveMigration()
            .addMigrations(Migration8to9)
            .build()
    }

    @Provides
    fun provideExpDao(database: BudgetDatabase) = database.expDao()

    @Provides
    fun provideAccDao(database: BudgetDatabase) = database.accDao()

    @Provides
    fun provideBudDao(database: BudgetDatabase) = database.budDao()

    @Provides
    fun providePrefDao(database: BudgetDatabase) = database.prefDao()

    @Provides
    fun provideTransferDao(database: BudgetDatabase) = database.transferDao()

    @Provides
    fun provideScheduleDao(database: BudgetDatabase) = database.scheduleDao()
}

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Singleton
    @Binds
    abstract fun bindExpRepository(repository: DefaultMainRepository): MainRepository
}

@Module
@InstallIn(SingletonComponent::class)
abstract class WorkRepositoryModule {
    @Singleton
    @Binds
    abstract fun bindWorkRepository(repository: WorkManagerRepository): WorkRepository
}

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideContext(
        @ApplicationContext context: Context): Context {
        return context
    }
}



