package com.example.ahorrofamiliar.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ahorrofamiliar.data.model.Usuario
import com.example.ahorrofamiliar.data.repository.UsuarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel encargado de la gestión de amigos.
 * Permite listar usuarios y establecer vínculos de amistad entre ellos.
 *
 * @property repository Repositorio de usuarios para realizar operaciones de red.
 */
class AmigosViewModel(
    private val repository: UsuarioRepository
) : ViewModel() {

    private val _usuarios =
        MutableStateFlow<List<Usuario>>(emptyList())

    /**
     * Flujo de estado que contiene la lista de usuarios disponibles en el sistema.
     */
    val usuarios: StateFlow<List<Usuario>> =
        _usuarios

    /**
     * Carga la lista completa de usuarios desde el servidor.
     * Actualiza el estado [_usuarios] al finalizar.
     */
    fun cargarUsuarios() {

        viewModelScope.launch {

            try {

                _usuarios.value =
                    repository.obtenerUsuarios()

            } catch (e: Exception) {

                e.printStackTrace()

            }

        }

    }

    /**
     * Agrega un nuevo vínculo de amistad entre dos usuarios.
     *
     * @param usuarioId ID del usuario que solicita la amistad.
     * @param amigoId ID del usuario que será agregado como amigo.
     * @param onSuccess Callback opcional que se ejecuta si la operación es exitosa.
     */
    fun agregarAmigo(
        usuarioId: Int,
        amigoId: Int,
        onSuccess: () -> Unit = {}
    ) {

        viewModelScope.launch {

            try {

                repository.agregarAmigo(
                    usuarioId,
                    amigoId
                )
                // Refrescar la lista local después de agregar
                cargarUsuarios()

                onSuccess()

            } catch (e: Exception) {

                e.printStackTrace()

            }

        }

    }

}