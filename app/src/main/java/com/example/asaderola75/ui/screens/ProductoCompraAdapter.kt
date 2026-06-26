package com.example.asaderola75.ui.screens

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.asaderola75.R
import com.example.asaderola75.models.Producto

data class ProductoEnCompra(
    val producto: Producto,
    var cantidad: Int,
    var precioUnitario: Double
)

class ProductoCompraAdapter(
    private val items: MutableList<ProductoEnCompra>,
    private val onRemover: (Int) -> Unit,
    private val onTotalChanged: () -> Unit
) : RecyclerView.Adapter<ProductoCompraAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNombre: TextView = view.findViewById(R.id.tvNombreProductoCompra)
        val tvPrecio: TextView = view.findViewById(R.id.tvPrecioProductoCompra)
        val tvCantidad: TextView = view.findViewById(R.id.tvCantidadCompra)
        val btnRemover: Button = view.findViewById(R.id.btnRemoverCompra)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_producto_compra, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.tvNombre.text = item.producto.nombre
        holder.tvPrecio.text = "$ ${"%.0f".format(item.precioUnitario)} c/u"
        holder.tvCantidad.text = "x${item.cantidad}"
        holder.btnRemover.setOnClickListener { onRemover(position) }
    }

    override fun getItemCount() = items.size

    fun calcularTotal(): Double {
        return items.sumOf { it.precioUnitario * it.cantidad }
    }
}