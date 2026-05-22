package com.example.asaderola75.models

data class Usuario(
    val id_usuario: Int,
    val nombre: String,
    val usuario: String,
    val correo: String,
    val id_rol: Int,
    val estado: Int
)

data class UsuariosResponse(
    val success: Boolean,
    val data: List<Usuario>
)