package com.example.ahorrofamiliar.ui.navigation

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.People
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.ahorrofamiliar.viewmodel.ViewModelFactory
import com.example.ahorrofamiliar.ui.screens.detalle.DetalleMetaScreen
import com.example.ahorrofamiliar.ui.screens.lista.ListaMetasScreen
import com.example.ahorrofamiliar.ui.screens.lista.CrearMetaScreen
import com.example.ahorrofamiliar.ui.screens.pago.PagoScreen
import com.example.ahorrofamiliar.ui.screens.usuarios.PerfilScreen
import com.example.ahorrofamiliar.ui.viewmodel.PerfilViewModel
import com.example.ahorrofamiliar.ui.viewmodel.AmigosViewModel
import com.example.ahorrofamiliar.ui.screens.amigos.AmigosScreen
import com.example.ahorrofamiliar.utils.DeviceUtils

// ─── Paleta compartida ────────────────────────────────────────────────────────
private val BgDeep        = Color(0xFF0A0A12)
private val BgCard        = Color(0xFF13131F)
private val BgCardBorder  = Color(0xFF252535)
private val Gold          = Color(0xFFC9A84C)
private val GoldLight     = Color(0xFFE4C97A)
private val GoldDim       = Color(0xFF5A4820)
private val TextPrimary   = Color(0xFFF0EAD6)
private val TextSecondary = Color(0xFF8C8A7E)

// ─── Rutas ────────────────────────────────────────────────────────────────────
object Rutas {
    const val LISTA      = "lista"
    const val DETALLE    = "detalle/{metaId}"
    const val PAGO       = "pago/{metaId}"
    const val PERFIL     = "perfil"
    const val AMIGOS     = "amigos"
    const val CREAR_META = "crear_meta"

    fun detalle(metaId: Int) = "detalle/$metaId"
    fun pago(metaId: Int)    = "pago/$metaId"
}

// ─── Modelo de ítem de nav ────────────────────────────────────────────────────
private data class NavItem(
    val route: String,
    val icon: ImageVector,
    val label: String
)

private val navItems = listOf(
    NavItem(Rutas.LISTA,   Icons.Default.Home,   "Metas"),
    NavItem(Rutas.AMIGOS,  Icons.Default.People, "Amigos"),
    NavItem(Rutas.PERFIL,  Icons.Default.Person, "Perfil"),
)

// ─── Rutas que muestran la barra inferior ─────────────────────────────────────
private val rootRoutes = setOf(Rutas.LISTA, Rutas.AMIGOS, Rutas.PERFIL)

