package com.courage.vibestickers.repository

import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getFavoriteTypeIdsFlow(userId: String): Flow<Set<String>>
    suspend fun addFavoriteTypeId(userId: String, typeId: String)
    suspend fun removeFavoriteTypeId(userId: String, typeId: String)
}