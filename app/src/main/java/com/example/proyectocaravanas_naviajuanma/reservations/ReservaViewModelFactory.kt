package com.example.proyectocaravanas_naviajuanma.reservations

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.proyectocaravanas_naviajuanma.API.ApiService

class ReservaViewModelFactory(private val apiService: ApiService) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ReservaViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ReservaViewModel(apiService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}