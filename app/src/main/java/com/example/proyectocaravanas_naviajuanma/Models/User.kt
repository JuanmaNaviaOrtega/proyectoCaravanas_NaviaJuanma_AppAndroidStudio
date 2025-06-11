package com.example.proyectocaravanas_naviajuanma.Models

data class User(
    val id: Int,
    val name: String,
    val email: String,
    val telefono: String?,
    val direccion: String?,
    val is_admin: Boolean
)