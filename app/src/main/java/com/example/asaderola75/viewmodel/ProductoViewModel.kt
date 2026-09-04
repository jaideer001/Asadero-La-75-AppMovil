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
import org.json.JSONObject
import retrofit2.Response

class ProductoViewModel : ViewModel() {
    private val _productos = MutableLiveData<List<Producto>>()
    val productos: LiveData<List<Producto>> = _productos

    private val _mensaje = MutableLiveData<String>()
    val mensaje: LiveData<String> = _mensaje

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    // Método para mapear errores de validación (ProductoRequest HTTP 422) o mensajes directos
    private fun <T> obtenerMensajeError(response: Response<T>, mensajePorDefecto: String): String {
        return try {
            val errorBody = response.errorBody()?.string()
            if (!errorBody.isNullOrEmpty()) {
                val jsonObject = JSONObject(errorBody)

                when {
                    // Mapea el mapa de errores enviado por el FormRequest de Laravel
                    jsonObject.has("errors") -> {
                        val errorsObj = jsonObject.getJSONObject("errors")
                        val primerCampo = errorsObj.keys().next()
                        val listaErrores = errorsObj.getJSONArray(primerCampo)
                        listaErrores.getString(0)
                    }
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

    fun getProductos(token: String) {
        viewModelScope.launch {
            try {
                val response = ApiClient.instance.getProductos("Bearer $token")
                if (response.isSuccessful) {
                    _productos.value = response.body()?.data
                } else {
                    _error.value = obtenerMensajeError(response, "Error al obtener productos")
                }
            } catch (e: Exception) {
                _error.value = "Error de red: ${e.localizedMessage ?: "Sin conexión"}"
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
                    _error.value = obtenerMensajeError(response, "Error al crear producto")
                }
            } catch (e: Exception) {
                _error.value = "Error de red: ${e.localizedMessage ?: "Sin conexión"}"
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
                    _error.value = obtenerMensajeError(response, "Error al actualizar producto")
                }
            } catch (e: Exception) {
                _error.value = "Error de red: ${e.localizedMessage ?: "Sin conexión"}"
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
                    _error.value = obtenerMensajeError(response, "No se puede eliminar el producto")
                }
            } catch (e: Exception) {
                _error.value = "Error de red: ${e.localizedMessage ?: "Sin conexión"}"
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
                    _error.value = obtenerMensajeError(response, "Error al cambiar estado")
                }
            } catch (e: Exception) {
                _error.value = "Error de red: ${e.localizedMessage ?: "Sin conexión"}"
            }
        }
    }
}