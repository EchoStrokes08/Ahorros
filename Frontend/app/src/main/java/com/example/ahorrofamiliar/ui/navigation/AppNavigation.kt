package com.example.ahorrofamiliar.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Add

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.lifecycle.viewmodel.compose.viewModel


import com.example.ahorrofamiliar.viewmodel.ViewModelFactory
import com.example.ahorrofamiliar.ui.screens.detalle.DetalleMetaScreen
import com.example.ahorrofamiliar.ui.screens.lista.ListaMetasScreen
import com.example.ahorrofamiliar.ui.screens.lista.CrearMetaScreen
import com.example.ahorrofamiliar.ui.screens.pago.PagoScreen
import com.example.ahorrofamiliar.ui.screens.usuarios.PerfilScreen
import com.example.ahorrofamiliar.ui.viewmodel.PerfilViewModel
import com.example.ahorrofamiliar.ui.screens.amigos.AmigosScreen
import com.example.ahorrofamiliar.util.DeviceUtils
import com.example.ahorrofamiliar.viewmodel.AmigosViewModel

object Rutas {

    const val LISTA = "lista"
    const val DETALLE = "detalle/{metaId}"
    const val PAGO = "pago/{metaId}"
    const val PERFIL = "perfil"
    const val AMIGOS = "amigos"
    const val CREAR_META = "crear_meta"

    /**
     * Genera la ruta para el detalle de una meta específica.
     */
    fun detalle(metaId: Int) = "detalle/$metaId"

    /**
     * Genera la ruta para registrar un pago a una meta específica.
     */
    fun pago(metaId: Int) = "pago/$metaId"
}

/**
 * Componente principal de navegación de la aplicación.
 * Gestiona el NavHost, la NavigationBar inferior y la lógica de redirección basada en el estado del usuario.
 */
@Composable
fun AppNavigation() {

    val context = LocalContext.current

    // Identificador único del dispositivo para persistencia sin login
    val deviceId = DeviceUtils.obtenerDispositivoId(context)

    // ViewModels compartidos a nivel de navegación
    val perfilViewModel: PerfilViewModel = viewModel(
        factory = ViewModelFactory()
    )
    val amigosViewModel: AmigosViewModel = viewModel(
        factory = ViewModelFactory()
    )
    val navController = rememberNavController()

    val backStackEntry by navController.currentBackStackEntryAsState()

    val currentRoute = backStackEntry?.destination?.route

    // Carga inicial del usuario al arrancar la app
    LaunchedEffect(Unit) {
        perfilViewModel.cargarUsuario(deviceId)
    }

    Scaffold(

        bottomBar = {

            NavigationBar {

                NavigationBarItem(

                    selected = currentRoute == Rutas.LISTA,

                    onClick = {

                        navController.navigate(Rutas.LISTA) {

                            popUpTo(navController.graph.startDestinationId)

                            launchSingleTop = true
                        }
                    },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Metas") },
                    label = { Text("Metas") }
                )
                // Item: Gestión de Amigos
                NavigationBarItem(
                    selected = currentRoute == Rutas.AMIGOS,
                    onClick = {
                        navController.navigate(Rutas.AMIGOS) {
                            popUpTo(navController.graph.startDestinationId)
                            launchSingleTop = true
                        }
                    },
                    icon = { Icon(Icons.Default.Add, contentDescription = "Amigos") },
                    label = { Text("Amigos") }
                )
                // Item: Perfil de Usuario
                NavigationBarItem(
                    selected = currentRoute == Rutas.PERFIL,
                    onClick = {
                        navController.navigate(Rutas.PERFIL) {
                            popUpTo(navController.graph.startDestinationId)
                            launchSingleTop = true
                        }
                    },
                    icon = { Icon(Icons.Default.Person, contentDescription = "Perfil") },
                    label = { Text("Perfil") }
                )
            }
        }
    ) { paddingValues ->

        NavHost(
            navController = navController,
            startDestination = Rutas.PERFIL,
            modifier = Modifier.padding(paddingValues)
        ) {

            // Pantalla de Perfil / Registro
            composable(Rutas.PERFIL) {
                PerfilScreen(
                    viewModel = perfilViewModel,
                    idDispositivo = deviceId,
                    onNavigateToLista = {
                        navController.navigate(Rutas.LISTA)
                    }
                )
            }

            // Pantalla de Lista de Amigos
            composable(Rutas.AMIGOS) {
                val usuario by perfilViewModel.usuario.collectAsState()
                usuario?.let {
                    AmigosScreen(
                        viewModel = amigosViewModel,
                        usuarioActual = it,
                        onAmigoAdded = {
                            perfilViewModel.cargarUsuario(deviceId)
                        }
                    )
                }
            }

            // Pantalla Principal: Listado de Metas de Ahorro
            composable(Rutas.LISTA) {
                val usuario by perfilViewModel.usuario.collectAsState()
                ListaMetasScreen(
                    userId = usuario?.id ?: 0,
                    onMetaClick = { metaId ->
                        navController.navigate(Rutas.detalle(metaId))
                    },
                    onCrearMetaClick = {
                        navController.navigate(Rutas.CREAR_META)
                    }
                )
            }

            // Pantalla para crear una nueva meta
            composable(Rutas.CREAR_META) {
                val usuario by perfilViewModel.usuario.collectAsState()
                CrearMetaScreen(
                    userId = usuario?.id ?: 0,
                    onMetaCreada = {
                        navController.popBackStack()
                    }
                )
            }

            // Pantalla de Detalle de Meta (con lista de miembros y aportes)
            composable(
                route = Rutas.DETALLE,
                arguments = listOf(
                    navArgument("metaId") { type = NavType.IntType }
                )
            ) { backStackEntry ->
                val metaId = backStackEntry.arguments?.getInt("metaId") ?: 0
                val usuario by perfilViewModel.usuario.collectAsState()

                DetalleMetaScreen(
                    metaId = metaId,
                    userId = usuario?.id ?: 0,
                    onRegistrarPago = { id ->
                        navController.navigate(Rutas.pago(id))
                    }
                )
            }

            // Pantalla para registrar un nuevo pago/aporte
            composable(
                route = Rutas.PAGO,
                arguments = listOf(
                    navArgument("metaId") { type = NavType.IntType }
                )
            ) { backStackEntry ->
                val metaId = backStackEntry.arguments?.getInt("metaId") ?: 0
                val usuario by perfilViewModel.usuario.collectAsState()

                PagoScreen(
                    metaId = metaId,
                    userId = usuario?.id ?: 0,
                    onPagoRegistrado = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}
