package com.example.ahorrofamiliar.data.remote

import com.example.ahorrofamiliar.data.model.Meta
import com.example.ahorrofamiliar.data.model.Miembro
import com.example.ahorrofamiliar.data.model.Pago
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
interface ApiService {/*

    // GET /metas - lista todas las metas
    @GET("metas")
    suspend fun obtenerMetas(): List<Meta>

    // GET /metas/{id} - detalle de una meta
    @GET("metas/{id}")
    suspend fun obtenerMeta(@Path("id") id: Int): Meta

    // GET /metas/{id}/pagos - pagos de una meta
    @GET("metas/{id}/pagos")
    suspend fun obtenerPagos(@Path("id") metaId: Int): List<Pago>

    // POST /pagos - registrar un pago nuevo
    @POST("pagos")
    suspend fun registrarPago(@Body pago: Pago): Pago


    */

    @GET("usuarios")
    suspend fun obtenerUsuarios(): List<Miembro>

    // GET /usuarios/{id} - detalle de un usuario buscado por su dispositivo
    @GET("usuarios/dispositivo/{idDispositivo}")
    suspend fun obtenerUsuarioPorDispositivo(
        @Path("idDispositivo")
        idDispositivo: String
    ): Miembro

    // POST /usuarios - crear un usuario
    @POST("usuarios")
    suspend fun crearUsuario(
        @Body usuario: Miembro
    ): Miembro

    // PUT /usuarios/{id} - editar un usuario
    @PUT("usuarios/{id}")
    suspend fun editarUsuario(
        @Path("id") id: Int,
        @Body usuario: Miembro
    ): Miembro

    // POST /usuarios/{id}/amigos - agregar un amigo para el usuario de con id
    @POST("usuarios/{id}/amigos")
    suspend fun agregarAmigo(
        @Path("id") id: Int, //params
        @Body body: Map<String, Int>
    ): Miembro

    // GET /metas - lista todas las metas
    @GET("metas")
    suspend fun obtenerMetas(
        @Query("userId") userId: Int
    ): List<Meta>

    // POST /metas - crear una meta
    @POST("metas")
    suspend fun crearMeta(
        @Body meta: Meta
    ): Meta

    // GET /metas/{id} - detalle de una meta
    @GET("metas/{id}")
    suspend fun obtenerMeta(
        @Path("id") id: Int,
        @Query("idPrincipal") userId: Int
    ): Meta

    // POST /metas/{id}/miembros - agregar miembro
    @POST("metas/{id}/miembros")
    suspend fun agregarMiembro(
        @Path("id") metaId: Int,
        @Body body: Map<String, Int>
    ): Map<String, String>

    // GET /metas/{id}/pagos - pagos de una meta
    @GET("metas/{id}/pagos")
    suspend fun obtenerPagos(@Path("id") metaId: Int): List<Pago>

    // POST /pagos - registrar un pago nuevo
    @POST("pagos")
    suspend fun registrarPago(@Body pago: Pago): Pago
}
