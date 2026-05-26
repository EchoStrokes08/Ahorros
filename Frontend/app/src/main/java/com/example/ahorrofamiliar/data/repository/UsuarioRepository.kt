package com.example.ahorrofamiliar.data.repository

import com.example.ahorrofamiliar.data.model.Usuario
import com.example.ahorrofamiliar.data.remote.ApiService

/**
 * Repository: capa intermedia entre los ViewModel de perfil, amigos y el ApiService.
 *
 * Por que existe?
 * - Para que el ViewModel no conozca a Retrofit directamente.
 * - Para poder cambiar la fuente de datos (API, base local, etc.) sin tocar el ViewModel.
 * - Para poder pasarle un ApiService falso con los servicios de usuario en las pruebas unitarias.
 */
class UsuarioRepository(
    private val api: ApiService
) {
    suspend fun obtenerUsuarioPorDispositivo( idDispositivo: String): Usuario {
        return api.obtenerUsuarioPorDispositivo(
            idDispositivo
        )
    }

    suspend fun crearUsuario( usuario: Usuario ): Usuario {
        return api.crearUsuario(usuario)
    }

    suspend fun editarUsuario(id: Int, usuario: Usuario): Usuario {
        return api.editarUsuario(
            id,
            usuario
        )
    }
    suspend fun obtenerUsuarios(): List<Usuario> {
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