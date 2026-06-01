package com.example.asaderola75.ui.screens

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
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
import com.example.asaderola75.api.CreateProductoRequest
import com.example.asaderola75.api.SessionManager
import com.example.asaderola75.api.UpdateProductoRequest
import com.example.asaderola75.models.Categoria
import com.example.asaderola75.models.Producto
import com.example.asaderola75.viewmodel.CategoriaViewModel
import com.example.asaderola75.viewmodel.ProductoViewModel

class ProductosActivity : AppCompatActivity() {
    private val viewModel: ProductoViewModel by viewModels()
    private val categoriaViewModel: CategoriaViewModel by viewModels()
    private lateinit var token: String
    private var categorias: List<Categoria> = emptyList()
    private val tipos = listOf("venta", "insumo")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_productos)

        token = SessionManager(this).getToken() ?: return
        val rv = findViewById<RecyclerView>(R.id.rvProductos)
        rv.layoutManager = LinearLayoutManager(this)

        categoriaViewModel.getCategorias(token)
        categoriaViewModel.categorias.observe(this) { lista ->
            categorias = lista
        }

        findViewById<Button>(R.id.btnAgregar).setOnClickListener {
            mostrarDialogoCrear()
        }

        viewModel.getProductos(token)

        viewModel.productos.observe(this) { lista ->
            findViewById<TextView>(R.id.tvConteo).text = "${lista.size} productos"
            rv.adapter = ProductoAdapter(
                lista,
                onEditar = { producto -> mostrarDialogoEditar(producto) },
                onEliminar = { producto -> confirmarEliminar(producto) },
                onCambiarEstado = { producto -> viewModel.toggleEstado(token, producto.id_producto) }
            )
        }

        viewModel.mensaje.observe(this) { msg ->
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        }

        viewModel.error.observe(this) { error ->
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
        }
    }

    private fun mostrarDialogoCrear() {
        val view = LayoutInflater.from(this).inflate(R.layout.dialog_producto, null)

        val spinnerTipo = view.findViewById<Spinner>(R.id.spinnerTipo)
        val adapterTipo = ArrayAdapter(this, android.R.layout.simple_spinner_item, tipos)
        adapterTipo.setDropDownViewResource(R.layout.item_spinner_dropdown)
        spinnerTipo.adapter = adapterTipo

        val spinnerCat = view.findViewById<Spinner>(R.id.spinnerCategoria)
        val adapterCat = ArrayAdapter(this, android.R.layout.simple_spinner_item, categorias.map { it.nombre })
        adapterCat.setDropDownViewResource(R.layout.item_spinner_dropdown)
        spinnerCat.adapter = adapterCat

        AlertDialog.Builder(this, R.style.DialogOscuro)
            .setTitle("Nuevo Producto")
            .setView(view)
            .setPositiveButton("Crear") { _, _ ->
                val categoriaSeleccionada = categorias.getOrNull(spinnerCat.selectedItemPosition)
                val body = CreateProductoRequest(
                    nombre = view.findViewById<EditText>(R.id.etNombre).text.toString(),
                    descripcion = view.findViewById<EditText>(R.id.etDescripcion).text.toString().ifEmpty { null },
                    stock_actual = view.findViewById<EditText>(R.id.etStock).text.toString().toDoubleOrNull() ?: 0.0,
                    unidad_medida = view.findViewById<EditText>(R.id.etUnidad).text.toString(),
                    precio_compra = view.findViewById<EditText>(R.id.etPrecioCompra).text.toString().toDoubleOrNull(),
                    precio_venta = view.findViewById<EditText>(R.id.etPrecioVenta).text.toString().toDoubleOrNull(),
                    tipo = tipos[spinnerTipo.selectedItemPosition],
                    status = 1,
                    id_categoria = categoriaSeleccionada?.id_categoria ?: 1
                )
                viewModel.createProducto(token, body)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun mostrarDialogoEditar(producto: Producto) {
        val view = LayoutInflater.from(this).inflate(R.layout.dialog_producto, null)
        view.findViewById<EditText>(R.id.etNombre).setText(producto.nombre)
        view.findViewById<EditText>(R.id.etDescripcion).setText(producto.descripcion)
        view.findViewById<EditText>(R.id.etStock).setText(producto.stock_actual)
        view.findViewById<EditText>(R.id.etUnidad).setText(producto.unidad_medida)
        view.findViewById<EditText>(R.id.etPrecioCompra).setText(producto.precio_compra)
        view.findViewById<EditText>(R.id.etPrecioVenta).setText(producto.precio_venta)

        val spinnerTipo = view.findViewById<Spinner>(R.id.spinnerTipo)
        val adapterTipo = ArrayAdapter(this, android.R.layout.simple_spinner_item, tipos)
        adapterTipo.setDropDownViewResource(R.layout.item_spinner_dropdown)
        spinnerTipo.adapter = adapterTipo
        spinnerTipo.setSelection(tipos.indexOf(producto.tipo).takeIf { it >= 0 } ?: 0)

        val spinnerCat = view.findViewById<Spinner>(R.id.spinnerCategoria)
        val adapterCat = ArrayAdapter(this, android.R.layout.simple_spinner_item, categorias.map { it.nombre })
        adapterCat.setDropDownViewResource(R.layout.item_spinner_dropdown)
        spinnerCat.adapter = adapterCat
        val catIndex = categorias.indexOfFirst { it.id_categoria == producto.id_categoria }
        if (catIndex >= 0) spinnerCat.setSelection(catIndex)

        AlertDialog.Builder(this, R.style.DialogOscuro)
            .setTitle("Editar Producto")
            .setView(view)
            .setPositiveButton("Guardar") { _, _ ->
                val categoriaSeleccionada = categorias.getOrNull(spinnerCat.selectedItemPosition)
                val body = UpdateProductoRequest(
                    nombre = view.findViewById<EditText>(R.id.etNombre).text.toString(),
                    descripcion = view.findViewById<EditText>(R.id.etDescripcion).text.toString().ifEmpty { null },
                    stock_actual = view.findViewById<EditText>(R.id.etStock).text.toString().toDoubleOrNull() ?: 0.0,
                    unidad_medida = view.findViewById<EditText>(R.id.etUnidad).text.toString(),
                    precio_compra = view.findViewById<EditText>(R.id.etPrecioCompra).text.toString().toDoubleOrNull(),
                    precio_venta = view.findViewById<EditText>(R.id.etPrecioVenta).text.toString().toDoubleOrNull(),
                    tipo = tipos[spinnerTipo.selectedItemPosition],
                    status = producto.status ?: 1,
                    id_categoria = categoriaSeleccionada?.id_categoria ?: producto.id_categoria
                )
                viewModel.updateProducto(token, producto.id_producto, body)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun confirmarEliminar(producto: Producto) {
        AlertDialog.Builder(this, R.style.DialogOscuro)
            .setTitle("Eliminar Producto")
            .setMessage("¿Estás seguro de eliminar ${producto.nombre}?")
            .setPositiveButton("Eliminar") { _, _ ->
                viewModel.deleteProducto(token, producto.id_producto)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
}