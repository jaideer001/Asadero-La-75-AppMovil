package com.example.asaderola75.models

data class Compra(
    val id_compra: Int,
    val id_proveedor: Int,
    val id_usuario: Int,
    val fecha: String,
    val total: String,
    val status: Int,
    val proveedor: Proveedor?,
    val usuario: Usuario?
)

data class ComprasResponse(
    val success: Boolean,
    val data: List<Compra>
)

data class DetalleCompra(
    val id_producto: Int,
    val cantidad: Int,
    val precio_unitario: String,
    val subtotal: String,
    val producto: Producto?
)

data class CompraDetalle(
    val id_compra: Int,
    val id_proveedor: Int,
    val id_usuario: Int,
    val fecha: String,
    val total: String,
    val status: Int,
    val proveedor: Proveedor?,
    val usuario: Usuario?,
    val detalles: List<DetalleCompra>?
)

data class CompraDetalleResponse(
    val success: Boolean,
    val data: CompraDetalle
)