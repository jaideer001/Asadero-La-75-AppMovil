package com.example.asaderola75.ui.screens

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.asaderola75.R
import com.example.asaderola75.api.SessionManager

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val session = SessionManager(this)
        val rol = session.getRol()

        // Ocultar todos primero
        val btnUsuarios = findViewById<LinearLayout>(R.id.btnUsuarios)
        val btnRoles = findViewById<LinearLayout>(R.id.btnRoles)
        val btnProductos = findViewById<LinearLayout>(R.id.btnProductos)
        val btnVentas = findViewById<LinearLayout>(R.id.btnVentas)
        val btnProveedores = findViewById<LinearLayout>(R.id.btnProveedores)
        val btnProductosMenu = findViewById<LinearLayout>(R.id.btnProductosMenu)
        val btnCompras = findViewById<LinearLayout>(R.id.btnCompras)

        // Ocultar todo
        btnUsuarios.visibility = View.GONE
        btnRoles.visibility = View.GONE
        btnProductos.visibility = View.GONE
        btnVentas.visibility = View.GONE
        btnProveedores.visibility = View.GONE
        btnProductosMenu.visibility = View.GONE
        btnCompras.visibility = View.GONE

        when (rol) {
            1 -> { // Administrador - todos
                btnUsuarios.visibility = View.VISIBLE
                btnRoles.visibility = View.VISIBLE
                btnProductos.visibility = View.VISIBLE
                btnVentas.visibility = View.VISIBLE
                btnProveedores.visibility = View.VISIBLE
                btnProductosMenu.visibility = View.VISIBLE
                btnCompras.visibility = View.VISIBLE
            }
            2 -> { // Cajero - proveedores, categorías, productos, compras
                btnProveedores.visibility = View.VISIBLE
                btnProductos.visibility = View.VISIBLE
                btnProductosMenu.visibility = View.VISIBLE
                btnCompras.visibility = View.VISIBLE
            }
            3 -> { // Vendedor - productos y ventas
                btnProductosMenu.visibility = View.VISIBLE
                btnVentas.visibility = View.VISIBLE
            }
            4 -> { // Compras - proveedores, categorías, productos, compras
                btnProveedores.visibility = View.VISIBLE
                btnProductos.visibility = View.VISIBLE
                btnProductosMenu.visibility = View.VISIBLE
                btnCompras.visibility = View.VISIBLE
            }
        }

        btnUsuarios.setOnClickListener {
            startActivity(Intent(this, UsuariosActivity::class.java))
        }
        btnRoles.setOnClickListener {
            startActivity(Intent(this, RolesActivity::class.java))
        }
        btnProductos.setOnClickListener {
            startActivity(Intent(this, CategoriasActivity::class.java))
        }
        btnVentas.setOnClickListener {
            startActivity(Intent(this, VentasActivity::class.java))
        }
        btnProveedores.setOnClickListener {
            startActivity(Intent(this, ProveedoresActivity::class.java))
        }
        btnProductosMenu.setOnClickListener {
            startActivity(Intent(this, ProductosActivity::class.java))
        }
        btnCompras.setOnClickListener {
            startActivity(Intent(this, ComprasActivity::class.java))
        }

        findViewById<LinearLayout>(R.id.btnCerrarSesion).setOnClickListener {
            session.clearToken()
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }
}