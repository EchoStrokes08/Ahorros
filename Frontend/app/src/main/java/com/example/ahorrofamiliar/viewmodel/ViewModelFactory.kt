package com.example.ahorrofamiliar.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ahorrofamiliar.data.remote.RetrofitClient
import com.example.ahorrofamiliar.data.repository.MetaRepository
import com.example.ahorrofamiliar.data.repository.UsuarioRepository
import com.example.ahorrofamiliar.ui.viewmodel.PerfilViewModel
import com.example.ahorrofamiliar.ui.viewmodel.AmigosViewModel

class ViewModelFactory : ViewModelProvider.Factory {

    private val apiService = RetrofitClient.apiService
    private val metaRepository = MetaRepository(apiService)
    private val usuarioRepository = UsuarioRepository(apiService)

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(ListaMetasViewModel::class.java) ->
                ListaMetasViewModel(metaRepository) as T
            
            modelClass.isAssignableFrom(PerfilViewModel::class.java) ->
                PerfilViewModel(usuarioRepository) as T
            
            modelClass.isAssignableFrom(AmigosViewModel::class.java) ->
                AmigosViewModel(usuarioRepository) as T
            
            modelClass.isAssignableFrom(DetalleMetaViewModel::class.java) ->
                DetalleMetaViewModel(metaRepository, usuarioRepository) as T
            
            modelClass.isAssignableFrom(PagoViewModel::class.java) ->
                PagoViewModel(metaRepository) as T
            
            modelClass.isAssignableFrom(CrearMetaViewModel::class.java) ->
                CrearMetaViewModel(metaRepository) as T

            else -> throw IllegalArgumentException("ViewModel desconocido: ${modelClass.name}")
        }
    }
}
