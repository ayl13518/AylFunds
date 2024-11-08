package com.aylmer.aylfunds.data

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface PrefDAO {

    @Query("SELECT * FROM preferences")
    fun getAll(): Flow<List<Preferences>>

    @Upsert
    suspend fun upsertPref(pref: Preferences)

    @Upsert
    suspend fun upsertAll(pref: List<Preferences>)

    @Query("SELECT name FROM preferences WHERE `key` = :key ")
    fun getPrefName(key: String): Flow<String>


}