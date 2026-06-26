package com.example.asaderola75.models

data class Venta(
    val id_venta: Int,
    val id_usuario: Int,
    val fecha: String,
    val total: String,
    val status: Int,
    val usuario: Usuario?
)

data class VentasResponse(
    val success: Boolean,
    val data: List<Venta>
)

data class DetalleVenta(
    val id_producto: Int,
    val cantidad: Int,
    val precio_unitario: String,
    val subtotal: String,
    val producto: Producto?
)

data class VentaDetalle(
    val id_venta: Int,
    val id_usuario: Int,
    val fecha: String,
    val total: String,
    val status: Int,
    val usuario: Usuario?,
    val detalle_ventas: List<DetalleVenta>?
)

data class VentaDetalleResponse(
    val success: Boolean,
    val data: VentaDetalle
)