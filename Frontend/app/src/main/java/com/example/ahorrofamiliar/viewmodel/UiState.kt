package com.example.ahorrofamiliar.viewmodel

/**
 * Estado generico para cualquier pantalla que carga datos.
 * Loading -> Success(datos)  o  Error(mensaje)
 *
 * La UI observa este estado y muestra lo que corresponda.
 */
sealed class UiState<out T> {
    object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val mensaje: String) : UiState<Nothing>()
}
