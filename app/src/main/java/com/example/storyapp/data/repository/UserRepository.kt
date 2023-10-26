package com.example.storyapp.data.repository

import com.example.storyapp.api.response.LoginResponse
import com.example.storyapp.api.response.RegisterResponse
import com.example.storyapp.api.retrofit.ApiService
import com.example.storyapp.data.pref.UserModel
import com.example.storyapp.data.pref.UserPreference
import kotlinx.coroutines.flow.Flow

class UserRepository(private val apiService: ApiService,  private val userPreference: UserPreference) {
    suspend fun login(email: String, password: String): LoginResponse{
        return apiService.login(email, password)
    }

    suspend fun register(name: String, email: String, password: String): RegisterResponse{
        return apiService.register(name, email, password)
    }

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    companion object{
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(apiService: ApiService, userPreference: UserPreference): UserRepository =
            instance ?: synchronized(this){
                instance ?: UserRepository(apiService, userPreference)
            }.also { instance = it }
    }
}