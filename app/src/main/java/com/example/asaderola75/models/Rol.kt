package com.example.asaderola75.models

data class Rol(
    val id_rol: Int,
    val nombre: String,
    val descripcion: String?,
    val status: Int
)

data class RolesResponse(
    val success: Boolean,
    val data: List<Rol>
)