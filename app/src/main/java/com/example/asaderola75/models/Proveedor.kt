package com.example.asaderola75.models

data class Proveedor(
    val id_proveedor: Int,
    val nombre: String,
    val telefono: String?,
    val direccion: String?,
    val correo: String?,
    val status: Int?
)

data class ProveedoresResponse(
    val success: Boolean,
    val data: List<Proveedor>
)