package com.example.storyapp.data.repository

import com.example.storyapp.api.response.StoryResponse
import com.example.storyapp.api.retrofit.ApiService

class MapsRepository(private val apiService: ApiService) {
    suspend fun getLocation(): StoryResponse{
        return apiService.getStoriesWithLocation()
    }

    companion object{
        @Volatile
        private var instance: MapsRepository? = null
        fun getInstance(apiService: ApiService):MapsRepository = instance ?: synchronized(this){
            instance ?: MapsRepository(apiService)
        }.also { instance = it }
    }
}