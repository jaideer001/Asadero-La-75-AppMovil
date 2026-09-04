package com.example.asaderola75.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.asaderola75.api.ApiClient
import com.example.asaderola75.api.CreateUsuarioRequest
import com.example.asaderola75.api.UpdateUsuarioRequest
import com.example.asaderola75.models.Usuario
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Response

class UsuarioViewModel : ViewModel() {
    private val _usuarios = MutableLiveData<List<Usuario>>()
    val usuarios: LiveData<List<Usuario>> = _usuarios

    private val _mensaje = MutableLiveData<String>()
    val mensaje: LiveData<String> = _mensaje

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    // Función auxiliar para leer el cuerpo de error JSON devuelto por el backend
    private fun <T> obtenerMensajeError(response: Response<T>, mensajePorDefecto: String): String {
        return try {
            val errorBody = response.errorBody()?.string()
            if (!errorBody.isNullOrEmpty()) {
                val jsonObject = JSONObject(errorBody)
                // Intenta obtener la clave "message" o "error" enviada desde la API
                when {
                    jsonObject.has("message") -> jsonObject.getString("message")
                    jsonObject.has("error") -> jsonObject.getString("error")
                    else -> mensajePorDefecto
                }
            } else {
                "$mensajePorDefecto (${response.code()})"
            }
        } catch (e: Exception) {
            mensajePorDefecto
        }
    }

    fun getUsuarios(token: String) {
        viewModelScope.launch {
            try {
                val response = ApiClient.instance.getUsuarios("Bearer $token")
                if (response.isSuccessful) {
                    _usuarios.value = response.body()?.data
                } else {
                    _error.value = obtenerMensajeError(response, "Error al obtener usuarios")
                }
            } catch (e: Exception) {
                _error.value = "Error de red: ${e.localizedMessage ?: "Sin conexión"}"
            }
        }
    }

    fun createUsuario(token: String, body: CreateUsuarioRequest) {
        viewModelScope.launch {
            try {
                val response = ApiClient.instance.createUsuario("Bearer $token", body)
                if (response.isSuccessful) {
                    _mensaje.value = "Usuario creado correctamente"
                    getUsuarios(token)
                } else {
                    _error.value = obtenerMensajeError(response, "Error al crear usuario")
                }
            } catch (e: Exception) {
                _error.value = "Error de red: ${e.localizedMessage ?: "Sin conexión"}"
            }
        }
    }

    fun updateUsuario(token: String, id: Int, body: UpdateUsuarioRequest) {
        viewModelScope.launch {
            try {
                val response = ApiClient.instance.updateUsuario("Bearer $token", id, body)
                if (response.isSuccessful) {
                    _mensaje.value = "Usuario actualizado correctamente"
                    getUsuarios(token)
                } else {
                    _error.value = obtenerMensajeError(response, "Error al actualizar usuario")
                }
            } catch (e: Exception) {
                _error.value = "Error de red: ${e.localizedMessage ?: "Sin conexión"}"
            }
        }
    }

    fun deleteUsuario(token: String, id: Int) {
        viewModelScope.launch {
            try {
                val response = ApiClient.instance.deleteUsuario("Bearer $token", id)
                if (response.isSuccessful) {
                    _mensaje.value = "Usuario eliminado correctamente"
                    getUsuarios(token)
                } else {
                    _error.value = obtenerMensajeError(response, "Error al eliminar usuario")
                }
            } catch (e: Exception) {
                _error.value = "Error de red: ${e.localizedMessage ?: "Sin conexión"}"
            }
        }
    }

    fun cambiarEstado(token: String, id: Int) {
        viewModelScope.launch {
            try {
                val response = ApiClient.instance.cambiarEstado("Bearer $token", id)
                if (response.isSuccessful) {
                    _mensaje.value = response.body()?.message ?: "Estado actualizado"
                    getUsuarios(token)
                } else {
                    _error.value = obtenerMensajeError(response, "Error al cambiar estado")
                }
            } catch (e: Exception) {
                _error.value = "Error de red: ${e.localizedMessage ?: "Sin conexión"}"
            }
        }
    }
}