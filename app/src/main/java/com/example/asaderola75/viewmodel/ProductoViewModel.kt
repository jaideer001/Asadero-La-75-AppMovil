package com.example.asaderola75.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.asaderola75.api.ApiClient
import com.example.asaderola75.api.CreateProductoRequest
import com.example.asaderola75.api.UpdateProductoRequest
import com.example.asaderola75.models.Producto
import kotlinx.coroutines.launch

class ProductoViewModel : ViewModel() {
    private val _productos = MutableLiveData<List<Producto>>()
    val productos: LiveData<List<Producto>> = _productos

    private val _mensaje = MutableLiveData<String>()
    val mensaje: LiveData<String> = _mensaje

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun getProductos(token: String) {
        viewModelScope.launch {
            try {
                val response = ApiClient.instance.getProductos("Bearer $token")
                if (response.isSuccessful) {
                    _productos.value = response.body()?.data
                } else {
                    _error.value = "Error: ${response.code()}"
                }
            } catch (e: Exception) {
                _error.value = e.message ?: e.toString()
            }
        }
    }

    fun createProducto(token: String, body: CreateProductoRequest) {
        viewModelScope.launch {
            try {
                val response = ApiClient.instance.createProducto("Bearer $token", body)
                if (response.isSuccessful) {
                    _mensaje.value = "Producto creado correctamente"
                    getProductos(token)
                } else {
                    _error.value = "Error al crear producto"
                }
            } catch (e: Exception) {
                _error.value = e.message ?: e.toString()
            }
        }
    }

    fun updateProducto(token: String, id: Int, body: UpdateProductoRequest) {
        viewModelScope.launch {
            try {
                val response = ApiClient.instance.updateProducto("Bearer $token", id, body)
                if (response.isSuccessful) {
                    _mensaje.value = "Producto actualizado correctamente"
                    getProductos(token)
                } else {
                    _error.value = "Error al actualizar"
                }
            } catch (e: Exception) {
                _error.value = e.message ?: e.toString()
            }
        }
    }

    fun deleteProducto(token: String, id: Int) {
        viewModelScope.launch {
            try {
                val response = ApiClient.instance.deleteProducto("Bearer $token", id)
                if (response.isSuccessful) {
                    _mensaje.value = "Producto eliminado correctamente"
                    getProductos(token)
                } else {
                    _error.value = "No se puede eliminar el producto"
                }
            } catch (e: Exception) {
                _error.value = e.message ?: e.toString()
            }
        }
    }

    fun toggleEstado(token: String, id: Int) {
        viewModelScope.launch {
            try {
                val response = ApiClient.instance.toggleEstadoProducto("Bearer $token", id)
                if (response.isSuccessful) {
                    _mensaje.value = response.body()?.message ?: "Estado actualizado"
                    getProductos(token)
                } else {
                    _error.value = "Error al cambiar estado"
                }
            } catch (e: Exception) {
                _error.value = e.message ?: e.toString()
            }
        }
    }
}