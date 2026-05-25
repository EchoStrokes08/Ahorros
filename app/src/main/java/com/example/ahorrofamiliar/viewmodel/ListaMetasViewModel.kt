package com.example.ahorrofamiliar.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ahorrofamiliar.data.model.Meta
import com.example.ahorrofamiliar.data.repository.MetaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel para la pantalla de lista de metas.
 *
 * - Expone un StateFlow con el estado actual (Loading / Success / Error).
 * - La UI solo lo observa, no contiene logica.
 */
class ListaMetasViewModel(
    private val repository: MetaRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<Meta>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<Meta>>> = _uiState.asStateFlow()

    init {
        cargarMetas()
    }

    fun cargarMetas() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val metas = repository.obtenerMetas()
                _uiState.value = UiState.Success(metas)
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Error desconocido")
            }
        }
    }
}
