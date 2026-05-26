package com.example.ahorrofamiliar.ui.screens.perfil

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ahorrofamiliar.ui.viewmodel.PerfilViewModel

@Composable
fun PerfilScreen(
    viewModel: PerfilViewModel,
    dispositivoId: String,
    onNavigateToLista: () -> Unit
) {

    val usuario by viewModel.usuario.collectAsState()

    var nombre by remember {
        mutableStateOf("")
    }

    // =====================================
    // CARGAR USUARIO
    // =====================================

    LaunchedEffect(Unit) {

        viewModel.cargarUsuario(
            dispositivoId
        )

    }

    // =====================================
    // SI YA EXISTE
    // =====================================

    LaunchedEffect(usuario) {

        usuario?.let {

            nombre = it.nombre

        }

    }

    Column(

        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),

        verticalArrangement =
            Arrangement.Center

    ) {

        Text(
            text = "Perfil"
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        OutlinedTextField(

            value = nombre,

            onValueChange = {
                nombre = it
            },

            label = {
                Text("Nombre")
            }

        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        Button(

            onClick = {

                if (usuario == null) {

                    viewModel.crearUsuario(
                        nombre,
                        dispositivoId
                    )

                } else {

                    viewModel.editarUsuario(
                        nombre
                    )

                }

            }

        ) {

            Text("Guardar")

        }

    }
}