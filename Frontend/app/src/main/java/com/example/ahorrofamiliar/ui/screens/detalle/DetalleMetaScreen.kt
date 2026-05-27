package com.example.ahorrofamiliar.ui.screens.detalle

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.ahorrofamiliar.data.model.Meta
import com.example.ahorrofamiliar.data.model.Pago
import com.example.ahorrofamiliar.viewmodel.DetalleMetaViewModel
import com.example.ahorrofamiliar.viewmodel.UiState
import com.example.ahorrofamiliar.viewmodel.ViewModelFactory

@Composable
fun DetalleMetaScreen(
    metaId: Int,
    userId: Int,
    onRegistrarPago: (Int) -> Unit
) {
    val viewModel: DetalleMetaViewModel = viewModel(factory = ViewModelFactory())
    val metaState by viewModel.meta.collectAsState()
    val pagos by viewModel.pagos.collectAsState()
    val amigos by viewModel.amigosDisponibles.collectAsState()
    val nombresUsuarios by viewModel.nombresUsuarios.collectAsState()
    val miembrosAportes by viewModel.miembrosAportes.collectAsState()

    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(metaId, userId) {
        viewModel.cargarDetalle(metaId, userId)
    }

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { onRegistrarPago(metaId) },
                icon = { Icon(Icons.Default.Add, contentDescription = null) },
                text = { Text("Aportar") }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            when (val state = metaState) {
                is UiState.Loading -> CircularProgressIndicator(Modifier.align(Alignment.Center))
                is UiState.Error -> Text("Error: ${state.mensaje}", color = Color.Red, modifier = Modifier.align(Alignment.Center))
                is UiState.Success -> {
                    val meta = state.data
                    LazyColumn(
                        modifier = Modifier.fillMaxSize().padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        item {
                            AsyncImage(
                                model = meta.fotoUrl,
                                contentDescription = null,
                                modifier = Modifier.fillMaxWidth().height(200.dp)
                            )
                            Spacer(Modifier.height(8.dp))
                            Text(meta.nombre, fontSize = 28.sp, fontWeight = FontWeight.Bold)
                            Text("Objetivo: $${meta.valorTotal}", fontSize = 18.sp)
                            Text("Ahorrado: $${meta.totalAhorrado}", fontSize = 18.sp, color = Color.Green)
                            
                            Spacer(Modifier.height(8.dp))
                            LinearProgressIndicator(
                                progress = meta.progreso,
                                modifier = Modifier.fillMaxWidth().height(12.dp)
                            )
                            Text("Progreso: ${(meta.progreso * 100).toInt()}%")
                        }

                        item {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Miembros y Aportes", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                                if (meta.idUsuario == userId) {
                                    Button(onClick = { showDialog = true }) {
                                        Text("Invitar")
                                    }
                                }
                            }
                        }

                        items(miembrosAportes) { miembro ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp).fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(miembro.nombre, fontWeight = FontWeight.Medium)
                                    Text("$${miembro.montoTotal}", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                                }
                            }
                        }

                        item {
                            Spacer(Modifier.height(8.dp))
                            Text("Historial de Pagos", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                        }

                        if (pagos.isEmpty()) {
                            item { Text("No hay pagos registrados aún.") }
                        } else {
                            items(pagos) { pago ->
                                Card(modifier = Modifier.fillMaxWidth()) {
                                    Row(modifier = Modifier.padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                                        Column {
                                            val nombreAutor = nombresUsuarios[pago.idUsuario] ?: "Usuario #${pago.idUsuario}"
                                            Text(nombreAutor, fontWeight = FontWeight.Bold)
                                            Text("Fecha: ${pago.fecha ?: "N/A"}", fontSize = 12.sp)
                                        }
                                        Text("$${pago.monto}", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color(0xFF2E7D32))
                                    }
                                }
                            }
                        }
                    }

                    if (showDialog) {
                        AlertDialog(
                            onDismissRequest = { showDialog = false },
                            title = { Text("Invitar Amigo a la Meta") },
                            text = {
                                Column {
                                    if (amigos.isEmpty()) {
                                        Text("No tienes amigos disponibles para invitar.")
                                    } else {
                                        amigos.forEach { amigo ->
                                            TextButton(
                                                onClick = {
                                                    viewModel.agregarMiembro(meta.id, amigo.id, userId)
                                                    showDialog = false
                                                },
                                                modifier = Modifier.fillMaxWidth()
                                            ) {
                                                Text(amigo.nombre)
                                            }
                                        }
                                    }
                                }
                            },
                            confirmButton = {
                                TextButton(onClick = { showDialog = false }) { Text("Cerrar") }
                            }
                        )
                    }
                }
            }
        }
    }
}
