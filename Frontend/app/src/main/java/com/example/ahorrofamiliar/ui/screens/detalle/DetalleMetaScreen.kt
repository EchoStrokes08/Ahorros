package com.example.ahorrofamiliar.ui.screens.detalle

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.ahorrofamiliar.viewmodel.DetalleMetaViewModel
import com.example.ahorrofamiliar.viewmodel.UiState
import com.example.ahorrofamiliar.viewmodel.ViewModelFactory

// ─── Paleta de colores luxury dark ───────────────────────────────────────────
private val BgDeep        = Color(0xFF0A0A12)
private val BgCard        = Color(0xFF13131F)
private val BgCardBorder  = Color(0xFF252535)
private val Gold          = Color(0xFFC9A84C)
private val GoldLight     = Color(0xFFE4C97A)
private val GoldDim       = Color(0xFF5A4820)
private val TextPrimary   = Color(0xFFF0EAD6)
private val TextSecondary = Color(0xFF8C8A7E)
private val Emerald       = Color(0xFF2EB87A)
private val EmeraldDim    = Color(0xFF0D3D29)

@Composable
fun DetalleMetaScreen(
    metaId: Int,
    userId: Int,
    onRegistrarPago: (Int) -> Unit
) {
    val viewModel: DetalleMetaViewModel = viewModel(factory = ViewModelFactory())
    val metaState        by viewModel.meta.collectAsState()
    val pagos            by viewModel.pagos.collectAsState()
    val amigos           by viewModel.amigosDisponibles.collectAsState()
    val nombresUsuarios  by viewModel.nombresUsuarios.collectAsState()
    val miembrosAportes  by viewModel.miembrosAportes.collectAsState()

    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(metaId, userId) {
        viewModel.cargarDetalle(metaId, userId)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BgDeep)
    ) {
        when (val state = metaState) {

            // ── Loading ──────────────────────────────────────────────────────
            is UiState.Loading -> {
                val infiniteTransition = rememberInfiniteTransition(label = "pulse")
                val alpha by infiniteTransition.animateFloat(
                    initialValue = 0.3f, targetValue = 1f,
                    animationSpec = infiniteRepeatable(tween(900), RepeatMode.Reverse),
                    label = "alpha"
                )
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(color = Gold.copy(alpha = alpha), strokeWidth = 2.dp)
                        Spacer(Modifier.height(16.dp))
                        Text("Cargando meta…", color = TextSecondary, fontSize = 13.sp)
                    }
                }
            }

            // ── Error ────────────────────────────────────────────────────────
            is UiState.Error -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(32.dp)) {
                        Text("✦", fontSize = 40.sp, color = Gold)
                        Spacer(Modifier.height(12.dp))
                        Text("Algo salió mal", color = TextPrimary, fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
                        Spacer(Modifier.height(4.dp))
                        Text(state.mensaje, color = TextSecondary, fontSize = 13.sp, textAlign = TextAlign.Center)
                    }
                }
            }

            // ── Success ──────────────────────────────────────────────────────
            is UiState.Success -> {
                val meta = state.data

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 100.dp)
                ) {

                    // ── Hero header ──────────────────────────────────────────
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(300.dp)
                        ) {
                            // Imagen de fondo
                            AsyncImage(
                                model = meta.fotoUrl,
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                            // Gradiente superior oscuro
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        Brush.verticalGradient(
                                            0f to Color(0x99000000),
                                            0.35f to Color(0x22000000),
                                            1f to BgDeep
                                        )
                                    )
                            )
                            // Orbe dorado decorativo
                            Box(
                                modifier = Modifier
                                    .size(180.dp)
                                    .align(Alignment.TopEnd)
                                    .offset(x = 60.dp, y = (-40).dp)
                                    .background(
                                        Brush.radialGradient(listOf(GoldDim.copy(0.45f), Color.Transparent)),
                                        CircleShape
                                    )
                                    .blur(30.dp)
                            )
                            // Texto sobre imagen
                            Column(
                                modifier = Modifier
                                    .align(Alignment.BottomStart)
                                    .padding(start = 24.dp, bottom = 28.dp, end = 24.dp)
                            ) {
                                Text(
                                    text = "META DE AHORRO",
                                    color = Gold,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 4.sp
                                )
                                Spacer(Modifier.height(4.dp))
                                Text(
                                    text = meta.nombre,
                                    color = TextPrimary,
                                    fontSize = 30.sp,
                                    fontWeight = FontWeight.Bold,
                                    lineHeight = 34.sp
                                )
                            }
                        }
                    }

                    // ── Tarjeta de progreso principal ────────────────────────
                    item {
                        Spacer(Modifier.height(4.dp))
                        GlassCard(modifier = Modifier.padding(horizontal = 20.dp)) {
                            Column(modifier = Modifier.padding(24.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    StatColumn(label = "OBJETIVO", value = "$${meta.valorTotal}", valueColor = TextPrimary)
                                    // Separador vertical
                                    Box(
                                        modifier = Modifier
                                            .width(1.dp)
                                            .height(48.dp)
                                            .background(BgCardBorder)
                                    )
                                    StatColumn(label = "AHORRADO", value = "$${meta.totalAhorrado}", valueColor = Emerald)
                                    Box(
                                        modifier = Modifier
                                            .width(1.dp)
                                            .height(48.dp)
                                            .background(BgCardBorder)
                                    )
                                    StatColumn(
                                        label = "RESTANTE",
                                        value = "$${meta.valorTotal - meta.totalAhorrado}",
                                        valueColor = Gold
                                    )
                                }

                                Spacer(Modifier.height(24.dp))

                                // Barra de progreso custom
                                val progressAnim by animateFloatAsState(
                                    targetValue = meta.progreso,
                                    animationSpec = tween(1200, easing = FastOutSlowInEasing),
                                    label = "progress"
                                )
                                Column {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text("Progreso", color = TextSecondary, fontSize = 12.sp)
                                        Text(
                                            "${(meta.progreso * 100).toInt()}%",
                                            color = Gold,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                    Spacer(Modifier.height(8.dp))
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(6.dp)
                                            .clip(RoundedCornerShape(3.dp))
                                            .background(BgCardBorder)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth(progressAnim)
                                                .fillMaxHeight()
                                                .clip(RoundedCornerShape(3.dp))
                                                .background(
                                                    Brush.horizontalGradient(
                                                        listOf(GoldDim, Gold, GoldLight)
                                                    )
                                                )
                                        )
                                    }
                                }
                            }
                        }
                        Spacer(Modifier.height(20.dp))
                    }

                    // ── Sección: Miembros ────────────────────────────────────
                    item {
                        SectionHeader(
                            title = "Miembros & Aportes",
                            action = if (meta.idUsuario == userId) "Invitar" else null,
                            onAction = { showDialog = true }
                        )
                        Spacer(Modifier.height(12.dp))
                    }

                    items(miembrosAportes) { miembro ->
                        MemberRow(
                            nombre = miembro.nombre,
                            monto = miembro.montoTotal
                        )
                        Spacer(Modifier.height(8.dp))
                    }

                    item { Spacer(Modifier.height(20.dp)) }

                    // ── Sección: Historial de pagos ──────────────────────────
                    item {
                        SectionHeader(title = "Historial de Pagos", action = null, onAction = {})
                        Spacer(Modifier.height(12.dp))
                    }

                    if (pagos.isEmpty()) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 20.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                GlassCard {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(32.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text("◈", fontSize = 28.sp, color = GoldDim)
                                        Spacer(Modifier.height(8.dp))
                                        Text(
                                            "Sin pagos registrados",
                                            color = TextSecondary,
                                            fontSize = 14.sp,
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }
                            }
                        }
                    } else {
                        items(pagos.size) { index ->
                            val pago = pagos[index]
                            val nombreAutor = nombresUsuarios[pago.idUsuario] ?: "Usuario #${pago.idUsuario}"
                            PaymentRow(
                                nombre = nombreAutor,
                                fecha = pago.fecha ?: "—",
                                monto = pago.monto,
                                isLast = index == pagos.lastIndex
                            )
                        }
                    }
                }

                // ── FAB personalizado ────────────────────────────────────────
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(end = 24.dp, bottom = 32.dp),
                    contentAlignment = Alignment.BottomEnd
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(
                                Brush.linearGradient(listOf(GoldDim, Gold))
                            )
                            .clickable { onRegistrarPago(metaId) }
                            .padding(horizontal = 24.dp, vertical = 16.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, tint = BgDeep, modifier = Modifier.size(18.dp))
                            Text(
                                "Aportar",
                                color = BgDeep,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                letterSpacing = 0.5.sp
                            )
                        }
                    }
                }

                // ── Dialog: Invitar amigo ────────────────────────────────────
                if (showDialog) {
                    AlertDialog(
                        onDismissRequest = { showDialog = false },
                        containerColor = BgCard,
                        shape = RoundedCornerShape(24.dp),
                        title = {
                            Column {
                                Text(
                                    "✦",
                                    color = Gold,
                                    fontSize = 20.sp
                                )
                                Spacer(Modifier.height(4.dp))
                                Text(
                                    "Invitar a la Meta",
                                    color = TextPrimary,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp
                                )
                            }
                        },
                        text = {
                            Column {
                                HorizontalDivider(color = BgCardBorder, thickness = 1.dp)
                                Spacer(Modifier.height(8.dp))
                                if (amigos.isEmpty()) {
                                    Text(
                                        "No tienes amigos disponibles para invitar.",
                                        color = TextSecondary,
                                        fontSize = 14.sp
                                    )
                                } else {
                                    amigos.forEach { amigo ->
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clip(RoundedCornerShape(12.dp))
                                                .clickable {
                                                    viewModel.agregarMiembro(meta.id, amigo.id, userId)
                                                    showDialog = false
                                                }
                                                .padding(horizontal = 12.dp, vertical = 14.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                                        ) {
                                            // Avatar inicial
                                            Box(
                                                modifier = Modifier
                                                    .size(36.dp)
                                                    .clip(CircleShape)
                                                    .background(GoldDim),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text(
                                                    amigo.nombre.firstOrNull()?.uppercase() ?: "?",
                                                    color = Gold,
                                                    fontWeight = FontWeight.Bold,
                                                    fontSize = 15.sp
                                                )
                                            }
                                            Text(amigo.nombre, color = TextPrimary, fontSize = 15.sp)
                                            Spacer(Modifier.weight(1f))
                                            Icon(
                                                Icons.Default.PersonAdd,
                                                contentDescription = null,
                                                tint = Gold,
                                                modifier = Modifier.size(18.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        },
                        confirmButton = {
                            TextButton(onClick = { showDialog = false }) {
                                Text("Cerrar", color = Gold)
                            }
                        }
                    )
                }
            }
        }
    }
}

// ── Componentes auxiliares ────────────────────────────────────────────────────

@Composable
private fun GlassCard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(BgCard)
            .border(1.dp, BgCardBorder, RoundedCornerShape(20.dp))
    ) {
        content()
    }
}

