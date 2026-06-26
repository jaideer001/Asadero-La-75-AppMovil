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
import com.example.asaderola75.viewmodel.CompraViewModel

class ComprasActivity : AppCompatActivity() {
    private val viewModel: CompraViewModel by viewModels()
    private lateinit var token: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compras)

        token = SessionManager(this).getToken() ?: return
        val rv = findViewById<RecyclerView>(R.id.rvCompras)
        rv.layoutManager = LinearLayoutManager(this)

        findViewById<Button>(R.id.btnNuevaCompra).setOnClickListener {
            startActivity(Intent(this, NuevaCompraActivity::class.java))
        }

        viewModel.getCompras(token)

        viewModel.compras.observe(this) { lista ->
            findViewById<TextView>(R.id.tvConteo).text = "${lista.size} compras"
            rv.adapter = CompraAdapter(
                lista,
                onVerDetalle = { compra ->
                    val intent = Intent(this, DetalleCompraActivity::class.java)
                    intent.putExtra("id_compra", compra.id_compra)
                    startActivity(intent)
                },
                onAnular = { compra -> confirmarAnular(compra.id_compra) }
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
        viewModel.getCompras(token)
    }

    private fun confirmarAnular(idCompra: Int) {
        AlertDialog.Builder(this)
            .setTitle("Anular Compra")
            .setMessage("¿Estás seguro de anular la compra #$idCompra?")
            .setPositiveButton("Anular") { _, _ ->
                viewModel.anularCompra(token, idCompra)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
}