package com.example.asaderola75.api

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("asadero_prefs", Context.MODE_PRIVATE)

    fun saveToken(token: String) {
        prefs.edit().putString("token", token).apply()
    }

    fun getToken(): String? {
        return prefs.getString("token", null)
    }

    fun saveRol(rol: Int) {
        prefs.edit().putInt("rol", rol).apply()
    }

    fun getRol(): Int {
        return prefs.getInt("rol", 1)
    }

    fun clearToken() {
        prefs.edit().clear().apply()
    }
}