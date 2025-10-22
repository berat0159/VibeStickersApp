package com.courage.vibestickers.repository

import kotlinx.coroutines.flow.Flow

class FakeRepositoryUser(): UserRepository {
    override fun getFavoriteTypeIdsFlow(userId: String): Flow<Set<String>> {
        TODO("Not yet implemented")
    }

    override suspend fun addFavoriteTypeId(userId: String, typeId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun removeFavoriteTypeId(userId: String, typeId: String) {
        TODO("Not yet implemented")
    }
}