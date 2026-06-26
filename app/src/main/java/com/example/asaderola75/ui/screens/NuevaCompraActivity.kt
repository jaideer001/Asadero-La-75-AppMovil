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
import com.example.asaderola75.api.CreateCompraRequest
import com.example.asaderola75.api.ProductoCompraItem
import com.example.asaderola75.api.SessionManager
import com.example.asaderola75.models.Producto
import com.example.asaderola75.models.Proveedor
import com.example.asaderola75.viewmodel.CompraViewModel
import com.example.asaderola75.viewmodel.ProductoViewModel
import com.example.asaderola75.viewmodel.ProveedorViewModel
import java.time.LocalDate

class NuevaCompraActivity : AppCompatActivity() {
    private val compraViewModel: CompraViewModel by viewModels()
    private val productoViewModel: ProductoViewModel by viewModels()
    private val proveedorViewModel: ProveedorViewModel by viewModels()
    private lateinit var token: String
    private var productos: List<Producto> = emptyList()
    private var proveedores: List<Proveedor> = emptyList()
    private val productosEnCompra = mutableListOf<ProductoEnCompra>()
    private lateinit var adapter: ProductoCompraAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nueva_compra)

        token = SessionManager(this).getToken() ?: return

        val rv = findViewById<RecyclerView>(R.id.rvProductosCompra)
        rv.layoutManager = LinearLayoutManager(this)

        adapter = ProductoCompraAdapter(
            productosEnCompra,
            onRemover = { position ->
                productosEnCompra.removeAt(position)
                adapter.notifyItemRemoved(position)
                actualizarTotal()
            },
            onTotalChanged = { actualizarTotal() }
        )
        rv.adapter = adapter

        val etFecha = findViewById<EditText>(R.id.etFecha)
        etFecha.setText(LocalDate.now().toString())

        proveedorViewModel.getProveedores(token)
        proveedorViewModel.proveedores.observe(this) { lista ->
            proveedores = lista.filter { it.status == 1 }
            val spinnerProveedor = findViewById<Spinner>(R.id.spinnerProveedor)
            val adapterSpinner = ArrayAdapter(this, android.R.layout.simple_spinner_item, proveedores.map { it.nombre })
            adapterSpinner.setDropDownViewResource(R.layout.item_spinner_dropdown)
            spinnerProveedor.adapter = adapterSpinner
        }

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
            val etPrecio = findViewById<EditText>(R.id.etPrecioUnitario)
            val cantidad = etCantidad.text.toString().toIntOrNull() ?: 1
            val precio = etPrecio.text.toString().toDoubleOrNull() ?: 0.0
            val productoSeleccionado = productos.getOrNull(spinnerProducto.selectedItemPosition)

            if (productoSeleccionado == null) {
                Toast.makeText(this, "Selecciona un producto", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (precio <= 0) {
                Toast.makeText(this, "Ingresa el precio unitario", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val existente = productosEnCompra.indexOfFirst { it.producto.id_producto == productoSeleccionado.id_producto }
            if (existente >= 0) {
                productosEnCompra[existente].cantidad += cantidad
                adapter.notifyItemChanged(existente)
            } else {
                productosEnCompra.add(ProductoEnCompra(productoSeleccionado, cantidad, precio))
                adapter.notifyItemInserted(productosEnCompra.size - 1)
            }

            etCantidad.setText("")
            etPrecio.setText("")
            actualizarTotal()
        }

        findViewById<Button>(R.id.btnConfirmarCompra).setOnClickListener {
            if (productosEnCompra.isEmpty()) {
                Toast.makeText(this, "Agrega al menos un producto", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            confirmarCompra()
        }

        compraViewModel.mensaje.observe(this) { msg ->
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
            finish()
        }

        compraViewModel.error.observe(this) { error ->
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
        }
    }

    private fun actualizarTotal() {
        val total = adapter.calcularTotal()
        findViewById<TextView>(R.id.tvTotalNuevaCompra).text = "Total: $ ${"%.0f".format(total)}"
    }

    private fun confirmarCompra() {
        val total = adapter.calcularTotal()
        val spinnerProveedor = findViewById<Spinner>(R.id.spinnerProveedor)
        val proveedorSeleccionado = proveedores.getOrNull(spinnerProveedor.selectedItemPosition)
        val fecha = findViewById<EditText>(R.id.etFecha).text.toString()

        AlertDialog.Builder(this)
            .setTitle("Confirmar Compra")
            .setMessage("Total: $ ${"%.0f".format(total)}\n¿Confirmar la compra?")
            .setPositiveButton("Confirmar") { _, _ ->
                val body = CreateCompraRequest(
                    id_proveedor = proveedorSeleccionado?.id_proveedor ?: 1,
                    fecha = fecha,
                    total_compra = total,
                    productos = productosEnCompra.map {
                        ProductoCompraItem(
                            id_producto = it.producto.id_producto,
                            cantidad = it.cantidad,
                            precio_unitario = it.precioUnitario
                        )
                    }
                )
                compraViewModel.createCompra(token, body)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
}