package com.example.asaderola75.ui.screens

import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.asaderola75.R
import com.example.asaderola75.api.SessionManager
import com.example.asaderola75.viewmodel.CompraViewModel

class DetalleCompraActivity : AppCompatActivity() {
    private val viewModel: CompraViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_compra)

        val token = SessionManager(this).getToken() ?: return
        val idCompra = intent.getIntExtra("id_compra", -1)
        if (idCompra == -1) return

        val rv = findViewById<RecyclerView>(R.id.rvDetallesCompra)
        rv.layoutManager = LinearLayoutManager(this)

        viewModel.getCompraDetalle(token, idCompra)

        viewModel.compraDetalle.observe(this) { compra ->
            findViewById<TextView>(R.id.tvTituloCompra).text = "Compra #${compra.id_compra}"
            findViewById<TextView>(R.id.tvTotalCompra).text = "Total: $ ${compra.total}"
            findViewById<TextView>(R.id.tvInfoProveedor).text = "Proveedor: ${compra.proveedor?.nombre ?: "N/A"}"
            findViewById<TextView>(R.id.tvInfoUsuario).text = "Usuario: ${compra.usuario?.nombre ?: "N/A"}"
            findViewById<TextView>(R.id.tvInfoFecha).text = "Fecha: ${compra.fecha}"
            findViewById<TextView>(R.id.tvInfoEstado).text = "Estado: ${if (compra.status == 1) "Activa" else "Anulada"}"

            rv.adapter = DetalleCompraAdapter(compra.detalles ?: emptyList())
        }
    }
}