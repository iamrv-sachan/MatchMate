package com.example.data.di

import android.content.Context
import androidx.room.Room
import com.example.data.room.MatchMateDatabase
import com.example.data.api.RandomUserApi
import com.example.data.room.MIGRATION_1_2
import com.example.data.room.MIGRATION_2_3
import com.example.data.room.dao.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun provideBaseUrl() = "https://randomuser.me/"

    @Provides
    @Singleton
    fun provideRetrofit(baseUrl: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideRandomUserApi(retrofit: Retrofit): RandomUserApi {
        return retrofit.create(RandomUserApi::class.java)
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): MatchMateDatabase {
        return Room.databaseBuilder(
            context,
            MatchMateDatabase::class.java,
            "matchmate_db"
        )
            .addMigrations(MIGRATION_1_2)
            .addMigrations(MIGRATION_2_3)
            .build()
    }

    @Provides
    fun provideUserDao(matchMateDatabase: MatchMateDatabase): UserDao {
        return matchMateDatabase.userDao()
    }
}