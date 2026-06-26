package com.example.asaderola75.ui.screens

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.asaderola75.R
import com.example.asaderola75.models.Venta

class VentaAdapter(
    private val ventas: List<Venta>,
    private val onVerDetalle: (Venta) -> Unit,
    private val onAnular: (Venta) -> Unit
) : RecyclerView.Adapter<VentaAdapter.VentaViewHolder>() {

    class VentaViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvIdVenta: TextView = view.findViewById(R.id.tvIdVenta)
        val tvUsuario: TextView = view.findViewById(R.id.tvUsuario)
        val tvFecha: TextView = view.findViewById(R.id.tvFecha)
        val tvTotal: TextView = view.findViewById(R.id.tvTotal)
        val tvEstado: TextView = view.findViewById(R.id.tvEstado)
        val btnVerDetalle: Button = view.findViewById(R.id.btnVerDetalle)
        val btnAnular: Button = view.findViewById(R.id.btnAnular)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VentaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_venta, parent, false)
        return VentaViewHolder(view)
    }

    override fun onBindViewHolder(holder: VentaViewHolder, position: Int) {
        val venta = ventas[position]
        holder.tvIdVenta.text = "Venta #${venta.id_venta}"
        holder.tvUsuario.text = venta.usuario?.nombre ?: "Sin usuario"
        holder.tvFecha.text = venta.fecha
        holder.tvTotal.text = "$ ${venta.total}"

        if (venta.status == 1) {
            holder.tvEstado.text = "Activa"
            holder.tvEstado.setBackgroundResource(R.drawable.bg_badge_active)
            holder.tvEstado.setTextColor(holder.itemView.context.getColor(R.color.active_text))
            holder.btnAnular.isEnabled = true
            holder.btnAnular.alpha = 1f
        } else {
            holder.tvEstado.text = "Anulada"
            holder.tvEstado.setBackgroundResource(R.drawable.bg_badge_inactive)
            holder.tvEstado.setTextColor(holder.itemView.context.getColor(R.color.inactive_text))
            holder.btnAnular.isEnabled = false
            holder.btnAnular.alpha = 0.5f
        }

        holder.btnVerDetalle.setOnClickListener { onVerDetalle(venta) }
        holder.btnAnular.setOnClickListener { onAnular(venta) }
    }

    override fun getItemCount() = ventas.size
}