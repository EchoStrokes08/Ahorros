package com.example.ahorrofamiliar.data.repository

import com.example.ahorrofamiliar.data.model.Usuario
import com.example.ahorrofamiliar.data.remote.ApiService

class UsuarioRepository(
    private val api: ApiService
) {

    suspend fun obtenerUsuarioPorDispositivo(
        idDispositivo: String
    ): Usuario {

        return api.obtenerUsuarioPorDispositivo(
            idDispositivo
        )

    }

    suspend fun crearUsuario(
        usuario: Usuario
    ): Usuario {

        return api.crearUsuario(usuario)

    }

    suspend fun editarUsuario(
        id: Int,
        usuario: Usuario
    ): Usuario {

        return api.editarUsuario(
            id,
            usuario
        )

    }
    suspend fun obtenerUsuarios():
            List<Usuario> {

        return api.obtenerUsuarios()
    }
    suspend fun agregarAmigo(
        idUsuario: Int,
        idAmigo: Int
    ) {

        api.agregarAmigo(
            idUsuario,
            mapOf(
                "friendId" to idAmigo
            )
        )
    }
}