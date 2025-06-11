package com.example.proyectocaravanas_naviajuanma.Auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectocaravanas_naviajuanma.API.RetrofitClient
import com.example.proyectocaravanas_naviajuanma.Models.Resource
import com.example.proyectocaravanas_naviajuanma.Models.User


import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    data class LoginRequest(val email: String, val password: String)
    data class RegisterRequest(val name: String, val email: String, val password: String)
    data class AuthResponse(val token: String, val user: User)
    private val _authResult = MutableLiveData<Resource<AuthResponse>>()
    val authResult: LiveData<Resource<AuthResponse>> = _authResult

    fun login(email: String, password: String) {
        _authResult.value = Resource.Loading()
        viewModelScope.launch {
            try {
                val response = RetrofitClient.instance.login(LoginRequest(email, password))
                if (response.isSuccessful) {
                    _authResult.value = Resource.Success(response.body()!!)
                } else {
                    _authResult.value = Resource.Error(response.message())
                }
            } catch (e: Exception) {
                _authResult.value = Resource.Error(e.message ?: "Error desconocido")
            }
        }
    }

    fun register(name: String, email: String, password: String) {
        _authResult.value = Resource.Loading()
        viewModelScope.launch {
            try {
                val response = RetrofitClient.instance.register(RegisterRequest(name, email, password))
                if (response.isSuccessful) {
                    _authResult.value = Resource.Success(response.body()!!)
                } else {
                    _authResult.value = Resource.Error(response.message())
                }
            } catch (e: Exception) {
                _authResult.value = Resource.Error(e.message ?: "Error desconocido")
            }
        }
    }

    private val _profile = MutableLiveData<Resource<User>>()
    val profile: LiveData<Resource<User>> = _profile

    fun getProfile(token: String) {
        _profile.value = Resource.Loading()
        viewModelScope.launch {
            try {
                val response = RetrofitClient.instance.getProfile(token)
                if (response.isSuccessful) {
                    _profile.value = Resource.Success(response.body()!!)
                } else {
                    _profile.value = Resource.Error(response.message())
                }
            } catch (e: Exception) {
                _profile.value = Resource.Error(e.message ?: "Error desconocido")
            }
        }
    }


}

