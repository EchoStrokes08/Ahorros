package com.example.ahorrofamiliar.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ahorrofamiliar.data.model.Meta
import com.example.ahorrofamiliar.data.repository.MetaRepository
import kotlinx.coroutines.launch

class CrearMetaViewModel(
    private val repository: MetaRepository
) : ViewModel() {

    fun crearMeta(
        titulo: String,
        montoObjetivo: Double,
        imagen: String,
        idPrincipal: Int,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            try {
                val nuevaMeta = Meta(
                    id = 0,
                    nombre = titulo,
                    valorTotal = montoObjetivo.toLong(),
                    fotoUrl = imagen,
                    idUsuario = idPrincipal
                )
                repository.crearMeta(nuevaMeta)
                onSuccess()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
