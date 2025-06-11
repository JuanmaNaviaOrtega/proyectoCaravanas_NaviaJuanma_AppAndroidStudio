package com.example.proyectocaravanas_naviajuanma.API

import com.example.proyectocaravanas_naviajuanma.API.responses.CreateReservaRequest
import com.example.proyectocaravanas_naviajuanma.Auth.AuthViewModel
import com.example.proyectocaravanas_naviajuanma.Models.Reserva
import com.example.proyectocaravanas_naviajuanma.Models.User
import com.example.proyectocaravanas_naviajuanma.Models.Vehiculo
import retrofit2.Response
import retrofit2.http.*

interface ApiService {




    @GET("api/reservas/{id}")
    suspend fun getReservaById(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<Reserva>

    @POST("api/reservas")
    suspend fun createReserva(
        @Header("Authorization") token: String,
        @Body request: CreateReservaRequest
    ): Response<Reserva>

    @POST("api/register")
    suspend fun register(@Body request: AuthViewModel.RegisterRequest): Response<AuthViewModel.AuthResponse>

    @GET("api/vehiculos/disponibles")
    suspend fun getVehiculosDisponibles(
        @Header("Authorization") token: String,
        @Query("fecha_inicio") fechaInicio: String,
        @Query("fecha_fin") fechaFin: String
    ): Response<List<Vehiculo>>



    @GET("api/user/profile")
    suspend fun getProfile(@Header("Authorization") token: String): Response<User>
    @GET("api/vehiculos")
    suspend fun getAllVehiculos(@Header("Authorization") token: String): Response<List<Vehiculo>>

    @DELETE("api/reservas/{id}")
    suspend fun borrarReserva(
        @Header("Authorization") token: String,
        @Path("id") reservaId: Int
    ): Response<Unit>
    @GET("api/reservas")
    suspend fun getReservas(@Header("Authorization") token: String): Response<List<Reserva>>

    @POST("api/login")
    suspend fun login(@Body request: AuthViewModel.LoginRequest): Response<AuthViewModel.AuthResponse>
    @PUT("api/reservas/{id}")
    suspend fun editarReserva(
        @Header("Authorization") token: String,
        @Path("id") reservaId: Int,
        @Body request: CreateReservaRequest
    ): Response<Reserva>
}