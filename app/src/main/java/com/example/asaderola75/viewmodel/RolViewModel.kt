package com.example.asaderola75.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.asaderola75.api.ApiClient
import com.example.asaderola75.api.CreateRolRequest
import com.example.asaderola75.api.UpdateRolRequest
import com.example.asaderola75.models.Rol
import kotlinx.coroutines.launch

class RolViewModel : ViewModel() {
    private val _roles = MutableLiveData<List<Rol>>()
    val roles: LiveData<List<Rol>> = _roles

    private val _mensaje = MutableLiveData<String>()
    val mensaje: LiveData<String> = _mensaje

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun getRoles(token: String) {
        viewModelScope.launch {
            try {
                val response = ApiClient.instance.getRoles("Bearer $token")
                if (response.isSuccessful) {
                    _roles.value = response.body()?.data
                } else {
                    _error.value = "Error: ${response.code()}"
                }
            } catch (e: Exception) {
                _error.value = "Sin conexión"
            }
        }
    }

    fun createRol(token: String, body: CreateRolRequest) {
        viewModelScope.launch {
            try {
                val response = ApiClient.instance.createRol("Bearer $token", body)
                if (response.isSuccessful) {
                    _mensaje.value = "Rol creado correctamente"
                    getRoles(token)
                } else {
                    _error.value = "Error al crear rol"
                }
            } catch (e: Exception) {
                _error.value = "Sin conexión"
            }
        }
    }

    fun updateRol(token: String, id: Int, body: UpdateRolRequest) {
        viewModelScope.launch {
            try {
                val response = ApiClient.instance.updateRol("Bearer $token", id, body)
                if (response.isSuccessful) {
                    _mensaje.value = "Rol actualizado correctamente"
                    getRoles(token)
                } else {
                    _error.value = "Error al actualizar"
                }
            } catch (e: Exception) {
                _error.value = "Sin conexión"
            }
        }
    }

    fun deleteRol(token: String, id: Int) {
        viewModelScope.launch {
            try {
                val response = ApiClient.instance.deleteRol("Bearer $token", id)
                if (response.isSuccessful) {
                    _mensaje.value = "Rol eliminado correctamente"
                    getRoles(token)
                } else {
                    _error.value = "No se puede eliminar, está asociado a usuarios"
                }
            } catch (e: Exception) {
                _error.value = "Sin conexión"
            }
        }
    }

    fun toggleEstado(token: String, id: Int) {
        viewModelScope.launch {
            try {
                val response = ApiClient.instance.toggleEstadoRol("Bearer $token", id)
                if (response.isSuccessful) {
                    _mensaje.value = response.body()?.message ?: "Estado actualizado"
                    getRoles(token)
                } else {
                    _error.value = "Error al cambiar estado"
                }
            } catch (e: Exception) {
                _error.value = "Sin conexión"
            }
        }
    }
}