@Composable
fun AppNavigation() {
    val context     = LocalContext.current
    val deviceId    = DeviceUtils.obtenerDispositivoId(context)

    val perfilViewModel: PerfilViewModel = viewModel(factory = ViewModelFactory())
    val amigosViewModel: AmigosViewModel = viewModel(factory = ViewModelFactory())
    val navController = rememberNavController()

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute   = backStackEntry?.destination?.route

    LaunchedEffect(Unit) { perfilViewModel.cargarUsuario(deviceId) }

    val showBottomBar = currentRoute in rootRoutes

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BgDeep)
    ) {
        // ── NavHost sin padding de Scaffold ──────────────────────────────
        NavHost(
            navController    = navController,
            startDestination = Rutas.PERFIL,
            modifier         = Modifier
                .fillMaxSize()
                .then(
                    if (showBottomBar)
                        Modifier.padding(bottom = 80.dp)
                    else
                        Modifier
                )
        ) {
            composable(Rutas.PERFIL) {
                PerfilScreen(
                    viewModel       = perfilViewModel,
                    idDispositivo   = deviceId,
                    onNavigateToLista = { navController.navigate(Rutas.LISTA) }
                )
            }

            composable(Rutas.AMIGOS) {
                val usuario by perfilViewModel.usuario.collectAsState()
                usuario?.let {
                    AmigosScreen(
                        viewModel      = amigosViewModel,
                        usuarioActual  = it,
                        onAmigoAdded   = { perfilViewModel.cargarUsuario(deviceId) }
                    )
                }
            }

            composable(Rutas.LISTA) {
                val usuario by perfilViewModel.usuario.collectAsState()
                ListaMetasScreen(
                    userId          = usuario?.id ?: 0,
                    onMetaClick     = { metaId -> navController.navigate(Rutas.detalle(metaId)) },
                    onCrearMetaClick = { navController.navigate(Rutas.CREAR_META) }
                )
            }

            composable(Rutas.CREAR_META) {
                val usuario by perfilViewModel.usuario.collectAsState()
                CrearMetaScreen(
                    userId       = usuario?.id ?: 0,
                    onMetaCreada = { navController.popBackStack() },
                    onBack       = { navController.popBackStack() }
                )
            }

            composable(
                route     = Rutas.DETALLE,
                arguments = listOf(navArgument("metaId") { type = NavType.IntType })
            ) { backStack ->
                val metaId  = backStack.arguments?.getInt("metaId") ?: 0
                val usuario by perfilViewModel.usuario.collectAsState()
                DetalleMetaScreen(
                    metaId          = metaId,
                    userId          = usuario?.id ?: 0,
                    onRegistrarPago = { id -> navController.navigate(Rutas.pago(id)) }
                )
            }

            composable(
                route     = Rutas.PAGO,
                arguments = listOf(navArgument("metaId") { type = NavType.IntType })
            ) { backStack ->
                val metaId  = backStack.arguments?.getInt("metaId") ?: 0
                val usuario by perfilViewModel.usuario.collectAsState()
                PagoScreen(
                    metaId           = metaId,
                    userId           = usuario?.id ?: 0,
                    onPagoRegistrado = { navController.popBackStack() },
                    onBack           = { navController.popBackStack() }
                )
            }
        }

        // ── Barra de navegación custom ────────────────────────────────────
        if (showBottomBar) {
            LuxuryNavBar(
                items        = navItems,
                currentRoute = currentRoute,
                modifier     = Modifier.align(Alignment.BottomCenter),
                onItemClick  = { route ->
                    navController.navigate(route) {
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}

// ─── Barra de navegación luxury ───────────────────────────────────────────────

@Composable
private fun LuxuryNavBar(
    items: List<NavItem>,
    currentRoute: String?,
    modifier: Modifier = Modifier,
    onItemClick: (String) -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 24.dp, vertical = 12.dp)
    ) {
        // Contenedor principal de la barra
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(BgCard)
                .border(1.dp, BgCardBorder, RoundedCornerShape(20.dp))
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            items.forEach { item ->
                val selected = currentRoute == item.route
                NavBarItem(
                    item     = item,
                    selected = selected,
                    onClick  = { onItemClick(item.route) }
                )
            }
        }
    }
}

@Composable
private fun NavBarItem(
    item: NavItem,
    selected: Boolean,
    onClick: () -> Unit
) {
    val bgColor by animateColorAsState(
        targetValue    = if (selected) GoldDim else Color.Transparent,
        animationSpec  = tween(250),
        label          = "bgColor"
    )
    val iconColor by animateColorAsState(
        targetValue    = if (selected) GoldLight else TextSecondary,
        animationSpec  = tween(250),
        label          = "iconColor"
    )
    val labelColor by animateColorAsState(
        targetValue    = if (selected) Gold else TextSecondary,
        animationSpec  = tween(250),
        label          = "labelColor"
    )
    val scale by animateFloatAsState(
        targetValue   = if (selected) 1f else 0.92f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label         = "scale"
    )

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(14.dp))
            .background(bgColor)
            .border(
                width  = if (selected) 1.dp else 0.dp,
                color  = if (selected) GoldDim else Color.Transparent,
                shape  = RoundedCornerShape(14.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 18.dp, vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        if (selected) {
            // Vista expandida con label
            Row(
                verticalAlignment    = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector        = item.icon,
                    contentDescription = item.label,
                    tint               = iconColor,
                    modifier           = Modifier.size(18.dp)
                )
                Text(
                    text       = item.label,
                    color      = labelColor,
                    fontSize   = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.3.sp
                )
            }
        } else {
            // Solo ícono
            Icon(
                imageVector        = item.icon,
                contentDescription = item.label,
                tint               = iconColor,
                modifier           = Modifier.size(20.dp)
            )
        }
    }
}