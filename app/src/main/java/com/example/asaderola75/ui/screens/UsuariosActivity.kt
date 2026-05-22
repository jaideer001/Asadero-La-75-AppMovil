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
import com.example.asaderola75.api.CreateUsuarioRequest
import com.example.asaderola75.api.SessionManager
import com.example.asaderola75.api.UpdateUsuarioRequest
import com.example.asaderola75.models.Usuario
import com.example.asaderola75.viewmodel.UsuarioViewModel

class UsuariosActivity : AppCompatActivity() {
    private val viewModel: UsuarioViewModel by viewModels()
    private lateinit var token: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_usuarios)

        token = SessionManager(this).getToken() ?: return
        val rv = findViewById<RecyclerView>(R.id.rvUsuarios)
        rv.layoutManager = LinearLayoutManager(this)

        findViewById<Button>(R.id.btnAgregar).setOnClickListener {
            mostrarDialogoCrear()
        }

        viewModel.getUsuarios(token)

        viewModel.usuarios.observe(this) { lista ->
            rv.adapter = UsuarioAdapter(
                lista,
                onEditar = { usuario -> mostrarDialogoEditar(usuario) },
                onEliminar = { usuario -> confirmarEliminar(usuario) },
                onCambiarEstado = { usuario ->
                    viewModel.cambiarEstado(token, usuario.id_usuario)
                }
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
        val view = LayoutInflater.from(this).inflate(R.layout.dialog_crear_usuario, null)

        AlertDialog.Builder(this, R.style.DialogOscuro)
            .setTitle("Nuevo Usuario")
            .setView(view)
            .setPositiveButton("Crear") { _, _ ->
                val body = CreateUsuarioRequest(
                    nombre = view.findViewById<EditText>(R.id.etNombre).text.toString(),
                    usuario = view.findViewById<EditText>(R.id.etUsuario).text.toString(),
                    correo = view.findViewById<EditText>(R.id.etCorreo).text.toString(),
                    contrasena = view.findViewById<EditText>(R.id.etContrasena).text.toString(),
                    id_rol = 1,
                    estado = 1
                )
                viewModel.createUsuario(token, body)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun mostrarDialogoEditar(usuario: Usuario) {
        val view = LayoutInflater.from(this).inflate(R.layout.dialog_editar_usuario, null)
        view.findViewById<EditText>(R.id.etNombre).setText(usuario.nombre)
        view.findViewById<EditText>(R.id.etUsuario).setText(usuario.usuario)
        view.findViewById<EditText>(R.id.etCorreo).setText(usuario.correo)

        AlertDialog.Builder(this, R.style.DialogOscuro)
            .setTitle("Editar Usuario")
            .setView(view)
            .setPositiveButton("Guardar") { _, _ ->
                val body = UpdateUsuarioRequest(
                    nombre = view.findViewById<EditText>(R.id.etNombre).text.toString(),
                    usuario = view.findViewById<EditText>(R.id.etUsuario).text.toString(),
                    correo = view.findViewById<EditText>(R.id.etCorreo).text.toString(),
                    contrasena = view.findViewById<EditText>(R.id.etContrasena).text.toString().ifEmpty { null },
                    id_rol = usuario.id_rol,
                    estado = usuario.estado
                )
                viewModel.updateUsuario(token, usuario.id_usuario, body)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun confirmarEliminar(usuario: Usuario) {
        AlertDialog.Builder(this, R.style.DialogOscuro)
            .setTitle("Eliminar Usuario")
            .setMessage("¿Estás seguro de eliminar a ${usuario.nombre}?")
            .setPositiveButton("Eliminar") { _, _ ->
                viewModel.deleteUsuario(token, usuario.id_usuario)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
}