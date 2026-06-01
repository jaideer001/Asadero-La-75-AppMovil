package com.example.asaderola75.ui.screens

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.asaderola75.R
import com.example.asaderola75.models.Producto

class ProductoAdapter(
    private val productos: List<Producto>,
    private val onEditar: (Producto) -> Unit,
    private val onEliminar: (Producto) -> Unit,
    private val onCambiarEstado: (Producto) -> Unit
) : RecyclerView.Adapter<ProductoAdapter.ProductoViewHolder>() {

    class ProductoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNombre: TextView = view.findViewById(R.id.tvNombre)
        val tvCategoria: TextView = view.findViewById(R.id.tvCategoria)
        val tvTipo: TextView = view.findViewById(R.id.tvTipo)
        val tvEstado: TextView = view.findViewById(R.id.tvEstado)
        val tvStock: TextView = view.findViewById(R.id.tvStock)
        val tvPrecioVenta: TextView = view.findViewById(R.id.tvPrecioVenta)
        val btnEditar: Button = view.findViewById(R.id.btnEditar)
        val btnEliminar: Button = view.findViewById(R.id.btnEliminar)
        val btnEstado: Button = view.findViewById(R.id.btnEstado)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_producto, parent, false)
        return ProductoViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductoViewHolder, position: Int) {
        val producto = productos[position]
        holder.tvNombre.text = producto.nombre
        holder.tvCategoria.text = producto.categoria?.nombre ?: "Sin categoría"
        holder.tvTipo.text = "Tipo: ${producto.tipo}"
        holder.tvStock.text = "${producto.stock_actual} ${producto.unidad_medida}"
        holder.tvPrecioVenta.text = "$${producto.precio_venta ?: "N/A"}"

        if (producto.status == 1) {
            holder.tvEstado.text = "Activo"
            holder.tvEstado.setBackgroundResource(R.drawable.bg_badge_active)
            holder.tvEstado.setTextColor(holder.itemView.context.getColor(R.color.active_text))
            holder.btnEstado.text = "Inactivar"
        } else {
            holder.tvEstado.text = "Inactivo"
            holder.tvEstado.setBackgroundResource(R.drawable.bg_badge_inactive)
            holder.tvEstado.setTextColor(holder.itemView.context.getColor(R.color.inactive_text))
            holder.btnEstado.text = "Activar"
        }

        holder.btnEditar.setOnClickListener { onEditar(producto) }
        holder.btnEliminar.setOnClickListener { onEliminar(producto) }
        holder.btnEstado.setOnClickListener { onCambiarEstado(producto) }
    }

    override fun getItemCount() = productos.size
}