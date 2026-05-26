package com.example.ahorrofamiliar.ui.screens.pago

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ahorrofamiliar.viewmodel.PagoViewModel
import com.example.ahorrofamiliar.viewmodel.ViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PagoScreen(
    metaId: Int,
    userId: Int,
    onPagoRegistrado: () -> Unit
) {
    val viewModel: PagoViewModel = viewModel(factory = ViewModelFactory())
    var monto by remember { mutableStateOf("") }
    var cargando by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Registrar Aporte") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Meta ID: #$metaId", style = MaterialTheme.typography.titleMedium)
            
            OutlinedTextField(
                value = monto,
                onValueChange = { if (it.all { char -> char.isDigit() }) monto = it },
                label = { Text("Monto a aportar") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                prefix = { Text("$") }
            )

            Button(
                onClick = {
                    val montoDouble = monto.toDoubleOrNull() ?: 0.0
                    if (montoDouble > 0) {
                        cargando = true
                        viewModel.registrarPago(metaId, userId, montoDouble) {
                            cargando = false
                            onPagoRegistrado()
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = monto.isNotBlank() && !cargando
            ) {
                if (cargando) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary)
                } else {
                    Text("Confirmar Pago")
                }
            }
        }
    }
}
