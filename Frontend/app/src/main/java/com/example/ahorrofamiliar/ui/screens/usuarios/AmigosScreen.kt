package com.example.ahorrofamiliar.ui.screens.amigos

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items

import androidx.compose.material3.*

import androidx.compose.runtime.*

import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

import com.example.ahorrofamiliar.data.model.Miembro
import com.example.ahorrofamiliar.viewmodel.AmigosViewModel

@Composable
fun AmigosScreen(
    viewModel: AmigosViewModel,
    usuarioActual: Miembro,
    onAmigoAdded: () -> Unit = {}
) {

    val usuarios by
    viewModel.usuarios.collectAsState()

    LaunchedEffect(Unit) {

        viewModel.cargarUsuarios()

    }

    LazyColumn(

        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)

    ) {

        items(usuarios) { usuario ->

            /*
            No mostrarme a mí mismo
            */

            if (
                usuario.id !=
                usuarioActual.id
            ) {

                Card(

                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)

                ) {

                    Row(

                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),

                        horizontalArrangement =
                            Arrangement.SpaceBetween

                    ) {

                        Text(
                            usuario.nombre
                        )

                        val esAmigo =

                            usuarioActual.amigos
                                .contains(
                                    usuario.id
                                )

                        if (esAmigo) {

                            Text("Amigo")

                        } else {

                            Button(

                                onClick = {

                                    viewModel.agregarAmigo(
                                        usuarioActual.id,
                                        usuario.id,
                                        onSuccess = onAmigoAdded
                                    )

                                }

                            ) {

                                Text("Agregar")

                            }
                        }
                    }
                }
            }
        }
    }
}