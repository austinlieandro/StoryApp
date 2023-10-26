package com.example.storyapp.view.register

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyapp.api.response.RegisterResponse
import com.example.storyapp.data.repository.UserRepository
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class RegisterViewModel(private val repository: UserRepository): ViewModel() {
    val error = MutableLiveData<String?>()
    val status: MutableLiveData<Boolean> = MutableLiveData()

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    fun register(username: String, email: String, password: String) {
        viewModelScope.launch {
            try {
                _loading.value = true
                val registerResponse = repository.register(username, email, password)
                status.postValue(true)
                Log.d("REGISTER", "$registerResponse")
            } catch (e: HttpException) {
                _loading.value = false
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, RegisterResponse::class.java)
                error.postValue(errorBody.message)
                status.postValue(false)
                Log.d("REGISTER", "$e")
            } catch (e: Exception) {
                _loading.value = true
                error.postValue("Terjadi kesalahan saat pendaftaran")
                status.postValue(false)
                Log.d("REGISTER", "$e")
            }
        }

    }
}