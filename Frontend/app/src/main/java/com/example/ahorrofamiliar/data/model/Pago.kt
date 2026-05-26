package com.example.ahorrofamiliar.data.model

import com.google.gson.annotations.SerializedName

data class Pago(
    //Los SerializedName es para poder tener los mismos valores que en el backend
    // y no haya confilctos por llamar diferentes las variables
    val id: Int? = null,
    val metaId: Int,
    @SerializedName("idMiembro")
    val idUsuario: Int,
    @SerializedName("montoAportado")
    val monto: Double,
    val fecha: String? = null
)