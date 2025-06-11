package com.example.proyectocaravanas_naviajuanma.vehicles

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectocaravanas_naviajuanma.API.RetrofitClient
import com.example.proyectocaravanas_naviajuanma.Models.Resource
import com.example.proyectocaravanas_naviajuanma.Models.Vehiculo
import kotlinx.coroutines.launch

class VehiculoViewModel : ViewModel() {
    private val _vehiculos = MutableLiveData<Resource<List<Vehiculo>>>()
    val vehiculos: LiveData<Resource<List<Vehiculo>>> = _vehiculos

    fun getVehiculosDisponibles(token: String, fechaInicio: String, fechaFin: String) {
        _vehiculos.value = Resource.Loading()
        viewModelScope.launch {
            try {
                val response = RetrofitClient.instance.getVehiculosDisponibles(
                    "Bearer $token",
                    fechaInicio,
                    fechaFin
                )
                if (response.isSuccessful) {
                    _vehiculos.value = Resource.Success(response.body()!!)
                } else {
                    _vehiculos.value = Resource.Error(response.message())
                }
            } catch (e: Exception) {
                _vehiculos.value = Resource.Error(e.message ?: "Error desconocido")
            }
        }

    }

    private val _allVehiculos = MutableLiveData<Resource<List<Vehiculo>>>()
    val allVehiculos: LiveData<Resource<List<Vehiculo>>> = _allVehiculos

    fun getAllVehiculos(token: String) {
        _allVehiculos.value = Resource.Loading()
        viewModelScope.launch {
            try {
                val response = RetrofitClient.instance.getAllVehiculos(token)
                if (response.isSuccessful) {
                    _allVehiculos.value = Resource.Success(response.body()!!)
                } else {
                    _allVehiculos.value = Resource.Error(response.message())
                }
            } catch (e: Exception) {
                _allVehiculos.value = Resource.Error(e.message ?: "Error desconocido")
            }
        }
    }
}
