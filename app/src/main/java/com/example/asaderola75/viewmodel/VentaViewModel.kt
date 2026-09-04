package com.example.asaderola75.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.asaderola75.api.ApiClient
import com.example.asaderola75.api.CreateVentaRequest
import com.example.asaderola75.models.Venta
import com.example.asaderola75.models.VentaDetalle
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Response

class VentaViewModel : ViewModel() {
    private val _ventas = MutableLiveData<List<Venta>>()
    val ventas: LiveData<List<Venta>> = _ventas

    private val _ventaDetalle = MutableLiveData<VentaDetalle>()
    val ventaDetalle: LiveData<VentaDetalle> = _ventaDetalle

    private val _mensaje = MutableLiveData<String>()
    val mensaje: LiveData<String> = _mensaje

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    // Método para parsear errores de validación de Laravel (VentaRequest) o excepciones personalizadas
    private fun <T> obtenerMensajeError(response: Response<T>, mensajePorDefecto: String): String {
        return try {
            val errorBody = response.errorBody()?.string()
            if (!errorBody.isNullOrEmpty()) {
                val jsonObject = JSONObject(errorBody)

                when {
                    // Mapea errores de validación de VentaRequest (e.g. productos duplicados, sin stock, etc.)
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

    fun getVentas(token: String) {
        viewModelScope.launch {
            try {
                val response = ApiClient.instance.getVentas("Bearer $token")
                if (response.isSuccessful) {
                    _ventas.value = response.body()?.data
                } else {
                    _error.value = obtenerMensajeError(response, "Error al obtener las ventas")
                }
            } catch (e: Exception) {
                _error.value = "Error de red: ${e.localizedMessage ?: "Sin conexión"}"
            }
        }
    }

    fun getVentaDetalle(token: String, id: Int) {
        viewModelScope.launch {
            try {
                val response = ApiClient.instance.getVenta("Bearer $token", id)
                if (response.isSuccessful) {
                    _ventaDetalle.value = response.body()?.data
                } else {
                    _error.value = obtenerMensajeError(response, "Error al obtener el detalle de la venta")
                }
            } catch (e: Exception) {
                _error.value = "Error de red: ${e.localizedMessage ?: "Sin conexión"}"
            }
        }
    }

    fun createVenta(token: String, body: CreateVentaRequest) {
        viewModelScope.launch {
            try {
                val response = ApiClient.instance.createVenta("Bearer $token", body)
                if (response.isSuccessful) {
                    _mensaje.value = "Venta registrada correctamente"
                    getVentas(token)
                } else {
                    _error.value = obtenerMensajeError(response, "Error al registrar la venta")
                }
            } catch (e: Exception) {
                _error.value = "Error de red: ${e.localizedMessage ?: "Sin conexión"}"
            }
        }
    }

    fun anularVenta(token: String, id: Int) {
        viewModelScope.launch {
            try {
                val response = ApiClient.instance.anularVenta("Bearer $token", id)
                if (response.isSuccessful) {
                    _mensaje.value = "Venta anulada correctamente"
                    getVentas(token)
                } else {
                    _error.value = obtenerMensajeError(response, "Error al anular la venta")
                }
            } catch (e: Exception) {
                _error.value = "Error de red: ${e.localizedMessage ?: "Sin conexión"}"
            }
        }
    }
}