package com.example.domain.repoImpl

import androidx.paging.PagingData
import com.example.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getAllUsers(): Flow<List<User>>
    fun getProfileMatchesUsers(): Flow<PagingData<User>>
    suspend fun fetchUsersFromApi()
    suspend fun acceptMatch(user: User)
    suspend fun declineMatch(user: User)
    suspend fun getUserCount(): Int
}