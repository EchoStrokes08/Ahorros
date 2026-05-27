package com.example.ahorrofamiliar.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ahorrofamiliar.data.model.Pago
import com.example.ahorrofamiliar.data.repository.MetaRepository
import kotlinx.coroutines.launch

class PagoViewModel(
    private val repository: MetaRepository
) : ViewModel() {

    fun registrarPago(
        metaId: Int,
        userId: Int,
        monto: Double,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            try {
                val nuevoPago = Pago(
                    metaId = metaId,
                    idUsuario = userId,
                    monto = monto
                )
                repository.registrarPago(nuevoPago)
                onSuccess()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
