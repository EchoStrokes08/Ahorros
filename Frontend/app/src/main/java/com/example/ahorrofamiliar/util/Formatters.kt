package com.example.ahorrofamiliar.util

import java.text.NumberFormat
import java.util.Locale

/**
 * Formatea un Double como moneda colombiana sin decimales.
 * Ej: 2500000.0 -> "$ 2.500.000"
 */
fun Double.aPesos(): String {
    val formatter = NumberFormat.getCurrencyInstance(Locale("es", "CO")).apply {
        maximumFractionDigits = 0
    }
    return formatter.format(this)
}
