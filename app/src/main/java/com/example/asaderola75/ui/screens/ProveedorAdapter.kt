package com.example.asaderola75.ui.screens

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.asaderola75.R
import com.example.asaderola75.models.Proveedor

class ProveedorAdapter(
    private val proveedores: List<Proveedor>,
    private val onEditar: (Proveedor) -> Unit,
    private val onEliminar: (Proveedor) -> Unit,
    private val onCambiarEstado: (Proveedor) -> Unit
) : RecyclerView.Adapter<ProveedorAdapter.ProveedorViewHolder>() {

    class ProveedorViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNombre: TextView = view.findViewById(R.id.tvNombre)
        val tvTelefono: TextView = view.findViewById(R.id.tvTelefono)
        val tvCorreo: TextView = view.findViewById(R.id.tvCorreo)
        val tvDireccion: TextView = view.findViewById(R.id.tvDireccion)
        val tvEstado: TextView = view.findViewById(R.id.tvEstado)
        val btnEditar: Button = view.findViewById(R.id.btnEditar)
        val btnEliminar: Button = view.findViewById(R.id.btnEliminar)
        val btnEstado: Button = view.findViewById(R.id.btnEstado)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProveedorViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_proveedor, parent, false)
        return ProveedorViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProveedorViewHolder, position: Int) {
        val proveedor = proveedores[position]
        holder.tvNombre.text = proveedor.nombre
        holder.tvTelefono.text = proveedor.telefono ?: "Sin teléfono"
        holder.tvCorreo.text = proveedor.correo ?: "Sin correo"
        holder.tvDireccion.text = proveedor.direccion ?: "Sin dirección"

        if (proveedor.status == 1) {
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

        holder.btnEditar.setOnClickListener { onEditar(proveedor) }
        holder.btnEliminar.setOnClickListener { onEliminar(proveedor) }
        holder.btnEstado.setOnClickListener { onCambiarEstado(proveedor) }
    }

    override fun getItemCount() = proveedores.size
}