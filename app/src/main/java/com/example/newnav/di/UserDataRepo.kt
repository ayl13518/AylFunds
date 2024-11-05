package com.example.newnav.di

import com.example.newnav.models.UserData
import kotlinx.coroutines.flow.Flow

interface UserDataRepo {
    /**
     * Stream of [UserData]
     */
    val userData: Flow<UserData>
}