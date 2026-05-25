package com.example.ahorrofamiliar.data.model

data class Pago(
    val id: Int? = null,
    val metaId: Int,
    val miembroId: Int,
    val monto: Double,
    val fecha: String? = null
)
