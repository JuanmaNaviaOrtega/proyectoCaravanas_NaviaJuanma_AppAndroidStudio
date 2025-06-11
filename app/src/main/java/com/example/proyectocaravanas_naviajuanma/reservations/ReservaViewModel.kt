package com.example.proyectocaravanas_naviajuanma.reservations

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectocaravanas_naviajuanma.API.ApiService
import com.example.proyectocaravanas_naviajuanma.API.responses.CreateReservaRequest
import com.example.proyectocaravanas_naviajuanma.Models.Reserva
import com.example.proyectocaravanas_naviajuanma.Models.Resource
import com.example.proyectocaravanas_naviajuanma.Models.Vehiculo
import kotlinx.coroutines.launch

class ReservaViewModel(private val apiService: ApiService) : ViewModel() {
    private val _reservas = MutableLiveData<Resource<List<Reserva>>>()
    val reservas: LiveData<Resource<List<Reserva>>> = _reservas

    private val _reserva = MutableLiveData<Resource<Reserva>>()
    val reserva: LiveData<Resource<Reserva>> = _reserva

    private val _createReservaResult = MutableLiveData<Resource<Reserva>>()
    val createReservaResult: LiveData<Resource<Reserva>> = _createReservaResult

    fun getReservas(token: String) {
        viewModelScope.launch {
            _reservas.value = Resource.Loading()
            try {
                val response = apiService.getReservas(token)
                if (response.isSuccessful) {
                    _reservas.value = Resource.Success(response.body() ?: emptyList())
                } else {
                    _reservas.value = Resource.Error("Error: ${response.message()}")
                }
            } catch (e: Exception) {
                _reservas.value = Resource.Error(e.message ?: "Error desconocido")
            }
        }
    }

    fun getReserva(token: String, id: Int) {
        viewModelScope.launch {
            _reserva.value = Resource.Loading()
            try {
                val response = apiService.getReservaById(token, id)
                if (response.isSuccessful) {
                    _reserva.value = Resource.Success(response.body()!!)
                } else {
                    _reserva.value = Resource.Error("Error: ${response.message()}")
                }
            } catch (e: Exception) {
                _reserva.value = Resource.Error(e.message ?: "Error desconocido")
            }
        }
    }

    fun createReserva(token: String, vehiculoId: Int, fechaInicio: String, fechaFin: String) {
        viewModelScope.launch {
            _createReservaResult.value = Resource.Loading()
            try {
                val response = apiService.createReserva(
                    token,
                    CreateReservaRequest(vehiculoId, fechaInicio, fechaFin)
                )
                if (response.isSuccessful) {
                    _createReservaResult.value = Resource.Success(response.body()!!)
                } else {
                    _createReservaResult.value = Resource.Error("Error: ${response.message()}")
                }
            } catch (e: Exception) {
                _createReservaResult.value = Resource.Error(e.message ?: "Error desconocido")
            }
        }
    }

    fun borrarReserva(
        token: String,
        reservaId: Int,
        onSuccess: () -> Unit,
        onError: (String) -> Unit = {}
    ) {
        viewModelScope.launch {
            try {
                val response = apiService.borrarReserva(token, reservaId)
                if (response.isSuccessful) {
                    onSuccess()
                } else {
                    onError("Error: ${response.code()} - ${response.message()}")
                }
            } catch (e: Exception) {
                onError(e.message ?: "Error desconocido")
            }

        }
    }
    fun editarReserva(
        token: String,
        reservaId: Int,
        vehiculoId: Int,
        fechaInicio: String,
        fechaFin: String,
        onSuccess: (Reserva) -> Unit,
        onError: (String) -> Unit = {}
    ) {
        viewModelScope.launch {
            try {
                val response = apiService.editarReserva(
                    token,
                    reservaId,
                    CreateReservaRequest(vehiculoId, fechaInicio, fechaFin)
                )
                if (response.isSuccessful && response.body() != null) {
                    onSuccess(response.body()!!)
                } else {

                    if (response.code() == 422) {
                        val errorBody = response.errorBody()?.string()
                        val message = parseValidationError(errorBody)
                        onError(message)
                    } else {
                        onError("Error: ${response.code()} - ${response.message()}")
                    }
                }
            } catch (e: Exception) {
                onError(e.message ?: "Error desconocido")
            }
        }
    }

    private fun parseValidationError(errorBody: String?): String {
        if (errorBody.isNullOrEmpty()) return "Error de validación"

        val regex = """"message"\s*:\s*"([^"]+)"""".toRegex()
        val match = regex.find(errorBody)
        return match?.groups?.get(1)?.value ?: "Error de validación"
    }
    private val _allVehiculos = MutableLiveData<Resource<List<Vehiculo>>>()
    val allVehiculos: LiveData<Resource<List<Vehiculo>>> = _allVehiculos

    fun getAllVehiculos(token: String) {
        _allVehiculos.value = Resource.Loading()
        viewModelScope.launch {
            try {
                val response = apiService.getAllVehiculos(token)
                if (response.isSuccessful) {
                    _allVehiculos.value = Resource.Success(response.body() ?: emptyList())
                } else {
                    _allVehiculos.value = Resource.Error(response.message())
                }
            } catch (e: Exception) {
                _allVehiculos.value = Resource.Error(e.message ?: "Error desconocido")
            }
        }
    }



}