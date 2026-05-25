package com.example.ahorrofamiliar.data.remote

import com.example.ahorrofamiliar.data.model.Meta
import com.example.ahorrofamiliar.data.model.Pago
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * Define los endpoints del backend.
 * Retrofit genera la implementacion en tiempo de ejecucion.
 */
interface ApiService {

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
}
