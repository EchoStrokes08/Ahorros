package com.example.ahorrofamiliar.data.model

import com.google.gson.annotations.SerializedName

/**
 * Representa una meta de ahorro familiar.
 * Por ejemplo: "TV 55 pulgadas", "Moto", etc.
 */
data class Meta(

    //Los SerializedName es para poder tener los mismos valores que en el backend
    // y no haya confilctos por llamar diferentes las variables
    val id: Int,
    @SerializedName("titulo")
    val nombre: String,
    @SerializedName("montoObjetivo")
    val valorTotal: Long,
    @SerializedName("totalSalvado")
    val totalAhorrado: Double = 0.0,
    @SerializedName("imagen")
    val fotoUrl: String,
    @SerializedName("idPrincipal")
    val idUsuario: Int, // el dueño original
    val miembros: List<Int> = emptyList()
) {
    // Porcentaje completado de la meta (0.0 a 1.0)
    val progreso: Float
        get() = if (valorTotal > 0) (totalAhorrado / valorTotal).toFloat().coerceIn(0f, 1f) else 0f

    // Porcentaje faltante (0 a 100)
    val porcentajeFaltante: Int
        get() = ((1f - progreso) * 100).toInt()
}
