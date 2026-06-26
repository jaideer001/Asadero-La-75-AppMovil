package com.example.asaderola75.ui.screens

import android.app.AlertDialog
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.asaderola75.R
import com.example.asaderola75.api.CreateVentaRequest
import com.example.asaderola75.api.ProductoVentaItem
import com.example.asaderola75.api.SessionManager
import com.example.asaderola75.models.Producto
import com.example.asaderola75.viewmodel.ProductoViewModel
import com.example.asaderola75.viewmodel.VentaViewModel

class NuevaVentaActivity : AppCompatActivity() {
    private val ventaViewModel: VentaViewModel by viewModels()
    private val productoViewModel: ProductoViewModel by viewModels()
    private lateinit var token: String
    private var productos: List<Producto> = emptyList()
    private val productosEnVenta = mutableListOf<ProductoEnVenta>()
    private lateinit var adapter: ProductoVentaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nueva_venta)

        token = SessionManager(this).getToken() ?: return

        val rv = findViewById<RecyclerView>(R.id.rvProductosVenta)
        rv.layoutManager = LinearLayoutManager(this)

        adapter = ProductoVentaAdapter(
            productosEnVenta,
            onRemover = { position ->
                productosEnVenta.removeAt(position)
                adapter.notifyItemRemoved(position)
                actualizarTotal()
            },
            onTotalChanged = { actualizarTotal() }
        )
        rv.adapter = adapter

        productoViewModel.getProductos(token)
        productoViewModel.productos.observe(this) { lista ->
            productos = lista.filter { it.status == 1 }
            val spinnerProducto = findViewById<Spinner>(R.id.spinnerProducto)
            val adapterSpinner = ArrayAdapter(this, android.R.layout.simple_spinner_item, productos.map { it.nombre })
            adapterSpinner.setDropDownViewResource(R.layout.item_spinner_dropdown)
            spinnerProducto.adapter = adapterSpinner
        }

        findViewById<Button>(R.id.btnAgregarProducto).setOnClickListener {
            val spinnerProducto = findViewById<Spinner>(R.id.spinnerProducto)
            val etCantidad = findViewById<EditText>(R.id.etCantidad)
            val cantidad = etCantidad.text.toString().toIntOrNull() ?: 1
            val productoSeleccionado = productos.getOrNull(spinnerProducto.selectedItemPosition)

            if (productoSeleccionado == null) {
                Toast.makeText(this, "Selecciona un producto", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val existente = productosEnVenta.indexOfFirst { it.producto.id_producto == productoSeleccionado.id_producto }
            if (existente >= 0) {
                productosEnVenta[existente].cantidad += cantidad
                adapter.notifyItemChanged(existente)
            } else {
                productosEnVenta.add(ProductoEnVenta(productoSeleccionado, cantidad))
                adapter.notifyItemInserted(productosEnVenta.size - 1)
            }

            etCantidad.setText("")
            actualizarTotal()
        }

        findViewById<Button>(R.id.btnConfirmarVenta).setOnClickListener {
            if (productosEnVenta.isEmpty()) {
                Toast.makeText(this, "Agrega al menos un producto", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            confirmarVenta()
        }

        ventaViewModel.mensaje.observe(this) { msg ->
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
            finish()
        }

        ventaViewModel.error.observe(this) { error ->
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
        }
    }

    private fun actualizarTotal() {
        val total = adapter.calcularTotal()
        findViewById<TextView>(R.id.tvTotalNuevaVenta).text = "Total: $ ${"%.0f".format(total)}"
    }

    private fun confirmarVenta() {
        val total = adapter.calcularTotal()
        AlertDialog.Builder(this)
            .setTitle("Confirmar Venta")
            .setMessage("Total: $ ${"%.0f".format(total)}\n¿Confirmar la venta?")
            .setPositiveButton("Confirmar") { _, _ ->
                val body = CreateVentaRequest(
                    productos = productosEnVenta.map {
                        ProductoVentaItem(
                            id_producto = it.producto.id_producto,
                            cantidad = it.cantidad
                        )
                    }
                )
                ventaViewModel.createVenta(token, body)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
}