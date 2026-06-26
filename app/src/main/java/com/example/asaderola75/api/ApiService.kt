package com.example.asaderola75.api

import com.example.asaderola75.models.CategoriasResponse
import com.example.asaderola75.models.CompraDetalleResponse
import com.example.asaderola75.models.ComprasResponse
import com.example.asaderola75.models.ProductosResponse
import com.example.asaderola75.models.ProveedoresResponse
import com.example.asaderola75.models.RolesResponse
import com.example.asaderola75.models.UsuariosResponse
import com.example.asaderola75.models.VentaDetalleResponse
import com.example.asaderola75.models.VentasResponse
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

    @GET("categorias")
    suspend fun getCategorias(@Header("Authorization") token: String): Response<CategoriasResponse>

    @POST("categorias")
    suspend fun createCategoria(
        @Header("Authorization") token: String,
        @Body body: CreateCategoriaRequest
    ): Response<MessageResponse>

    @PUT("categorias/{id}")
    suspend fun updateCategoria(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Body body: UpdateCategoriaRequest
    ): Response<MessageResponse>

    @DELETE("categorias/{id}")
    suspend fun deleteCategoria(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<MessageResponse>

    @POST("categorias/{id}/toggle-estado")
    suspend fun toggleEstadoCategoria(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<MessageResponse>

    @GET("proveedores")
    suspend fun getProveedores(@Header("Authorization") token: String): Response<ProveedoresResponse>

    @POST("proveedores")
    suspend fun createProveedor(
        @Header("Authorization") token: String,
        @Body body: CreateProveedorRequest
    ): Response<MessageResponse>

    @PUT("proveedores/{id}")
    suspend fun updateProveedor(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Body body: UpdateProveedorRequest
    ): Response<MessageResponse>

    @DELETE("proveedores/{id}")
    suspend fun deleteProveedor(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<MessageResponse>

    @POST("proveedores/{id}/toggle-estado")
    suspend fun toggleEstadoProveedor(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<MessageResponse>

    @GET("productos")
    suspend fun getProductos(@Header("Authorization") token: String): Response<ProductosResponse>

    @POST("productos")
    suspend fun createProducto(
        @Header("Authorization") token: String,
        @Body body: CreateProductoRequest
    ): Response<MessageResponse>

    @PUT("productos/{id}")
    suspend fun updateProducto(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Body body: UpdateProductoRequest
    ): Response<MessageResponse>

    @DELETE("productos/{id}")
    suspend fun deleteProducto(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<MessageResponse>

    @POST("productos/{id}/toggle-estado")
    suspend fun toggleEstadoProducto(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<MessageResponse>

    @GET("ventas")
    suspend fun getVentas(@Header("Authorization") token: String): Response<VentasResponse>

    @GET("ventas/{id}")
    suspend fun getVenta(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<VentaDetalleResponse>

    @POST("ventas")
    suspend fun createVenta(
        @Header("Authorization") token: String,
        @Body body: CreateVentaRequest
    ): Response<MessageResponse>

    @DELETE("ventas/{id}")
    suspend fun anularVenta(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<MessageResponse>

    @GET("compras")
    suspend fun getCompras(@Header("Authorization") token: String): Response<ComprasResponse>

    @GET("compras/{id}")
    suspend fun getCompra(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<CompraDetalleResponse>

    @POST("compras")
    suspend fun createCompra(
        @Header("Authorization") token: String,
        @Body body: CreateCompraRequest
    ): Response<MessageResponse>

    @DELETE("compras/{id}")
    suspend fun anularCompra(
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
    val message: String,
    val user: UserLogin?
)

data class UserLogin(
    val id: Int,
    val nombre: String,
    val correo: String,
    val rol: Int
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

data class CreateCategoriaRequest(
    val nombre: String,
    val descripcion: String?,
    val status: Int
)

data class UpdateCategoriaRequest(
    val nombre: String,
    val descripcion: String?,
    val status: Int
)

data class CreateProveedorRequest(
    val nombre: String,
    val telefono: String?,
    val direccion: String?,
    val correo: String?,
    val status: Int
)

data class UpdateProveedorRequest(
    val nombre: String,
    val telefono: String?,
    val direccion: String?,
    val correo: String?,
    val status: Int
)

data class CreateProductoRequest(
    val nombre: String,
    val descripcion: String?,
    val stock_actual: Double,
    val unidad_medida: String,
    val precio_compra: Double?,
    val precio_venta: Double?,
    val tipo: String,
    val status: Int,
    val id_categoria: Int
)

data class UpdateProductoRequest(
    val nombre: String,
    val descripcion: String?,
    val stock_actual: Double,
    val unidad_medida: String,
    val precio_compra: Double?,
    val precio_venta: Double?,
    val tipo: String,
    val status: Int,
    val id_categoria: Int
)

data class CreateVentaRequest(
    val productos: List<ProductoVentaItem>
)

data class ProductoVentaItem(
    val id_producto: Int,
    val cantidad: Int
)

data class CreateCompraRequest(
    val id_proveedor: Int,
    val fecha: String,
    val total_compra: Double,
    val productos: List<ProductoCompraItem>
)

data class ProductoCompraItem(
    val id_producto: Int,
    val cantidad: Int,
    val precio_unitario: Double
)

data class MessageResponse(
    val success: Boolean,
    val message: String
)