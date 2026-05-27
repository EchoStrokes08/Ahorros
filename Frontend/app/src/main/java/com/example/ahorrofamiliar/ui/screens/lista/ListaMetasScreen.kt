package com.example.ahorrofamiliar.ui.screens.lista

import androidx.compose.animation.core.*
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.ahorrofamiliar.data.model.Meta
import com.example.ahorrofamiliar.viewmodel.ListaMetasViewModel
import com.example.ahorrofamiliar.viewmodel.UiState
import com.example.ahorrofamiliar.viewmodel.ViewModelFactory

// ─── Paleta compartida ────────────────────────────────────────────────────────
private val BgDeep       = Color(0xFF0A0A12)
private val BgCard       = Color(0xFF13131F)
private val BgCardBorder = Color(0xFF252535)
private val Gold         = Color(0xFFC9A84C)
private val GoldLight    = Color(0xFFE4C97A)
private val GoldDim      = Color(0xFF5A4820)
private val TextPrimary  = Color(0xFFF0EAD6)
private val TextSecondary= Color(0xFF8C8A7E)
private val Emerald      = Color(0xFF2EB87A)
private val EmeraldDim   = Color(0xFF0D3D29)

@Composable
fun ListaMetasScreen(
    userId: Int,
    onMetaClick: (Int) -> Unit,
    onCrearMetaClick: () -> Unit
) {
    val viewModel: ListaMetasViewModel = viewModel(factory = ViewModelFactory())
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(userId) { viewModel.cargarMetas(userId) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BgDeep)
    ) {
        // ── Orbe decorativo superior ──────────────────────────────────────
        Box(
            modifier = Modifier
                .size(260.dp)
                .align(Alignment.TopEnd)
                .offset(x = 80.dp, y = (-50).dp)
                .background(
                    Brush.radialGradient(listOf(GoldDim.copy(alpha = 0.3f), Color.Transparent)),
                    CircleShape
                )
        )

        Column(modifier = Modifier.fillMaxSize()) {

            // ── Encabezado ────────────────────────────────────────────────
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(start = 24.dp, end = 24.dp, top = 24.dp, bottom = 20.dp)
            ) {
                Text(
                    "MIS METAS",
                    color = Gold,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 4.sp
                )
                Spacer(Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(
                        "Ahorro\nFamiliar",
                        color = TextPrimary,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 36.sp
                    )
                    // Resumen rápido si hay datos
                    if (uiState is UiState.Success) {
                        val metas = (uiState as UiState.Success<List<Meta>>).data
                        if (metas.isNotEmpty()) {
                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    "${metas.size}",
                                    color = Gold,
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    "metas activas",
                                    color = TextSecondary,
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }
                }

                Spacer(Modifier.height(20.dp))
                // Separador decorativo
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(
                            Brush.horizontalGradient(
                                listOf(Gold.copy(alpha = 0.4f), Color.Transparent)
                            )
                        )
                )
            }

            // ── Contenido según estado ────────────────────────────────────
            Box(modifier = Modifier.weight(1f)) {
                when (val estado = uiState) {

                    // Loading
                    is UiState.Loading -> {
                        val infiniteTransition = rememberInfiniteTransition(label = "pulse")
                        val alpha by infiniteTransition.animateFloat(
                            initialValue = 0.3f, targetValue = 1f,
                            animationSpec = infiniteRepeatable(tween(900), RepeatMode.Reverse),
                            label = "alpha"
                        )
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                CircularProgressIndicator(
                                    color = Gold.copy(alpha = alpha),
                                    strokeWidth = 2.dp,
                                    modifier = Modifier.size(36.dp)
                                )
                                Spacer(Modifier.height(16.dp))
                                Text("Cargando metas…", color = TextSecondary, fontSize = 13.sp)
                            }
                        }
                    }

                    // Error
                    is UiState.Error -> {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.padding(32.dp)
                            ) {
                                Text("✦", fontSize = 36.sp, color = GoldDim)
                                Spacer(Modifier.height(12.dp))
                                Text(
                                    "No se pudieron cargar\nlas metas",
                                    color = TextPrimary,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 18.sp,
                                    textAlign = TextAlign.Center
                                )
                                Spacer(Modifier.height(6.dp))
                                Text(
                                    estado.mensaje,
                                    color = TextSecondary,
                                    fontSize = 13.sp,
                                    textAlign = TextAlign.Center
                                )
                                Spacer(Modifier.height(24.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(12.dp))
                                        .border(1.dp, GoldDim, RoundedCornerShape(12.dp))
                                        .clickable { viewModel.cargarMetas(userId) }
                                        .padding(horizontal = 28.dp, vertical = 12.dp)
                                ) {
                                    Text(
                                        "Reintentar",
                                        color = Gold,
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 14.sp
                                    )
                                }
                            }
                        }
                    }

                    // Success
                    is UiState.Success -> {
                        if (estado.data.isEmpty()) {
                            // Estado vacío
                            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.padding(32.dp)
                                ) {
                                    // Ícono decorativo
                                    Box(
                                        modifier = Modifier
                                            .size(80.dp)
                                            .clip(CircleShape)
                                            .background(
                                                Brush.radialGradient(listOf(GoldDim.copy(0.4f), Color.Transparent))
                                            )
                                            .border(1.dp, GoldDim, CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text("◈", fontSize = 32.sp, color = Gold)
                                    }
                                    Spacer(Modifier.height(20.dp))
                                    Text(
                                        "Sin metas todavía",
                                        color = TextPrimary,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 20.sp
                                    )
                                    Spacer(Modifier.height(6.dp))
                                    Text(
                                        "Crea tu primera meta y\nempieza a ahorrar",
                                        color = TextSecondary,
                                        fontSize = 14.sp,
                                        textAlign = TextAlign.Center,
                                        lineHeight = 20.sp
                                    )
                                }
                            }
                        } else {
                            LazyColumn(
                                contentPadding = PaddingValues(
                                    start = 20.dp,
                                    end = 20.dp,
                                    top = 4.dp,
                                    bottom = 100.dp
                                ),
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                itemsIndexed(estado.data) { index, meta ->
                                    var visible by remember { mutableStateOf(false) }
                                    LaunchedEffect(Unit) {
                                        kotlinx.coroutines.delay(index * 80L)
                                        visible = true
                                    }
                                    AnimatedVisibility(
                                        visible = visible,
                                        enter = fadeIn(tween(400)) + slideInVertically(tween(400)) { 40 }
                                    ) {
                                        TarjetaMeta(meta = meta, onClick = { onMetaClick(meta.id) })
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // ── FAB personalizado ─────────────────────────────────────────────
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 24.dp, bottom = 32.dp)
        ) {
            // Resplandor dorado detrás
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .align(Alignment.Center)
                    .background(
                        Brush.radialGradient(listOf(GoldDim.copy(alpha = 0.5f), Color.Transparent)),
                        CircleShape
                    )
            )
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Brush.linearGradient(listOf(GoldDim, Gold)))
                    .clickable { onCrearMetaClick() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Crear Meta",
                    tint = BgDeep,
                    modifier = Modifier.size(22.dp)
                )
            }
        }
    }
}

