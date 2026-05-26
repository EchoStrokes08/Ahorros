package com.example.ahorrofamiliar.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ahorrofamiliar.data.remote.RetrofitClient
import com.example.ahorrofamiliar.data.repository.MetaRepository

/**
 * Factory unica para crear todos los ViewModels.
 * Inyecta el Repository con la implementacion real (Retrofit).
 */
class ViewModelFactory : ViewModelProvider.Factory {

    private val repository = MetaRepository(RetrofitClient.apiService)

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(ListaMetasViewModel::class.java) ->
                ListaMetasViewModel(repository) as T

            else -> throw IllegalArgumentException("ViewModel desconocido: ${modelClass.name}")
        }
    }
}
