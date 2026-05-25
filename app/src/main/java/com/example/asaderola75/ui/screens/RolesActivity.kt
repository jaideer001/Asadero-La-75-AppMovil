package com.example.asaderola75.ui.screens

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.asaderola75.R
import com.example.asaderola75.api.CreateRolRequest
import com.example.asaderola75.api.SessionManager
import com.example.asaderola75.api.UpdateRolRequest
import com.example.asaderola75.models.Rol
import com.example.asaderola75.viewmodel.RolViewModel

class RolesActivity : AppCompatActivity() {
    private val viewModel: RolViewModel by viewModels()
    private lateinit var token: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_roles)

        token = SessionManager(this).getToken() ?: return
        val rv = findViewById<RecyclerView>(R.id.rvRoles)
        rv.layoutManager = LinearLayoutManager(this)

        findViewById<Button>(R.id.btnAgregar).setOnClickListener {
            mostrarDialogoCrear()
        }

        viewModel.getRoles(token)

        viewModel.roles.observe(this) { lista ->
            rv.adapter = RolAdapter(
                lista,
                onEditar = { rol -> mostrarDialogoEditar(rol) },
                onEliminar = { rol -> confirmarEliminar(rol) },
                onCambiarEstado = { rol -> viewModel.toggleEstado(token, rol.id_rol) }
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
        val view = LayoutInflater.from(this).inflate(R.layout.dialog_rol, null)

        AlertDialog.Builder(this, R.style.DialogOscuro)
            .setTitle("Nuevo Rol")
            .setView(view)
            .setPositiveButton("Crear") { _, _ ->
                val body = CreateRolRequest(
                    nombre = view.findViewById<EditText>(R.id.etNombre).text.toString(),
                    descripcion = view.findViewById<EditText>(R.id.etDescripcion).text.toString().ifEmpty { null },
                    status = 1
                )
                viewModel.createRol(token, body)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun mostrarDialogoEditar(rol: Rol) {
        val view = LayoutInflater.from(this).inflate(R.layout.dialog_rol, null)
        view.findViewById<EditText>(R.id.etNombre).setText(rol.nombre)
        view.findViewById<EditText>(R.id.etDescripcion).setText(rol.descripcion)

        AlertDialog.Builder(this, R.style.DialogOscuro)
            .setTitle("Editar Rol")
            .setView(view)
            .setPositiveButton("Guardar") { _, _ ->
                val body = UpdateRolRequest(
                    nombre = view.findViewById<EditText>(R.id.etNombre).text.toString(),
                    descripcion = view.findViewById<EditText>(R.id.etDescripcion).text.toString().ifEmpty { null },
                    status = rol.status
                )
                viewModel.updateRol(token, rol.id_rol, body)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun confirmarEliminar(rol: Rol) {
        AlertDialog.Builder(this, R.style.DialogOscuro)
            .setTitle("Eliminar Rol")
            .setMessage("¿Estás seguro de eliminar el rol ${rol.nombre}?")
            .setPositiveButton("Eliminar") { _, _ ->
                viewModel.deleteRol(token, rol.id_rol)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
}