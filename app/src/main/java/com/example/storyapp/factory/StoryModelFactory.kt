package com.example.storyapp.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.storyapp.data.repository.StoryRepository
import com.example.storyapp.di.Injection
import com.example.storyapp.view.addstory.AddStoryViewModel
import com.example.storyapp.view.detailstory.DetailViewModel
import com.example.storyapp.view.main.MainViewModel

class StoryModelFactory(private val repository: StoryRepository):ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when{
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(repository) as T
            }
            modelClass.isAssignableFrom(DetailViewModel::class.java) -> {
                DetailViewModel(repository) as T
            }
            modelClass.isAssignableFrom(AddStoryViewModel::class.java) -> {
                AddStoryViewModel(repository) as T
            }
            else -> throw IllegalArgumentException("Unkown ViewModel class: " + modelClass.name)
        }
    }

    companion object{
        @Volatile
        private var instance: StoryModelFactory? = null

        fun getInstance(context: Context): StoryModelFactory =
            instance ?: synchronized(this){
                instance ?: StoryModelFactory(Injection.provideStoryRepository(context))
            }.also { instance = it }
    }
}