package com.courage.vibestickers.data.domain

import com.courage.vibestickers.view.bottomnavigator.Route
import kotlinx.coroutines.flow.Flow

interface LocalUserManager {

    suspend fun saveAppEntry()
    val readAppEntry: Flow<Boolean>
}