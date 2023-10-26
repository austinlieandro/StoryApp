package com.example.storyapp.view.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyapp.api.response.LoginResponse
import com.example.storyapp.data.pref.UserModel
import com.example.storyapp.data.repository.UserRepository
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class LoginViewModel(private val repository: UserRepository): ViewModel() {
    val error = MutableLiveData<String?>()
    val status: MutableLiveData<Boolean> = MutableLiveData()

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    fun login(email:String, password:String){
        viewModelScope.launch {
            try {
                _loading.value = true
                val loginResponse = repository.login(email, password)
                status.postValue(true)
                val token = loginResponse.loginResult?.token
                repository.saveSession(UserModel(email, token ?: "", true))
                Log.d("LOGIN", "$loginResponse")
            }catch (e: HttpException){
                _loading.value = false
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, LoginResponse::class.java)
                error.postValue(errorBody.message)
                status.postValue(false)
                Log.d("LOGIN", "$e")
            }catch (e: Exception) {
                _loading.value = false
                error.postValue("Terjadi kesalahan saat login")
                status.postValue(false)
                Log.d("LOGIN", "$e")
            }
        }
    }
}