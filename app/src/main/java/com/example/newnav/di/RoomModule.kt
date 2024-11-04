package com.example.newnav.di

import android.content.Context
import androidx.room.Room
import com.example.newnav.data.BudgetDatabase
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
        ).build()
    }

    @Provides
    fun provideExpDao(database: BudgetDatabase) = database.expDao()

    @Provides
    fun provideAccDao(database: BudgetDatabase) = database.accDao()

    @Provides
    fun provideBudDao(database: BudgetDatabase) = database.budDao()

    @Provides
    fun providePrefDao(database: BudgetDatabase) = database.prefDao()
}

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Singleton
    @Binds
    abstract fun bindExpRepository(repository: DefaultMainRepository): MainRepository
}