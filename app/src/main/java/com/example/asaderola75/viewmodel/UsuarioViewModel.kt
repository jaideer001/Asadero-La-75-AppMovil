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

class UsuarioViewModel : ViewModel() {
    private val _usuarios = MutableLiveData<List<Usuario>>()
    val usuarios: LiveData<List<Usuario>> = _usuarios

    private val _mensaje = MutableLiveData<String>()
    val mensaje: LiveData<String> = _mensaje

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun getUsuarios(token: String) {
        viewModelScope.launch {
            try {
                val response = ApiClient.instance.getUsuarios("Bearer $token")
                if (response.isSuccessful) {
                    _usuarios.value = response.body()?.data
                } else {
                    _error.value = "Error: ${response.code()}"
                }
            } catch (e: Exception) {
                _error.value = "Sin conexión"
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
                    _error.value = "Error al crear usuario"
                }
            } catch (e: Exception) {
                _error.value = "Sin conexión"
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
                    _error.value = "Error al actualizar"
                }
            } catch (e: Exception) {
                _error.value = "Sin conexión"
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
                    _error.value = "Error al eliminar"
                }
            } catch (e: Exception) {
                _error.value = "Sin conexión"
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
                    _error.value = "Error al cambiar estado"
                }
            } catch (e: Exception) {
                _error.value = "Sin conexión"
            }
        }
    }
}