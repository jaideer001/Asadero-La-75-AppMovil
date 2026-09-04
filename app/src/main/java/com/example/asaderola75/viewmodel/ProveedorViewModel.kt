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
import org.json.JSONObject
import retrofit2.Response

class ProveedorViewModel : ViewModel() {
    private val _proveedores = MutableLiveData<List<Proveedor>>()
    val proveedores: LiveData<List<Proveedor>> = _proveedores

    private val _mensaje = MutableLiveData<String>()
    val mensaje: LiveData<String> = _mensaje

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    // Extrae los mensajes de error de validación de Laravel (FormRequest) o la respuesta directa
    private fun <T> obtenerMensajeError(response: Response<T>, mensajePorDefecto: String): String {
        return try {
            val errorBody = response.errorBody()?.string()
            if (!errorBody.isNullOrEmpty()) {
                val jsonObject = JSONObject(errorBody)

                when {
                    // Si Laravel devuelve errores de validación en FormRequest (HTTP 422)
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

    fun getProveedores(token: String) {
        viewModelScope.launch {
            try {
                val response = ApiClient.instance.getProveedores("Bearer $token")
                if (response.isSuccessful) {
                    _proveedores.value = response.body()?.data
                } else {
                    _error.value = obtenerMensajeError(response, "Error al obtener proveedores")
                }
            } catch (e: Exception) {
                _error.value = "Error de red: ${e.localizedMessage ?: "Sin conexión"}"
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
                    _error.value = obtenerMensajeError(response, "Error al crear proveedor")
                }
            } catch (e: Exception) {
                _error.value = "Error de red: ${e.localizedMessage ?: "Sin conexión"}"
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
                    _error.value = obtenerMensajeError(response, "Error al actualizar proveedor")
                }
            } catch (e: Exception) {
                _error.value = "Error de red: ${e.localizedMessage ?: "Sin conexión"}"
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
                    _error.value = obtenerMensajeError(response, "No se puede eliminar el proveedor")
                }
            } catch (e: Exception) {
                _error.value = "Error de red: ${e.localizedMessage ?: "Sin conexión"}"
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
                    _error.value = obtenerMensajeError(response, "Error al cambiar estado")
                }
            } catch (e: Exception) {
                _error.value = "Error de red: ${e.localizedMessage ?: "Sin conexión"}"
            }
        }
    }
}