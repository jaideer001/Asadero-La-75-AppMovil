package com.example.asaderola75.ui.screens

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.asaderola75.R
import com.example.asaderola75.models.Producto

data class ProductoEnVenta(
    val producto: Producto,
    var cantidad: Int
)

class ProductoVentaAdapter(
    private val items: MutableList<ProductoEnVenta>,
    private val onRemover: (Int) -> Unit,
    private val onTotalChanged: () -> Unit
) : RecyclerView.Adapter<ProductoVentaAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNombre: TextView = view.findViewById(R.id.tvNombreProductoVenta)
        val tvPrecio: TextView = view.findViewById(R.id.tvPrecioProductoVenta)
        val tvCantidad: TextView = view.findViewById(R.id.tvCantidadVenta)
        val btnRemover: Button = view.findViewById(R.id.btnRemover)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_producto_venta, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.tvNombre.text = item.producto.nombre
        holder.tvPrecio.text = "$ ${item.producto.precio_venta} c/u"
        holder.tvCantidad.text = "x${item.cantidad}"
        holder.btnRemover.setOnClickListener { onRemover(position) }
    }

    override fun getItemCount() = items.size

    fun calcularTotal(): Double {
        return items.sumOf {
            (it.producto.precio_venta?.toDoubleOrNull() ?: 0.0) * it.cantidad
        }
    }
}