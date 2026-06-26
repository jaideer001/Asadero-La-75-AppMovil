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

class VentaViewModel : ViewModel() {
    private val _ventas = MutableLiveData<List<Venta>>()
    val ventas: LiveData<List<Venta>> = _ventas

    private val _ventaDetalle = MutableLiveData<VentaDetalle>()
    val ventaDetalle: LiveData<VentaDetalle> = _ventaDetalle

    private val _mensaje = MutableLiveData<String>()
    val mensaje: LiveData<String> = _mensaje

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun getVentas(token: String) {
        viewModelScope.launch {
            try {
                val response = ApiClient.instance.getVentas("Bearer $token")
                if (response.isSuccessful) {
                    _ventas.value = response.body()?.data
                } else {
                    _error.value = "Error: ${response.code()}"
                }
            } catch (e: Exception) {
                _error.value = e.message ?: e.toString()
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
                    _error.value = "Error: ${response.code()}"
                }
            } catch (e: Exception) {
                _error.value = e.message ?: e.toString()
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
                    _error.value = "Error al registrar venta"
                }
            } catch (e: Exception) {
                _error.value = e.message ?: e.toString()
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
                    _error.value = "Error al anular venta"
                }
            } catch (e: Exception) {
                _error.value = e.message ?: e.toString()
            }
        }
    }
}