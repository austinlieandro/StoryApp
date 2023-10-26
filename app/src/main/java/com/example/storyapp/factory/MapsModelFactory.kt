package com.example.storyapp.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.storyapp.data.repository.MapsRepository
import com.example.storyapp.di.Injection
import com.example.storyapp.view.maps.MapsViewModel

class MapsModelFactory(private val repo:MapsRepository): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when{
            modelClass.isAssignableFrom(MapsViewModel::class.java) ->{
                MapsViewModel(repo) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: MapsModelFactory? = null

        @JvmStatic
        fun getInstance(context: Context): MapsModelFactory {
            return INSTANCE ?: synchronized(MapsModelFactory::class.java) {
                INSTANCE ?: MapsModelFactory(Injection.provideMapsRepository(context)).also {
                    INSTANCE = it
                }
            }
        }
    }
}