package com.example.storyapp.view.detailstory

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyapp.api.response.DetailStoryResponse
import com.example.storyapp.api.response.Story
import com.example.storyapp.data.repository.StoryRepository
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class DetailViewModel(private val repository: StoryRepository): ViewModel() {
    val error = MutableLiveData<String?>()
    val status: MutableLiveData<Boolean> = MutableLiveData()
    val detailStory = MutableLiveData<Story>()

    fun getDetailStory(id: String){
        viewModelScope.launch {
            try {
                val detailResponse = repository.getDetailStory(id)
                status.postValue(true)
                detailStory.postValue(detailResponse.story)
                Log.d("DETAIL", "$detailResponse")
            }catch (e: HttpException){
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, DetailStoryResponse::class.java)
                error.postValue(errorBody.message)
                status.postValue(false)
                Log.d("DETAIL", "$e")
            }catch (e: Exception) {
                error.postValue("Terjadi kesalahan saat memuat data")
                status.postValue(false)
                Log.d("DETAIL", "$e")
            }
        }
    }
}