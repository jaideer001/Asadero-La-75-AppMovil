package com.example.asaderola75.ui.screens

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.asaderola75.R
import com.example.asaderola75.models.DetalleCompra

class DetalleCompraAdapter(
    private val detalles: List<DetalleCompra>
) : RecyclerView.Adapter<DetalleCompraAdapter.DetalleViewHolder>() {

    class DetalleViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvProductoNombre: TextView = view.findViewById(R.id.tvProductoNombre)
        val tvCantidad: TextView = view.findViewById(R.id.tvCantidad)
        val tvPrecioUnitario: TextView = view.findViewById(R.id.tvPrecioUnitario)
        val tvSubtotal: TextView = view.findViewById(R.id.tvSubtotal)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetalleViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_detalle_venta, parent, false)
        return DetalleViewHolder(view)
    }

    override fun onBindViewHolder(holder: DetalleViewHolder, position: Int) {
        val detalle = detalles[position]
        holder.tvProductoNombre.text = detalle.producto?.nombre ?: "Producto"
        holder.tvCantidad.text = "Cantidad: ${detalle.cantidad}"
        holder.tvPrecioUnitario.text = "Precio unit: $ ${detalle.precio_unitario}"
        holder.tvSubtotal.text = "$ ${detalle.subtotal}"
    }

    override fun getItemCount() = detalles.size
}