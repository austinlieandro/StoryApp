package com.example.storyapp.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.storyapp.api.response.DetailStoryResponse
import com.example.storyapp.api.response.ListStoryItem
import com.example.storyapp.api.response.NewStoryResponse
import com.example.storyapp.api.response.StoryResponse
import com.example.storyapp.api.retrofit.ApiService
import com.example.storyapp.data.paging.StoryPagingSource
import com.example.storyapp.data.paging.StoryRemoteMediator
import com.example.storyapp.data.pref.UserPreference
import com.example.storyapp.database.StoryDatabase
import com.google.gson.Gson
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File

class StoryRepository(private val apiService: ApiService, private val userPreference: UserPreference, private val storyDatabase: StoryDatabase,) {
    suspend fun getStory(): StoryResponse {
        return apiService.getStories()
    }

    suspend fun getDetailStory(id: String): DetailStoryResponse {
        return apiService.getDetailStory(id)
    }

    fun uploadStory(file: File, description: String){
        val requestBody = description.toRequestBody("text/plain".toMediaType())
        val requestImageFile = file.asRequestBody("image/jpeg".toMediaType())
        val multipart = MultipartBody.Part.createFormData(
            "photo",
            file.name,
            requestImageFile
        )
        Log.d("TAG", "uploadStory: $description")

        GlobalScope.launch {
            try {
                val response = apiService.uploadImage(multipart, requestBody).execute()
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, NewStoryResponse::class.java)
                Log.d("UploadError", "Error uploading story: ${errorResponse?.message}")
            }
        }
    }

    fun uploadStoryWithLocation(file: File, description: String, lat: Double, lon: Double){
        val requestBody = description.toRequestBody("text/plain".toMediaType())
        val requestImageFile = file.asRequestBody("image/jpeg".toMediaType())

        val multipart = MultipartBody.Part.createFormData(
            "photo",
            file.name,
            requestImageFile
        )

        Log.d("TAG", "uploadStory: $description")

        GlobalScope.launch {
            try {
                val response = apiService.uploadWithLocation(multipart, requestBody, lat, lon).execute()
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, NewStoryResponse::class.java)
                Log.d("UploadError", "Error uploading story: ${errorResponse?.message}")
            }
        }
    }

    fun getStoryPager(): LiveData<PagingData<ListStoryItem>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoryRemoteMediator(storyDatabase, apiService),
            pagingSourceFactory = {
                StoryPagingSource(apiService)
                storyDatabase.storyDao().getAllStory()
            }
        ).liveData
    }

    companion object{
        @Volatile
        private var instance: StoryRepository? = null
        fun getInstance(apiService: ApiService, userPreference: UserPreference, storyDatabase: StoryDatabase): StoryRepository =
            instance ?: synchronized(this){
                instance ?: StoryRepository(apiService, userPreference , storyDatabase)
            }.also { instance = it }
    }
}