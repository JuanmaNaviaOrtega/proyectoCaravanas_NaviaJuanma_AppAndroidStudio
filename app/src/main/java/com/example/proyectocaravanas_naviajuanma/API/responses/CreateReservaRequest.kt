package com.example.proyectocaravanas_naviajuanma.API.responses

data class CreateReservaRequest(
    val vehiculo_id: Int,
    val fecha_inicio: String,
    val fecha_fin: String
)