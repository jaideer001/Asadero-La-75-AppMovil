package com.example.asaderola75.models

data class Producto(
    val id_producto: Int,
    val nombre: String,
    val descripcion: String?,
    val stock_actual: String,
    val unidad_medida: String,
    val precio_compra: String?,
    val precio_venta: String?,
    val tipo: String,
    val status: Int?,
    val id_categoria: Int,
    val categoria: Categoria?
)

data class ProductosResponse(
    val success: Boolean,
    val data: List<Producto>
)