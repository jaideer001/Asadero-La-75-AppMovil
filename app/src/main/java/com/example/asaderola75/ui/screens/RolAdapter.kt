package com.example.asaderola75.ui.screens

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.asaderola75.R
import com.example.asaderola75.models.Rol

class RolAdapter(
    private val roles: List<Rol>,
    private val onEditar: (Rol) -> Unit,
    private val onEliminar: (Rol) -> Unit,
    private val onCambiarEstado: (Rol) -> Unit
) : RecyclerView.Adapter<RolAdapter.RolViewHolder>() {

    class RolViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNombre: TextView = view.findViewById(R.id.tvNombre)
        val tvDescripcion: TextView = view.findViewById(R.id.tvDescripcion)
        val tvEstado: TextView = view.findViewById(R.id.tvEstado)
        val btnEditar: Button = view.findViewById(R.id.btnEditar)
        val btnEliminar: Button = view.findViewById(R.id.btnEliminar)
        val btnEstado: Button = view.findViewById(R.id.btnEstado)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RolViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_rol, parent, false)
        return RolViewHolder(view)
    }

    override fun onBindViewHolder(holder: RolViewHolder, position: Int) {
        val rol = roles[position]
        holder.tvNombre.text = rol.nombre
        holder.tvDescripcion.text = rol.descripcion ?: "Sin descripción"

        if (rol.status == 1) {
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

        holder.btnEditar.setOnClickListener { onEditar(rol) }
        holder.btnEliminar.setOnClickListener { onEliminar(rol) }
        holder.btnEstado.setOnClickListener { onCambiarEstado(rol) }
    }

    override fun getItemCount() = roles.size
}