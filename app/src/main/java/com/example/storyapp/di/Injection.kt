package com.example.storyapp.di

import android.content.Context
import com.example.storyapp.api.retrofit.ApiConfig
import com.example.storyapp.data.pref.UserPreference
import com.example.storyapp.data.pref.dataStore
import com.example.storyapp.data.repository.MapsRepository
import com.example.storyapp.data.repository.StoryRepository
import com.example.storyapp.data.repository.UserRepository
import com.example.storyapp.database.StoryDatabase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideUserRepository(context: Context): UserRepository{
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getSession().first() }
        val apiService = ApiConfig.getApiService(user.token)
        return UserRepository.getInstance(apiService, pref)
    }

    fun provideStoryRepository(context: Context): StoryRepository{
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getSession().first() }
        val apiService = ApiConfig.getApiService(user.token)
        val database = StoryDatabase.getDatabase(context)
        return StoryRepository.getInstance(apiService, pref, database)
    }

    fun provideMapsRepository(context: Context): MapsRepository{
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getSession().first() }
        val apiService = ApiConfig.getApiService(user.token)
        return MapsRepository.getInstance(apiService)
    }
}