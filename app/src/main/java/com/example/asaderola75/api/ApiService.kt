package com.example.asaderola75.api

import com.example.asaderola75.models.RolesResponse
import com.example.asaderola75.models.UsuariosResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {
    @POST("login")
    suspend fun login(@Body body: LoginRequest): Response<LoginResponse>

    @GET("usuarios")
    suspend fun getUsuarios(@Header("Authorization") token: String): Response<UsuariosResponse>

    @POST("usuarios")
    suspend fun createUsuario(
        @Header("Authorization") token: String,
        @Body body: CreateUsuarioRequest
    ): Response<MessageResponse>

    @PUT("usuarios/{id}")
    suspend fun updateUsuario(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Body body: UpdateUsuarioRequest
    ): Response<MessageResponse>

    @DELETE("usuarios/{id}")
    suspend fun deleteUsuario(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<MessageResponse>

    @POST("usuarios/{id}/toggle-estado")
    suspend fun cambiarEstado(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<MessageResponse>

    @GET("roles")
    suspend fun getRoles(@Header("Authorization") token: String): Response<RolesResponse>

    @POST("roles")
    suspend fun createRol(
        @Header("Authorization") token: String,
        @Body body: CreateRolRequest
    ): Response<MessageResponse>

    @PUT("roles/{id}")
    suspend fun updateRol(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Body body: UpdateRolRequest
    ): Response<MessageResponse>

    @DELETE("roles/{id}")
    suspend fun deleteRol(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<MessageResponse>

    @POST("roles/{id}/toggle-estado")
    suspend fun toggleEstadoRol(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<MessageResponse>
}

data class LoginRequest(
    val login: String,
    val password: String
)

data class LoginResponse(
    val access_token: String,
    val message: String
)

data class CreateUsuarioRequest(
    val nombre: String,
    val usuario: String,
    val correo: String,
    val contrasena: String,
    val id_rol: Int,
    val estado: Int
)

data class UpdateUsuarioRequest(
    val nombre: String,
    val usuario: String,
    val correo: String,
    val contrasena: String?,
    val id_rol: Int,
    val estado: Int
)

data class CreateRolRequest(
    val nombre: String,
    val descripcion: String?,
    val status: Int
)

data class UpdateRolRequest(
    val nombre: String,
    val descripcion: String?,
    val status: Int
)

data class MessageResponse(
    val success: Boolean,
    val message: String
)