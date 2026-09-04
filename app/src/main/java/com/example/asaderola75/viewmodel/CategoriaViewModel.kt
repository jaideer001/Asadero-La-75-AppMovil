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
import org.json.JSONObject
import retrofit2.Response

class CategoriaViewModel : ViewModel() {
    private val _categorias = MutableLiveData<List<Categoria>>()
    val categorias: LiveData<List<Categoria>> = _categorias

    private val _mensaje = MutableLiveData<String>()
    val mensaje: LiveData<String> = _mensaje

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    // Lee los mensajes devueltos por el FormRequest de Laravel o respuestas de error directas
    private fun <T> obtenerMensajeError(response: Response<T>, mensajePorDefecto: String): String {
        return try {
            val errorBody = response.errorBody()?.string()
            if (!errorBody.isNullOrEmpty()) {
                val jsonObject = JSONObject(errorBody)

                when {
                    // Extrae el mensaje específico de validación de Laravel (HTTP 422)
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

    fun getCategorias(token: String) {
        viewModelScope.launch {
            try {
                val response = ApiClient.instance.getCategorias("Bearer $token")
                if (response.isSuccessful) {
                    _categorias.value = response.body()?.data
                } else {
                    _error.value = obtenerMensajeError(response, "Error al obtener categorías")
                }
            } catch (e: Exception) {
                _error.value = "Error de red: ${e.localizedMessage ?: "Sin conexión"}"
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
                    _error.value = obtenerMensajeError(response, "Error al crear categoría")
                }
            } catch (e: Exception) {
                _error.value = "Error de red: ${e.localizedMessage ?: "Sin conexión"}"
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
                    _error.value = obtenerMensajeError(response, "Error al actualizar categoría")
                }
            } catch (e: Exception) {
                _error.value = "Error de red: ${e.localizedMessage ?: "Sin conexión"}"
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
                    _error.value = obtenerMensajeError(response, "No se puede eliminar la categoría")
                }
            } catch (e: Exception) {
                _error.value = "Error de red: ${e.localizedMessage ?: "Sin conexión"}"
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
                    _error.value = obtenerMensajeError(response, "Error al cambiar estado")
                }
            } catch (e: Exception) {
                _error.value = "Error de red: ${e.localizedMessage ?: "Sin conexión"}"
            }
        }
    }
}