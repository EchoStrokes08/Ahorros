package com.example.ahorrofamiliar.data.repository

import com.example.ahorrofamiliar.data.model.Meta
import com.example.ahorrofamiliar.data.model.Miembro
import com.example.ahorrofamiliar.data.model.Pago
import com.example.ahorrofamiliar.data.remote.ApiService

/**
 * Repository: capa intermedia entre el ViewModel y el ApiService.
 *
 * Por que existe?
 * - Para que el ViewModel no conozca a Retrofit directamente.
 * - Para poder cambiar la fuente de datos (API, base local, etc.) sin tocar el ViewModel.
 * - Para poder pasarle un ApiService falso en las pruebas unitarias.
 */
class UsuarioRepository(private val api: ApiService) {

    suspend fun obtenerUsuarioPorDispositivo( idDispositivo: String): Miembro {
        return api.obtenerUsuarioPorDispositivo(
            idDispositivo
        )
    }

    suspend fun crearUsuario( usuario: Miembro ): Miembro {
        return api.crearUsuario(usuario)
    }

    suspend fun editarUsuario(id: Int, usuario: Miembro): Miembro {
        return api.editarUsuario(
            id,
            usuario
        )
    }
    suspend fun obtenerUsuarios(): List<Miembro> {
        return api.obtenerUsuarios()
    }
    suspend fun agregarAmigo(idUsuario: Int, idAmigo: Int
    ) {
        api.agregarAmigo(
            idUsuario,
            mapOf(
                "friendId" to idAmigo
            )
        )
    }
}
