package com.example.ahorrofamiliar.ui.screens.detalle

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * TODO: Pantalla de detalle de una meta.
 *
 * Debe mostrar segun el enunciado:
 * - Foto de la meta
 * - Nombre y valor total
 * - Lista de miembros con sus aportes
 * - Resumen (total aportado, % faltante)
 * - Boton para ir a registrar pago
 *
 * PASOS:
 * 1. Crear DetalleMetaViewModel siguiendo el patron de ListaMetasViewModel.
 *    - Recibe el metaId.
 *    - Expone UiState<Meta> y UiState<List<Pago>>.
 * 2. Agregarlo a ViewModelFactory.
 * 3. Aqui en el Composable: observar el estado y dibujar la UI.
 */
@Composable
fun DetalleMetaScreen(metaId: Int, onRegistrarPago: (Int) -> Unit) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Detalle de meta #$metaId (por implementar)")
    }
}
