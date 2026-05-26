package com.example.ahorrofamiliar.data.model

/**
 * Representa una meta de ahorro familiar.
 * Por ejemplo: "TV 55 pulgadas", "Moto", etc.
 */
data class Meta(
    val id: Int,
    val nombre: String,
    val valorTotal: Double,
    val totalAhorrado: Double,
    val fotoUrl: String,
    val idUsuario: Int, // el dueño original
    val miembros: List<Miembro> = emptyList()
) {
    // Porcentaje completado de la meta (0.0 a 1.0)
    val progreso: Float
        get() = if (valorTotal > 0) (totalAhorrado / valorTotal).toFloat().coerceIn(0f, 1f) else 0f

    // Porcentaje faltante (0 a 100)
    val porcentajeFaltante: Int
        get() = ((1f - progreso) * 100).toInt()
}
