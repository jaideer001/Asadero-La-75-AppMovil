package com.example.asaderola75.ui.screens

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.asaderola75.R
import com.example.asaderola75.api.SessionManager
import com.example.asaderola75.viewmodel.VentaViewModel

class VentasActivity : AppCompatActivity() {
    private val viewModel: VentaViewModel by viewModels()
    private lateinit var token: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ventas)

        token = SessionManager(this).getToken() ?: return
        val rv = findViewById<RecyclerView>(R.id.rvVentas)
        rv.layoutManager = LinearLayoutManager(this)

        findViewById<Button>(R.id.btnNuevaVenta).setOnClickListener {
            startActivity(Intent(this, NuevaVentaActivity::class.java))
        }

        viewModel.getVentas(token)

        viewModel.ventas.observe(this) { lista ->
            findViewById<TextView>(R.id.tvConteo).text = "${lista.size} ventas"
            rv.adapter = VentaAdapter(
                lista,
                onVerDetalle = { venta ->
                    val intent = Intent(this, DetalleVentaActivity::class.java)
                    intent.putExtra("id_venta", venta.id_venta)
                    startActivity(intent)
                },
                onAnular = { venta -> confirmarAnular(venta.id_venta) }
            )
        }

        viewModel.mensaje.observe(this) { msg ->
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        }

        viewModel.error.observe(this) { error ->
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getVentas(token)
    }

    private fun confirmarAnular(idVenta: Int) {
        AlertDialog.Builder(this)
            .setTitle("Anular Venta")
            .setMessage("¿Estás seguro de anular la venta #$idVenta?")
            .setPositiveButton("Anular") { _, _ ->
                viewModel.anularVenta(token, idVenta)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
}