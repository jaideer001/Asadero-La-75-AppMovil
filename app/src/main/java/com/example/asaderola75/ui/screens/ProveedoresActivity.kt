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
import com.example.asaderola75.api.CreateProveedorRequest
import com.example.asaderola75.api.SessionManager
import com.example.asaderola75.api.UpdateProveedorRequest
import com.example.asaderola75.models.Proveedor
import com.example.asaderola75.viewmodel.ProveedorViewModel

class ProveedoresActivity : AppCompatActivity() {

    private val viewModel: ProveedorViewModel by viewModels()
    private lateinit var token: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_proveedores)

        token = SessionManager(this).getToken() ?: return

        val rv = findViewById<RecyclerView>(R.id.rvProveedores)
        rv.layoutManager = LinearLayoutManager(this)

        findViewById<Button>(R.id.btnAgregar).setOnClickListener {
            mostrarDialogoCrear()
        }

        viewModel.getProveedores(token)

        viewModel.proveedores.observe(this) { lista ->
            findViewById<TextView>(R.id.tvConteo).text = "${lista.size} proveedores"

            rv.adapter = ProveedorAdapter(
                lista,
                onEditar = { mostrarDialogoEditar(it) },
                onEliminar = { confirmarEliminar(it) },
                onCambiarEstado = {
                    viewModel.toggleEstado(token, it.id_proveedor)
                }
            )
        }

        viewModel.mensaje.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }

        viewModel.error.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }
    }

    private fun mostrarDialogoCrear() {

        val view =
            LayoutInflater.from(this).inflate(R.layout.dialog_proveedor, null)

        val dialog = AlertDialog.Builder(this, R.style.DialogOscuro)
            .setTitle("Nuevo Proveedor")
            .setView(view)
            .setPositiveButton("Crear") { _, _ ->

                val body = CreateProveedorRequest(
                    nombre = view.findViewById<EditText>(R.id.etNombre).text.toString(),
                    telefono = view.findViewById<EditText>(R.id.etTelefono).text.toString().ifEmpty { null },
                    direccion = view.findViewById<EditText>(R.id.etDireccion).text.toString().ifEmpty { null },
                    correo = view.findViewById<EditText>(R.id.etCorreo).text.toString().ifEmpty { null },
                    status = 1
                )

                viewModel.createProveedor(token, body)

            }
            .setNegativeButton("Cancelar", null)
            .create()

        dialog.show()

        dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            .setTextColor(getColor(R.color.fire))

        dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
            .setTextColor(getColor(android.R.color.white))
    }

    private fun mostrarDialogoEditar(proveedor: Proveedor) {

        val view =
            LayoutInflater.from(this).inflate(R.layout.dialog_proveedor, null)

        view.findViewById<EditText>(R.id.etNombre).setText(proveedor.nombre)
        view.findViewById<EditText>(R.id.etTelefono).setText(proveedor.telefono)
        view.findViewById<EditText>(R.id.etDireccion).setText(proveedor.direccion)
        view.findViewById<EditText>(R.id.etCorreo).setText(proveedor.correo)

        val dialog = AlertDialog.Builder(this, R.style.DialogOscuro)
            .setTitle("Editar Proveedor")
            .setView(view)
            .setPositiveButton("Guardar") { _, _ ->

                val body = UpdateProveedorRequest(
                    nombre = view.findViewById<EditText>(R.id.etNombre).text.toString(),
                    telefono = view.findViewById<EditText>(R.id.etTelefono).text.toString().ifEmpty { null },
                    direccion = view.findViewById<EditText>(R.id.etDireccion).text.toString().ifEmpty { null },
                    correo = view.findViewById<EditText>(R.id.etCorreo).text.toString().ifEmpty { null },
                    status = proveedor.status ?: 1
                )

                viewModel.updateProveedor(
                    token,
                    proveedor.id_proveedor,
                    body
                )

            }
            .setNegativeButton("Cancelar", null)
            .create()

        dialog.show()

        dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            .setTextColor(getColor(R.color.fire))

        dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
            .setTextColor(getColor(android.R.color.white))
    }

    private fun confirmarEliminar(proveedor: Proveedor) {

        val dialog = AlertDialog.Builder(this, R.style.DialogOscuro)
            .setTitle("Eliminar Proveedor")
            .setMessage("¿Estás seguro de eliminar a ${proveedor.nombre}?")
            .setPositiveButton("Eliminar") { _, _ ->

                viewModel.deleteProveedor(
                    token,
                    proveedor.id_proveedor
                )

            }
            .setNegativeButton("Cancelar", null)
            .create()

        dialog.show()

        dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            .setTextColor(getColor(R.color.fire))

        dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
            .setTextColor(getColor(android.R.color.white))
    }
}