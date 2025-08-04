package com.example.data.api

import com.example.domain.model.UserResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface RandomUserApi {
    @GET("api/")
    suspend fun getUsers(@Query("results") count: Int = 10): UserResponse
}