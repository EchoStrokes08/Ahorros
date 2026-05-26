package com.example.ahorrofamiliar.data.repository

import com.example.ahorrofamiliar.data.model.Meta
import com.example.ahorrofamiliar.data.model.Pago
import com.example.ahorrofamiliar.data.remote.ApiService

/**
 * Repository: capa intermedia entre los ViewModel de crear, listar, detallar y pagos de las metas y el ApiService.
 *
 * Por que existe?
 * - Para que el ViewModel no conozca a Retrofit directamente.
 * - Para poder cambiar la fuente de datos (API, base local, etc.) sin tocar el ViewModel.
 * - Para poder pasarle un ApiService falso en las pruebas unitarias.
 */
class MetaRepository(private val api: ApiService) {

    suspend fun obtenerMetas(userId: Int): List<Meta> = api.obtenerMetas(userId)

    suspend fun crearMeta(meta: Meta): Meta = api.crearMeta(meta)

    suspend fun obtenerMeta(id: Int, userId: Int): Meta = api.obtenerMeta(id, userId)

    suspend fun agregarMiembro(metaId: Int, idUsuario: Int, idSolicitante: Int) {
        api.agregarMiembro(
            metaId,
            mapOf(
                "idUsuario" to idUsuario,
                "idSolicitante" to idSolicitante
            )
        )
    }

    suspend fun obtenerPagos(metaId: Int): List<Pago> = api.obtenerPagos(metaId)

    suspend fun registrarPago(pago: Pago): Pago = api.registrarPago(pago)
}
