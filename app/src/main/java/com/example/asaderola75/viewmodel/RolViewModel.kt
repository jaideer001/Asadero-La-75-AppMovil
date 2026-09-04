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
import org.json.JSONObject
import retrofit2.Response

class RolViewModel : ViewModel() {
    private val _roles = MutableLiveData<List<Rol>>()
    val roles: LiveData<List<Rol>> = _roles

    private val _mensaje = MutableLiveData<String>()
    val mensaje: LiveData<String> = _mensaje

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    // Extrae los errores de validación de Laravel (FormRequest) o el mensaje directo
    private fun <T> obtenerMensajeError(response: Response<T>, mensajePorDefecto: String): String {
        return try {
            val errorBody = response.errorBody()?.string()
            if (!errorBody.isNullOrEmpty()) {
                val jsonObject = JSONObject(errorBody)

                when {
                    // Si Laravel responde con errores de validación (FormRequest -> 422 Unprocessable Entity)
                    jsonObject.has("errors") -> {
                        val errorsObj = jsonObject.getJSONObject("errors")
                        val primerCampo = errorsObj.keys().next()
                        val listaErrores = errorsObj.getJSONArray(primerCampo)
                        listaErrores.getString(0)
                    }
                    // Si devuelve un mensaje directo en la raíz del JSON
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

    fun getRoles(token: String) {
        viewModelScope.launch {
            try {
                val response = ApiClient.instance.getRoles("Bearer $token")
                if (response.isSuccessful) {
                    _roles.value = response.body()?.data
                } else {
                    _error.value = obtenerMensajeError(response, "Error al obtener roles")
                }
            } catch (e: Exception) {
                _error.value = "Error de red: ${e.localizedMessage ?: "Sin conexión"}"
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
                    _error.value = obtenerMensajeError(response, "Error al crear rol")
                }
            } catch (e: Exception) {
                _error.value = "Error de red: ${e.localizedMessage ?: "Sin conexión"}"
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
                    _error.value = obtenerMensajeError(response, "Error al actualizar rol")
                }
            } catch (e: Exception) {
                _error.value = "Error de red: ${e.localizedMessage ?: "Sin conexión"}"
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
                    _error.value = obtenerMensajeError(response, "No se puede eliminar el rol")
                }
            } catch (e: Exception) {
                _error.value = "Error de red: ${e.localizedMessage ?: "Sin conexión"}"
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
                    _error.value = obtenerMensajeError(response, "Error al cambiar estado")
                }
            } catch (e: Exception) {
                _error.value = "Error de red: ${e.localizedMessage ?: "Sin conexión"}"
            }
        }
    }
}