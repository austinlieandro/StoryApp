package com.example.storyapp.view.welcome

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.storyapp.data.pref.UserModel
import com.example.storyapp.data.repository.UserRepository

class WelcomeViewModel(private val repository: UserRepository): ViewModel() {
    fun getSession(): LiveData<UserModel>{
        return repository.getSession().asLiveData()
    }
}