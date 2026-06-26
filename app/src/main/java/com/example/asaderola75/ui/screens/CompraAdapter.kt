package com.example.asaderola75.ui.screens

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.asaderola75.R
import com.example.asaderola75.models.Compra

class CompraAdapter(
    private val compras: List<Compra>,
    private val onVerDetalle: (Compra) -> Unit,
    private val onAnular: (Compra) -> Unit
) : RecyclerView.Adapter<CompraAdapter.CompraViewHolder>() {

    class CompraViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvIdCompra: TextView = view.findViewById(R.id.tvIdCompra)
        val tvProveedor: TextView = view.findViewById(R.id.tvProveedor)
        val tvFecha: TextView = view.findViewById(R.id.tvFecha)
        val tvTotal: TextView = view.findViewById(R.id.tvTotal)
        val tvEstado: TextView = view.findViewById(R.id.tvEstado)
        val btnVerDetalle: Button = view.findViewById(R.id.btnVerDetalle)
        val btnAnular: Button = view.findViewById(R.id.btnAnular)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CompraViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_compra, parent, false)
        return CompraViewHolder(view)
    }

    override fun onBindViewHolder(holder: CompraViewHolder, position: Int) {
        val compra = compras[position]
        holder.tvIdCompra.text = "Compra #${compra.id_compra}"
        holder.tvProveedor.text = compra.proveedor?.nombre ?: "Sin proveedor"
        holder.tvFecha.text = compra.fecha
        holder.tvTotal.text = "$ ${compra.total}"

        if (compra.status == 1) {
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

        holder.btnVerDetalle.setOnClickListener { onVerDetalle(compra) }
        holder.btnAnular.setOnClickListener { onAnular(compra) }
    }

    override fun getItemCount() = compras.size
}