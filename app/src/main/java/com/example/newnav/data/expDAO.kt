package com.example.newnav.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface expDAO {

    @Insert
    suspend fun insertExpTran(expTrans: expTrans)

    @Query("SELECT * FROM expTrans")
    fun getAllExpTrans(): Flow<List<expTrans>>

    @Query("DELETE FROM expTrans WHERE id = :id")
    suspend fun deleteExpTrans(id: Int)

    @Query(  "SELECT * FROM expTrans WHERE cast(STRFTIME('%m', dateTrans) as integer) = :id")
    fun getExpByMonth(id: Int): Flow<List<expTrans>>



}