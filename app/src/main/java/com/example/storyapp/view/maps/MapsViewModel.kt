package com.example.storyapp.view.maps

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyapp.api.response.StoryResponse
import com.example.storyapp.data.repository.MapsRepository
import kotlinx.coroutines.launch

class MapsViewModel(private val repository: MapsRepository): ViewModel() {
    private val _maps = MutableLiveData<StoryResponse>()
    val maps: LiveData<StoryResponse> get() = _maps

    fun getLocation(){
        viewModelScope.launch {
            try {
                val response = repository.getLocation()
                val location = response.listStory.firstOrNull()
                _maps.value = response
                Log.d("Maps", "${location?.lat} ${location?.lon}")
            }catch (e: Exception){
                Log.d("Maps", "$e")
            }
        }
    }
}