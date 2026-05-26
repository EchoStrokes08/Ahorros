package com.example.ahorrofamiliar.ui.screens.lista

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.ahorrofamiliar.data.model.Meta
import com.example.ahorrofamiliar.viewmodel.ListaMetasViewModel
import com.example.ahorrofamiliar.viewmodel.UiState
import com.example.ahorrofamiliar.viewmodel.ViewModelFactory

/**
 * Pantalla principal: lista de metas de ahorro.
 *
 * Esta pantalla SOLO observa el estado del ViewModel.
 * Toda la logica de red, parseo y manejo de errores vive en el ViewModel/Repository.
 */
@Composable
fun ListaMetasScreen(
    onMetaClick: (Int) -> Unit
) {
    val viewModel: ListaMetasViewModel = viewModel(factory = ViewModelFactory())
    val uiState by viewModel.uiState.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {

        Text(
            text = "Metas de Ahorro",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // La UI reacciona segun el estado
        when (val estado = uiState) {
            is UiState.Loading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is UiState.Error -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("Error: ${estado.mensaje}")
                    Spacer(Modifier.height(8.dp))
                    Button(onClick = { viewModel.cargarMetas() }) {
                        Text("Reintentar")
                    }
                }
            }
            is UiState.Success -> {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(estado.data) { meta ->
                        TarjetaMeta(meta = meta, onClick = { onMetaClick(meta.id) })
                    }
                }
            }
        }
    }
}

@Composable
fun TarjetaMeta(meta: Meta, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            AsyncImage(
                model = meta.fotoUrl,
                contentDescription = meta.nombre,
                modifier = Modifier.fillMaxWidth().height(140.dp)
            )

            Spacer(Modifier.height(8.dp))

            Text(meta.nombre, fontSize = 20.sp, fontWeight = FontWeight.Bold)

            Text("Meta: \$${meta.valorTotal}")
            Text("Ahorrado: \$${meta.totalAhorrado}")

            Spacer(Modifier.height(8.dp))

            LinearProgressIndicator(
                progress = meta.progreso,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(4.dp))

            Text("Falta: ${meta.porcentajeFaltante}%")
        }
    }
}
