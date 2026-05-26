package com.example.ahorrofamiliar.data.remote

import com.example.ahorrofamiliar.data.model.Meta
import com.example.ahorrofamiliar.data.model.Pago
import com.example.ahorrofamiliar.data.model.Usuario
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Define los endpoints del backend.
 * Retrofit genera la implementacion en tiempo de ejecucion.
 */
interface ApiService {


    @GET("usuarios/dispositivo/{dispositivoId}")
    suspend fun obtenerUsuarioPorDispositivo(
        @Path("dispositivoId")
        dispositivoId: String
    ): Usuario

    @POST("usuarios")
    suspend fun crearUsuario(
        @Body usuario: Usuario
    ): Usuario

    @PUT("usuarios/{id}")
    suspend fun editarUsuario(
        @Path("id") id: Int,
        @Body usuario: Usuario
    ): Usuario
    // GET /metas - lista todas las metas
    @GET("metas")
    suspend fun obtenerMetas(
        @Query("userId") userId: Int
    ): List<Meta>

    // GET /metas/{id} - detalle de una meta
    @GET("metas/{id}")
    suspend fun obtenerMeta(@Path("id") id: Int): Meta

    // GET /metas/{id}/pagos - pagos de una meta
    @GET("metas/{id}/pagos")
    suspend fun obtenerPagos(@Path("id") metaId: Int): List<Pago>

    // POST /pagos - registrar un pago nuevo
    @POST("pagos")
    suspend fun registrarPago(@Body pago: Pago): Pago
}
