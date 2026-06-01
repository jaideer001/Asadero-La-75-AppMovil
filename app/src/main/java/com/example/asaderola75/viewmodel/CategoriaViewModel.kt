package com.example.asaderola75.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.asaderola75.api.ApiClient
import com.example.asaderola75.api.CreateCategoriaRequest
import com.example.asaderola75.api.UpdateCategoriaRequest
import com.example.asaderola75.models.Categoria
import kotlinx.coroutines.launch

class CategoriaViewModel : ViewModel() {
    private val _categorias = MutableLiveData<List<Categoria>>()
    val categorias: LiveData<List<Categoria>> = _categorias

    private val _mensaje = MutableLiveData<String>()
    val mensaje: LiveData<String> = _mensaje

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun getCategorias(token: String) {
        viewModelScope.launch {
            try {
                val response = ApiClient.instance.getCategorias("Bearer $token")
                if (response.isSuccessful) {
                    _categorias.value = response.body()?.data
                } else {
                    _error.value = "Error: ${response.code()}"
                }
            } catch (e: Exception) {
                _error.value = e.message ?: e.toString()
            }
        }
    }

    fun createCategoria(token: String, body: CreateCategoriaRequest) {
        viewModelScope.launch {
            try {
                val response = ApiClient.instance.createCategoria("Bearer $token", body)
                if (response.isSuccessful) {
                    _mensaje.value = "Categoría creada correctamente"
                    getCategorias(token)
                } else {
                    _error.value = "Error al crear categoría"
                }
            } catch (e: Exception) {
                _error.value = e.message ?: e.toString()
            }
        }
    }

    fun updateCategoria(token: String, id: Int, body: UpdateCategoriaRequest) {
        viewModelScope.launch {
            try {
                val response = ApiClient.instance.updateCategoria("Bearer $token", id, body)
                if (response.isSuccessful) {
                    _mensaje.value = "Categoría actualizada correctamente"
                    getCategorias(token)
                } else {
                    _error.value = "Error al actualizar"
                }
            } catch (e: Exception) {
                _error.value = e.message ?: e.toString()
            }
        }
    }

    fun deleteCategoria(token: String, id: Int) {
        viewModelScope.launch {
            try {
                val response = ApiClient.instance.deleteCategoria("Bearer $token", id)
                if (response.isSuccessful) {
                    _mensaje.value = "Categoría eliminada correctamente"
                    getCategorias(token)
                } else {
                    _error.value = "No se puede eliminar, está asociada a productos"
                }
            } catch (e: Exception) {
                _error.value = e.message ?: e.toString()
            }
        }
    }

    fun toggleEstado(token: String, id: Int) {
        viewModelScope.launch {
            try {
                val response = ApiClient.instance.toggleEstadoCategoria("Bearer $token", id)
                if (response.isSuccessful) {
                    _mensaje.value = response.body()?.message ?: "Estado actualizado"
                    getCategorias(token)
                } else {
                    _error.value = "Error al cambiar estado"
                }
            } catch (e: Exception) {
                _error.value = e.message ?: e.toString()
            }
        }
    }
}