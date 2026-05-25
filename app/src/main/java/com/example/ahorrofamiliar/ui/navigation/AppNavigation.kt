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

object Rutas {
    const val LISTA = "lista"
    const val DETALLE = "detalle/{metaId}"
    const val PAGO = "pago/{metaId}"

    fun detalle(metaId: Int) = "detalle/$metaId"
    fun pago(metaId: Int) = "pago/$metaId"
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Rutas.LISTA) {

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