// ── Tarjeta de meta ───────────────────────────────────────────────────────────

@Composable
fun TarjetaMeta(meta: Meta, onClick: () -> Unit) {
    val progressAnim by animateFloatAsState(
        targetValue = meta.progreso,
        animationSpec = tween(1000, easing = FastOutSlowInEasing),
        label = "progress"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(BgCard)
            .border(1.dp, BgCardBorder, RoundedCornerShape(24.dp))
            .clickable { onClick() }
    ) {
        Column {
            // ── Imagen con overlay ────────────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
            ) {
                AsyncImage(
                    model = meta.fotoUrl,
                    contentDescription = meta.nombre,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                // Gradiente hacia abajo
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                0f to Color(0x33000000),
                                0.6f to Color(0x11000000),
                                1f to BgCard
                            )
                        )
                )
                // Badge de progreso en esquina superior derecha
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(12.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(BgDeep.copy(alpha = 0.85f))
                        .border(1.dp, GoldDim, RoundedCornerShape(10.dp))
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    Text(
                        "${(meta.progreso * 100).toInt()}%",
                        color = Gold,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // ── Info de la meta ───────────────────────────────────────────
            Column(modifier = Modifier.padding(start = 18.dp, end = 18.dp, top = 14.dp, bottom = 18.dp)) {
                Text(
                    meta.nombre,
                    color = TextPrimary,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(Modifier.height(12.dp))

                // Fila de stats
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    MiniStat(
                        label = "OBJETIVO",
                        value = "$${ meta.valorTotal }",
                        modifier = Modifier.weight(1f)
                    )
                    // Divisor
                    Box(
                        modifier = Modifier
                            .width(1.dp)
                            .height(36.dp)
                            .background(BgCardBorder)
                            .align(Alignment.CenterVertically)
                    )
                    MiniStat(
                        label = "AHORRADO",
                        value = "$${ meta.totalAhorrado }",
                        valueColor = Emerald,
                        modifier = Modifier.weight(1f)
                    )
                    Box(
                        modifier = Modifier
                            .width(1.dp)
                            .height(36.dp)
                            .background(BgCardBorder)
                            .align(Alignment.CenterVertically)
                    )
                    MiniStat(
                        label = "FALTA",
                        value = "${ meta.porcentajeFaltante }%",
                        valueColor = Gold,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(Modifier.height(14.dp))

                // Barra de progreso elegante
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(BgCardBorder)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(progressAnim)
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(2.dp))
                            .background(
                                Brush.horizontalGradient(listOf(GoldDim, Gold, GoldLight))
                            )
                    )
                    // Punto brillante al final de la barra
                    if (progressAnim > 0.02f) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(progressAnim)
                                .fillMaxHeight()
                                .wrapContentWidth(Alignment.End)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .offset(y = (-2).dp)
                                    .clip(CircleShape)
                                    .background(GoldLight)
                            )
                        }
                    }
                }
            }
        }
    }
}

// ── Mini stat ─────────────────────────────────────────────────────────────────

@Composable
private fun MiniStat(
    label: String,
    value: String,
    valueColor: Color = TextPrimary,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            label,
            color = TextSecondary,
            fontSize = 8.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.5.sp
        )
        Spacer(Modifier.height(4.dp))
        Text(
            value,
            color = valueColor,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}