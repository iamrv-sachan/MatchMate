package com.example.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.example.data.api.RandomUserApi
import com.example.data.mapper.UserMapper.toDomain
import com.example.data.mapper.UserMapper.toEntity
import com.example.data.room.dao.UserDao
import com.example.domain.model.User
import com.example.domain.repoImpl.UserRepository
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class UserRepositoryImpl @Inject constructor(
    private val api: RandomUserApi,
    private val userDao: UserDao
) : UserRepository {

    override fun getAllUsers(): Flow<List<User>> {
        return userDao.getHomeUsers()
            .map { entities -> entities.map { it.toDomain() } }
    }

    override fun getProfileMatchesUsers(): Flow<PagingData<User>> {
        return Pager(
            config = PagingConfig(pageSize = 10),
            pagingSourceFactory = { userDao.getProfileMatches() }
        ).flow.map { pagingData ->
            pagingData.map { it.toDomain() }
        }
    }

    override suspend fun fetchUsersFromApi() {
        withContext(Dispatchers.IO) {
            val userCount = userDao.getUserCount()
            if (userCount <= 3) {
                try {
                    val apiUsers = fetchUsers()
                    userDao.insertUsers(apiUsers.map { it.toEntity(it.id.name + "-" +  it.id.value) })
                } catch (e: Exception) {

                }
            }
        }
    }

    override suspend fun acceptMatch(user: User) {
        val updatedEntity = user.toEntity(user.id.name + "-" +  user.id.value).copy(isMatched = true)
        userDao.updateUser(updatedEntity)
    }

    override suspend fun declineMatch(user: User) {
        val updatedEntity = user.toEntity(user.id.name + "-" + user.id.value).copy(isDeclined = true)
        userDao.updateUser(updatedEntity)
    }

    override suspend fun getUserCount() = userDao.getUserCount()

    private suspend fun fetchUsers(): List<User> {
        return api.getUsers().results
    }
}