package com.example.asaderola75.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.asaderola75.api.ApiClient
import com.example.asaderola75.api.CreateCompraRequest
import com.example.asaderola75.models.Compra
import com.example.asaderola75.models.CompraDetalle
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Response

class CompraViewModel : ViewModel() {
    private val _compras = MutableLiveData<List<Compra>>()
    val compras: LiveData<List<Compra>> = _compras

    private val _compraDetalle = MutableLiveData<CompraDetalle>()
    val compraDetalle: LiveData<CompraDetalle> = _compraDetalle

    private val _mensaje = MutableLiveData<String>()
    val mensaje: LiveData<String> = _mensaje

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    // Método para mapear errores de validación (CompraRequest HTTP 422) o excepciones directas
    private fun <T> obtenerMensajeError(response: Response<T>, mensajePorDefecto: String): String {
        return try {
            val errorBody = response.errorBody()?.string()
            if (!errorBody.isNullOrEmpty()) {
                val jsonObject = JSONObject(errorBody)

                when {
                    // Parsea los errores devueltos por la función withValidator / rules de Laravel
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

    fun getCompras(token: String) {
        viewModelScope.launch {
            try {
                val response = ApiClient.instance.getCompras("Bearer $token")
                if (response.isSuccessful) {
                    _compras.value = response.body()?.data
                } else {
                    _error.value = obtenerMensajeError(response, "Error al obtener compras")
                }
            } catch (e: Exception) {
                _error.value = "Error de red: ${e.localizedMessage ?: "Sin conexión"}"
            }
        }
    }

    fun getCompraDetalle(token: String, id: Int) {
        viewModelScope.launch {
            try {
                val response = ApiClient.instance.getCompra("Bearer $token", id)
                if (response.isSuccessful) {
                    _compraDetalle.value = response.body()?.data
                } else {
                    _error.value = obtenerMensajeError(response, "Error al obtener detalle de la compra")
                }
            } catch (e: Exception) {
                _error.value = "Error de red: ${e.localizedMessage ?: "Sin conexión"}"
            }
        }
    }

    fun createCompra(token: String, body: CreateCompraRequest) {
        viewModelScope.launch {
            try {
                val response = ApiClient.instance.createCompra("Bearer $token", body)
                if (response.isSuccessful) {
                    _mensaje.value = "Compra registrada correctamente"
                    getCompras(token)
                } else {
                    _error.value = obtenerMensajeError(response, "Error al registrar compra")
                }
            } catch (e: Exception) {
                _error.value = "Error de red: ${e.localizedMessage ?: "Sin conexión"}"
            }
        }
    }

    fun anularCompra(token: String, id: Int) {
        viewModelScope.launch {
            try {
                val response = ApiClient.instance.anularCompra("Bearer $token", id)
                if (response.isSuccessful) {
                    _mensaje.value = "Compra anulada correctamente"
                    getCompras(token)
                } else {
                    _error.value = obtenerMensajeError(response, "Error al anular compra")
                }
            } catch (e: Exception) {
                _error.value = "Error de red: ${e.localizedMessage ?: "Sin conexión"}"
            }
        }
    }
}