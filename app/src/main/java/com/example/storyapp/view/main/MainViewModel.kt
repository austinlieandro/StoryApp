package com.example.storyapp.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.storyapp.api.response.ListStoryItem
import com.example.storyapp.data.repository.StoryRepository

class MainViewModel(private val repository: StoryRepository): ViewModel() {

    val story: LiveData<PagingData<ListStoryItem>> = repository.getStoryPager().cachedIn(viewModelScope)
}