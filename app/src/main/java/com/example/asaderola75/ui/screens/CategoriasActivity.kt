package com.example.asaderola75.ui.screens

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.asaderola75.R
import com.example.asaderola75.api.CreateCategoriaRequest
import com.example.asaderola75.api.SessionManager
import com.example.asaderola75.api.UpdateCategoriaRequest
import com.example.asaderola75.models.Categoria
import com.example.asaderola75.viewmodel.CategoriaViewModel

class CategoriasActivity : AppCompatActivity() {
    private val viewModel: CategoriaViewModel by viewModels()
    private lateinit var token: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_categorias)

        token = SessionManager(this).getToken() ?: return
        val rv = findViewById<RecyclerView>(R.id.rvCategorias)
        rv.layoutManager = LinearLayoutManager(this)

        findViewById<Button>(R.id.btnAgregar).setOnClickListener {
            mostrarDialogoCrear()
        }

        viewModel.getCategorias(token)

        viewModel.categorias.observe(this) { lista ->
            findViewById<TextView>(R.id.tvConteo).text = "${lista.size} categorías"
            rv.adapter = CategoriaAdapter(
                lista,
                onEditar = { categoria -> mostrarDialogoEditar(categoria) },
                onEliminar = { categoria -> confirmarEliminar(categoria) },
                onCambiarEstado = { categoria -> viewModel.toggleEstado(token, categoria.id_categoria) }
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
        val view = LayoutInflater.from(this).inflate(R.layout.dialog_categoria, null)

        AlertDialog.Builder(this, R.style.DialogOscuro)
            .setTitle("Nueva Categoría")
            .setView(view)
            .setPositiveButton("Crear") { _, _ ->
                val body = CreateCategoriaRequest(
                    nombre = view.findViewById<EditText>(R.id.etNombre).text.toString(),
                    descripcion = view.findViewById<EditText>(R.id.etDescripcion).text.toString().ifEmpty { null },
                    status = 1
                )
                viewModel.createCategoria(token, body)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun mostrarDialogoEditar(categoria: Categoria) {
        val view = LayoutInflater.from(this).inflate(R.layout.dialog_categoria, null)
        view.findViewById<EditText>(R.id.etNombre).setText(categoria.nombre)
        view.findViewById<EditText>(R.id.etDescripcion).setText(categoria.descripcion)

        AlertDialog.Builder(this, R.style.DialogOscuro)
            .setTitle("Editar Categoría")
            .setView(view)
            .setPositiveButton("Guardar") { _, _ ->
                val statusInt = categoria.status ?: 1
                val body = UpdateCategoriaRequest(
                    nombre = view.findViewById<EditText>(R.id.etNombre).text.toString(),
                    descripcion = view.findViewById<EditText>(R.id.etDescripcion).text.toString().ifEmpty { null },
                    status = statusInt
                )
                viewModel.updateCategoria(token, categoria.id_categoria, body)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun confirmarEliminar(categoria: Categoria) {
        AlertDialog.Builder(this, R.style.DialogOscuro)
            .setTitle("Eliminar Categoría")
            .setMessage("¿Estás seguro de eliminar ${categoria.nombre}?")
            .setPositiveButton("Eliminar") { _, _ ->
                viewModel.deleteCategoria(token, categoria.id_categoria)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
}