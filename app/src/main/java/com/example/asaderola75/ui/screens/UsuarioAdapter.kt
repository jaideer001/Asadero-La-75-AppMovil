package com.example.asaderola75.ui.screens

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.asaderola75.R
import com.example.asaderola75.models.Usuario

class UsuarioAdapter(
    private val usuarios: List<Usuario>,
    private val onEditar: (Usuario) -> Unit,
    private val onEliminar: (Usuario) -> Unit,
    private val onCambiarEstado: (Usuario) -> Unit
) : RecyclerView.Adapter<UsuarioAdapter.UsuarioViewHolder>() {

    class UsuarioViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvAvatar: TextView = view.findViewById(R.id.tvAvatar)
        val tvNombre: TextView = view.findViewById(R.id.tvNombre)
        val tvUsuario: TextView = view.findViewById(R.id.tvUsuario)
        val tvCorreo: TextView = view.findViewById(R.id.tvCorreo)
        val tvEstado: TextView = view.findViewById(R.id.tvEstado)
        val btnEditar: Button = view.findViewById(R.id.btnEditar)
        val btnEliminar: Button = view.findViewById(R.id.btnEliminar)
        val btnEstado: Button = view.findViewById(R.id.btnEstado)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsuarioViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_usuario, parent, false)
        return UsuarioViewHolder(view)
    }

    override fun onBindViewHolder(holder: UsuarioViewHolder, position: Int) {
        val usuario = usuarios[position]
        val iniciales = usuario.nombre.split(" ")
            .take(2).joinToString("") { it.first().uppercase() }

        holder.tvAvatar.text = iniciales
        holder.tvNombre.text = usuario.nombre
        holder.tvUsuario.text = "@${usuario.usuario}"
        holder.tvCorreo.text = usuario.correo

        if (usuario.estado == 1) {
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

        holder.btnEditar.setOnClickListener { onEditar(usuario) }
        holder.btnEliminar.setOnClickListener { onEliminar(usuario) }
        holder.btnEstado.setOnClickListener { onCambiarEstado(usuario) }
    }

    override fun getItemCount() = usuarios.size
}