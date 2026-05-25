package com.example.ahorrofamiliar.ui.screens.pago

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * TODO: Pantalla para registrar un pago.
 *
 * Debe permitir:
 * - Seleccionar el miembro que paga (dropdown).
 * - Ingresar el monto (TextField).
 * - Boton "Registrar" que llama al ViewModel -> repository.registrarPago(...).
 *
 * PASOS:
 * 1. Crear PagoViewModel con un metodo registrarPago(metaId, miembroId, monto).
 * 2. Agregarlo a ViewModelFactory.
 * 3. Aqui usar OutlinedTextField, DropdownMenu, Button.
 */
@Composable
fun PagoScreen(metaId: Int, onPagoRegistrado: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Registrar pago para meta #$metaId (por implementar)")
    }
}
