package com.example.ahorrofamiliar.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ahorrofamiliar.data.model.Usuario
import com.example.ahorrofamiliar.data.repository.UsuarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PerfilViewModel(
    private val repository: UsuarioRepository
) : ViewModel() {

    private val _usuario =
        MutableStateFlow<Usuario?>(null)

    val usuario: StateFlow<Usuario?> =
        _usuario

    // =====================================
    // CARGAR USUARIO
    // =====================================

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

    // =====================================
    // CREAR USUARIO
    // =====================================

    fun crearUsuario(
        nombre: String,
        idDispositivo: String,
        onSuccess: () -> Unit = {}
    ) {

        viewModelScope.launch {

            try {
                val nuevoUsuario = Usuario(
                    id = 0,
                    nombre = nombre,
                    idDispositivo = idDispositivo,
                    amigos = emptyList()
                )
                val usuarioCreado = repository.crearUsuario(nuevoUsuario)
                _usuario.value = usuarioCreado
                onSuccess() // Llamar al éxito
            } catch (e: Exception) {
                e.printStackTrace()
                // Aquí podrías manejar un estado de error para mostrar en la UI
            }

        }

    }

    // =====================================
    // EDITAR USUARIO
    // =====================================

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
                onSuccess() // Llamar al éxito
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

    }
}