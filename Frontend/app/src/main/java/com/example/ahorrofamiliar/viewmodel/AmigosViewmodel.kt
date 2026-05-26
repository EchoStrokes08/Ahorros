package com.example.ahorrofamiliar.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ahorrofamiliar.data.model.Usuario
import com.example.ahorrofamiliar.data.repository.UsuarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AmigosViewModel(
    private val repository: UsuarioRepository
) : ViewModel() {

    private val _usuarios =
        MutableStateFlow<List<Usuario>>(emptyList())

    val usuarios: StateFlow<List<Usuario>> =
        _usuarios

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

                cargarUsuarios()

                onSuccess()

            } catch (e: Exception) {

                e.printStackTrace()

            }

        }

    }

}