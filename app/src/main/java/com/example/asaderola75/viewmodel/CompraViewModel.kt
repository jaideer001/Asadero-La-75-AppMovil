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

class CompraViewModel : ViewModel() {
    private val _compras = MutableLiveData<List<Compra>>()
    val compras: LiveData<List<Compra>> = _compras

    private val _compraDetalle = MutableLiveData<CompraDetalle>()
    val compraDetalle: LiveData<CompraDetalle> = _compraDetalle

    private val _mensaje = MutableLiveData<String>()
    val mensaje: LiveData<String> = _mensaje

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun getCompras(token: String) {
        viewModelScope.launch {
            try {
                val response = ApiClient.instance.getCompras("Bearer $token")
                if (response.isSuccessful) {
                    _compras.value = response.body()?.data
                } else {
                    _error.value = "Error: ${response.code()}"
                }
            } catch (e: Exception) {
                _error.value = e.message ?: e.toString()
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
                    _error.value = "Error: ${response.code()}"
                }
            } catch (e: Exception) {
                _error.value = e.message ?: e.toString()
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
                    _error.value = "Error al registrar compra"
                }
            } catch (e: Exception) {
                _error.value = e.message ?: e.toString()
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
                    _error.value = "Error al anular compra"
                }
            } catch (e: Exception) {
                _error.value = e.message ?: e.toString()
            }
        }
    }
}