@Composable
private fun StatColumn(label: String, value: String, valueColor: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            label,
            color = TextSecondary,
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 2.sp
        )
        Spacer(Modifier.height(6.dp))
        Text(
            value,
            color = valueColor,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun SectionHeader(title: String, action: String?, onAction: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .width(3.dp)
                    .height(18.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(Gold)
            )
            Spacer(Modifier.width(10.dp))
            Text(title, color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 17.sp)
        }
        if (action != null) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .border(1.dp, GoldDim, RoundedCornerShape(8.dp))
                    .clickable { onAction() }
                    .padding(horizontal = 14.dp, vertical = 6.dp)
            ) {
                Text(action, color = Gold, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
private fun MemberRow(nombre: String, montoTotal: Double) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(BgCard)
            .border(1.dp, BgCardBorder, RoundedCornerShape(16.dp))
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Avatar con inicial
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(
                    Brush.linearGradient(listOf(GoldDim, Color(0xFF3A2E0A)))
                )
                .border(1.dp, GoldDim, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                nombre.firstOrNull()?.uppercase() ?: "?",
                color = GoldLight,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }
        Text(nombre, color = TextPrimary, fontWeight = FontWeight.Medium, fontSize = 15.sp, modifier = Modifier.weight(1f))
        // Badge de monto
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(EmeraldDim)
                .padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
            Text("$${montoTotal}", color = Emerald, fontWeight = FontWeight.Bold, fontSize = 13.sp)
        }
    }
}

@Composable
private fun PaymentRow(nombre: String, fecha: String, monto: Double, isLast: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
    ) {
        // Línea de timeline
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(20.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(Gold)
            )
            if (!isLast) {
                Box(
                    modifier = Modifier
                        .width(1.dp)
                        .height(56.dp)
                        .background(
                            Brush.verticalGradient(listOf(GoldDim, Color.Transparent))
                        )
                )
            }
        }
        Spacer(Modifier.width(12.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(BgCard)
                .border(1.dp, BgCardBorder, RoundedCornerShape(16.dp))
                .padding(horizontal = 16.dp, vertical = 14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(nombre, color = TextPrimary, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                    Spacer(Modifier.height(2.dp))
                    Text(fecha, color = TextSecondary, fontSize = 11.sp)
                }
                Text(
                    "+$${monto}",
                    color = Emerald,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }
    }
    if (!isLast) Spacer(Modifier.height(0.dp))
}