package com.example.proyectocaravanas_naviajuanma.gestiosReservas

data class UpdateReservaRequest(
    val vehiculo_id: Int,
    val fecha_inicio: String,
    val fecha_fin: String,
    val estado: String? = null
)