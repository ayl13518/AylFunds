package com.aylmer.aylfunds.di

import com.aylmer.aylfunds.models.UserData
import kotlinx.coroutines.flow.Flow

interface UserDataRepo {
    /**
     * Stream of [UserData]
     */
    val userData: Flow<UserData>
}