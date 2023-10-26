package com.example.storyapp.view.addstory

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.storyapp.api.response.NewStoryResponse
import com.example.storyapp.api.response.StoryResponse
import com.example.storyapp.data.repository.StoryRepository
import com.google.gson.Gson
import retrofit2.HttpException
import java.io.File

class AddStoryViewModel(private val repository: StoryRepository): ViewModel() {
    val error = MutableLiveData<String?>()
    val status: MutableLiveData<Boolean> = MutableLiveData()

    fun uploadImage(file: File, description: String){
        try{
            val response = repository.uploadStory(file, description)
            status.postValue(true)
            Log.d("AddStory", "$response")
        }catch (e: HttpException){
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, NewStoryResponse::class.java)
            error.postValue(errorBody.message)
            status.postValue(false)
            Log.d("AddStory", "$e")
        }catch (e: Exception){
            error.postValue("Terjadi kesalahan saat membuat data")
            status.postValue(false)
            Log.d("AddStory", "$e")
        }
    }

    fun uploadWithLocation(file: File, description: String, lat: Double, lon: Double){
        try {
            val response = repository.uploadStoryWithLocation(file, description, lat, lon)
            status.postValue(true)
            Log.d("AddStory", "uploadWithLocation: $response")
        }catch (e: HttpException){
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, StoryResponse::class.java)
            error.postValue(errorBody.message)
            status.postValue(false)
            Log.d("AddStory", "$e")
        }catch (e: Exception){
            error.postValue("Terjadi kesalahan saat membuat data")
            status.postValue(false)
            Log.d("AddStory", "$e")
        }
    }
}