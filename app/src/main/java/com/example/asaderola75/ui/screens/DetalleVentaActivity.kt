package com.example.asaderola75.ui.screens

import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.asaderola75.R
import com.example.asaderola75.api.SessionManager
import com.example.asaderola75.viewmodel.VentaViewModel

class DetalleVentaActivity : AppCompatActivity() {
    private val viewModel: VentaViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_venta)

        val token = SessionManager(this).getToken() ?: return
        val idVenta = intent.getIntExtra("id_venta", -1)
        if (idVenta == -1) return

        val rv = findViewById<RecyclerView>(R.id.rvDetalles)
        rv.layoutManager = LinearLayoutManager(this)

        viewModel.getVentaDetalle(token, idVenta)

        viewModel.ventaDetalle.observe(this) { venta ->
            findViewById<TextView>(R.id.tvTituloVenta).text = "Venta #${venta.id_venta}"
            findViewById<TextView>(R.id.tvTotalVenta).text = "Total: $ ${venta.total}"
            findViewById<TextView>(R.id.tvInfoUsuario).text = "Vendedor: ${venta.usuario?.nombre ?: "N/A"}"
            findViewById<TextView>(R.id.tvInfoFecha).text = "Fecha: ${venta.fecha}"
            findViewById<TextView>(R.id.tvInfoEstado).text = "Estado: ${if (venta.status == 1) "Activa" else "Anulada"}"

            rv.adapter = DetalleVentaAdapter(venta.detalle_ventas ?: emptyList())
        }
    }
}