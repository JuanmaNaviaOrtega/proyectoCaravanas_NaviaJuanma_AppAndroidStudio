package com.example.proyectocaravanas_naviajuanma.Models


data class Reserva(
    val id: Int,
    val user_id: Int,
    val vehiculo_id: Int,
    val fecha_inicio: String,
    val fecha_fin: String,
    val precio_total: Double,
    val deposito: Double,
    val estado: String,
    val transaccion_id: String?,
    val stripe_checkout_url: String?,
    val vehiculo: Vehiculo?
)