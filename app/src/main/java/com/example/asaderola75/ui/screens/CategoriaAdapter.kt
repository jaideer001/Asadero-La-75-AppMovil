package com.example.asaderola75.ui.screens

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.asaderola75.R
import com.example.asaderola75.models.Categoria

class CategoriaAdapter(
    private val categorias: List<Categoria>,
    private val onEditar: (Categoria) -> Unit,
    private val onEliminar: (Categoria) -> Unit,
    private val onCambiarEstado: (Categoria) -> Unit
) : RecyclerView.Adapter<CategoriaAdapter.CategoriaViewHolder>() {

    class CategoriaViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNombre: TextView = view.findViewById(R.id.tvNombre)
        val tvDescripcion: TextView = view.findViewById(R.id.tvDescripcion)
        val tvEstado: TextView = view.findViewById(R.id.tvEstado)
        val btnEditar: Button = view.findViewById(R.id.btnEditar)
        val btnEliminar: Button = view.findViewById(R.id.btnEliminar)
        val btnEstado: Button = view.findViewById(R.id.btnEstado)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_categoria, parent, false)
        return CategoriaViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoriaViewHolder, position: Int) {
        val categoria = categorias[position]
        holder.tvNombre.text = categoria.nombre
        holder.tvDescripcion.text = categoria.descripcion ?: "Sin descripción"

        if (categoria.status == 1) {
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

        holder.btnEditar.setOnClickListener { onEditar(categoria) }
        holder.btnEliminar.setOnClickListener { onEliminar(categoria) }
        holder.btnEstado.setOnClickListener { onCambiarEstado(categoria) }
    }

    override fun getItemCount() = categorias.size
}