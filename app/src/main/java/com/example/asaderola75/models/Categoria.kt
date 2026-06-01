package com.example.asaderola75.models

data class Categoria(
    val id_categoria: Int,
    val nombre: String,
    val descripcion: String?,
    val status: Int?
)

data class CategoriasResponse(
    val success: Boolean,
    val data: List<Categoria>
)