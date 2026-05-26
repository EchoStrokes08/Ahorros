package com.example.ahorrofamiliar.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.ahorrofamiliar.ui.screens.detalle.DetalleMetaScreen
import com.example.ahorrofamiliar.ui.screens.lista.ListaMetasScreen
import com.example.ahorrofamiliar.ui.screens.pago.PagoScreen
import com.example.ahorrofamiliar.ui.screens.perfil.PerfilScreen


import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ahorrofamiliar.data.remote.RetrofitClient
import com.example.ahorrofamiliar.data.repository.UsuarioRepository
import com.example.ahorrofamiliar.ui.viewmodel.PerfilViewModel
import com.example.ahorrofamiliar.utils.DeviceUtils


object Rutas {
    const val LISTA = "lista"
    const val DETALLE = "detalle/{metaId}"
    const val PAGO = "pago/{metaId}"

    const val PERFIL = "perfil"

    fun detalle(metaId: Int) = "detalle/$metaId"
    fun pago(metaId: Int) = "pago/$metaId"
}

@Composable
fun AppNavigation() {

    val context = LocalContext.current

    val deviceId =
        DeviceUtils.obtenerDispositivoId(
            context
        )
    val usuarioRepository =
        UsuarioRepository(
            RetrofitClient.apiService
        )
    val perfilViewModel =
        PerfilViewModel(
            usuarioRepository
        )

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Rutas.PERFIL) {

        composable(Rutas.PERFIL) {

            PerfilScreen(
                viewModel = perfilViewModel,
                dispositivoId = deviceId
            )

        }

        composable(Rutas.LISTA) {
            ListaMetasScreen(
                onMetaClick = { metaId ->
                    navController.navigate(Rutas.detalle(metaId))
                }
            )
        }

        composable(
            route = Rutas.DETALLE,
            arguments = listOf(navArgument("metaId") { type = NavType.IntType })
        ) { backStackEntry ->
            val metaId = backStackEntry.arguments?.getInt("metaId") ?: 0
            DetalleMetaScreen(
                metaId = metaId,
                onRegistrarPago = { id -> navController.navigate(Rutas.pago(id)) }
            )
        }

        composable(
            route = Rutas.PAGO,
            arguments = listOf(navArgument("metaId") { type = NavType.IntType })
        ) { backStackEntry ->
            val metaId = backStackEntry.arguments?.getInt("metaId") ?: 0
            PagoScreen(
                metaId = metaId,
                onPagoRegistrado = { navController.popBackStack() }
            )
        }
    }
}
