package com.example.proyectocaravanas_naviajuanma.Models

data class Vehiculo(
    val id: Int,
    val modelo: String,
    val matricula: String,
    val precio_dia: Double,
    val descripcion: String?,
    val capacidad_personas: Int,
    val numero_camas: Int,
    val disponible: Boolean,
    val imagen: String?,
    val caracteristicas: Map<String, Boolean>?
)