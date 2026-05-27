package com.example.ahorrofamiliar.ui.screens.lista

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ahorrofamiliar.viewmodel.CrearMetaViewModel
import com.example.ahorrofamiliar.viewmodel.ViewModelFactory

@Composable
fun CrearMetaScreen(
    userId: Int,
    onMetaCreada: () -> Unit
) {
    val viewModel: CrearMetaViewModel = viewModel(factory = ViewModelFactory())

    var titulo by remember { mutableStateOf("") }
    var monto by remember { mutableStateOf("") }
    var imagen by remember { mutableStateOf("https://cdn-icons-png.flaticon.com/512/1077/1077114.png") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Nueva Meta de Ahorro", style = MaterialTheme.typography.headlineMedium)

        OutlinedTextField(
            value = titulo,
            onValueChange = { titulo = it },
            label = { Text("¿Qué quieres comprar?") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = monto,
            onValueChange = { monto = it },
            label = { Text("Monto Objetivo") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = imagen,
            onValueChange = { imagen = it },
            label = { Text("URL de la Imagen") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                val montoDouble = monto.toDoubleOrNull() ?: 0.0
                viewModel.crearMeta(titulo, montoDouble, imagen, userId) {
                    onMetaCreada()
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = titulo.isNotBlank() && monto.isNotBlank()
        ) {
            Text("Crear Meta")
        }
    }
}
