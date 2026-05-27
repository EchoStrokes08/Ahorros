package com.example.ahorrofamiliar.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ahorrofamiliar.data.model.Miembro
import com.example.ahorrofamiliar.data.repository.UsuarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel encargado de la gestión del perfil del usuario actual.
 * Maneja la identificación por dispositivo, creación y edición de datos del usuario.
 *
 * @property repository Repositorio para la persistencia y consulta de datos de usuarios.
 */
class PerfilViewModel(
    private val repository: UsuarioRepository
) : ViewModel() {

    private val _usuario =
        MutableStateFlow<Miembro?>(null)

    /**
     * Estado que representa al usuario autenticado o identificado en la sesión actual.
     */
    val usuario: StateFlow<Miembro?> =
        _usuario

    /**
     * Carga la información de un usuario basándose en el ID único de su dispositivo.
     *
     * @param idDispositivo Identificador único del dispositivo (generado por DeviceUtils).
     */
    fun cargarUsuario(
        idDispositivo: String
    ) {

        viewModelScope.launch {

            try {

                val usuario =
                    repository
                        .obtenerUsuarioPorDispositivo(
                            idDispositivo
                        )

                _usuario.value = usuario

            } catch (e: Exception) {

                _usuario.value = null

            }

        }

    }

    /**
     * Crea un nuevo registro de usuario en el sistema.
     *
     * @param nombre Nombre del usuario.
     * @param idDispositivo ID del dispositivo al que se vinculará la cuenta.
     * @param onSuccess Callback ejecutado tras la creación exitosa.
     */
    fun crearUsuario(
        nombre: String,
        idDispositivo: String,
        onSuccess: () -> Unit = {}
    ) {

        viewModelScope.launch {

            try {
                val nuevoUsuario = Miembro(
                    id = 0,
                    nombre = nombre,
                    idDispositivo = idDispositivo,
                    amigos = emptyList()
                )
                val usuarioCreado = repository.crearUsuario(nuevoUsuario)
                _usuario.value = usuarioCreado
                onSuccess()
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

    }

    /**
     * Actualiza el nombre del usuario actual.
     *
     * @param nombre Nuevo nombre a asignar.
     * @param onSuccess Callback ejecutado tras la actualización exitosa.
     */
    fun editarUsuario(
        nombre: String,
        onSuccess: () -> Unit = {}
    ) {

        val usuarioActual =
            _usuario.value ?: return

        viewModelScope.launch {
            try {
                val usuarioEditado = usuarioActual.copy(nombre = nombre)
                val respuesta = repository.editarUsuario(usuarioActual.id, usuarioEditado)
                _usuario.value = respuesta
                onSuccess()
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

    }
}