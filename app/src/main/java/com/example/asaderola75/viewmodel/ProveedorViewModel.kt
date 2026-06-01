package com.example.asaderola75.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.asaderola75.api.ApiClient
import com.example.asaderola75.api.CreateProveedorRequest
import com.example.asaderola75.api.UpdateProveedorRequest
import com.example.asaderola75.models.Proveedor
import kotlinx.coroutines.launch

class ProveedorViewModel : ViewModel() {
    private val _proveedores = MutableLiveData<List<Proveedor>>()
    val proveedores: LiveData<List<Proveedor>> = _proveedores

    private val _mensaje = MutableLiveData<String>()
    val mensaje: LiveData<String> = _mensaje

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun getProveedores(token: String) {
        viewModelScope.launch {
            try {
                val response = ApiClient.instance.getProveedores("Bearer $token")
                if (response.isSuccessful) {
                    _proveedores.value = response.body()?.data
                } else {
                    _error.value = "Error: ${response.code()}"
                }
            } catch (e: Exception) {
                _error.value = e.message ?: e.toString()
            }
        }
    }

    fun createProveedor(token: String, body: CreateProveedorRequest) {
        viewModelScope.launch {
            try {
                val response = ApiClient.instance.createProveedor("Bearer $token", body)
                if (response.isSuccessful) {
                    _mensaje.value = "Proveedor creado correctamente"
                    getProveedores(token)
                } else {
                    _error.value = "Error al crear proveedor"
                }
            } catch (e: Exception) {
                _error.value = e.message ?: e.toString()
            }
        }
    }

    fun updateProveedor(token: String, id: Int, body: UpdateProveedorRequest) {
        viewModelScope.launch {
            try {
                val response = ApiClient.instance.updateProveedor("Bearer $token", id, body)
                if (response.isSuccessful) {
                    _mensaje.value = "Proveedor actualizado correctamente"
                    getProveedores(token)
                } else {
                    _error.value = "Error al actualizar"
                }
            } catch (e: Exception) {
                _error.value = e.message ?: e.toString()
            }
        }
    }

    fun deleteProveedor(token: String, id: Int) {
        viewModelScope.launch {
            try {
                val response = ApiClient.instance.deleteProveedor("Bearer $token", id)
                if (response.isSuccessful) {
                    _mensaje.value = "Proveedor eliminado correctamente"
                    getProveedores(token)
                } else {
                    _error.value = "No se puede eliminar, está asociado a productos"
                }
            } catch (e: Exception) {
                _error.value = e.message ?: e.toString()
            }
        }
    }

    fun toggleEstado(token: String, id: Int) {
        viewModelScope.launch {
            try {
                val response = ApiClient.instance.toggleEstadoProveedor("Bearer $token", id)
                if (response.isSuccessful) {
                    _mensaje.value = response.body()?.message ?: "Estado actualizado"
                    getProveedores(token)
                } else {
                    _error.value = "Error al cambiar estado"
                }
            } catch (e: Exception) {
                _error.value = e.message ?: e.toString()
            }
        }
    }
}