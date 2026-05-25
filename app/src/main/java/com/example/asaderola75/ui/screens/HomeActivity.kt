package com.example.asaderola75.ui.screens

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.asaderola75.R
import com.example.asaderola75.api.SessionManager

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        findViewById<LinearLayout>(R.id.btnUsuarios).setOnClickListener {
            startActivity(Intent(this, UsuariosActivity::class.java))
        }

        findViewById<LinearLayout>(R.id.btnRoles).setOnClickListener {
            startActivity(Intent(this, RolesActivity::class.java))
        }

        findViewById<LinearLayout>(R.id.btnCerrarSesion).setOnClickListener {
            SessionManager(this).clearToken()
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }
}