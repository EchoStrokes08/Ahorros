package com.example.ahorrofamiliar.ui.screens.pago

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ahorrofamiliar.viewmodel.PagoViewModel
import com.example.ahorrofamiliar.viewmodel.ViewModelFactory

// ─── Paleta compartida ────────────────────────────────────────────────────────
private val BgDeep        = Color(0xFF0A0A12)
private val BgCard        = Color(0xFF13131F)
private val BgCardBorder  = Color(0xFF252535)
private val Gold          = Color(0xFFC9A84C)
private val GoldLight     = Color(0xFFE4C97A)
private val GoldDim       = Color(0xFF5A4820)
private val TextPrimary   = Color(0xFFF0EAD6)
private val TextSecondary = Color(0xFF8C8A7E)
private val TextHint      = Color(0xFF4A4A5A)
private val Emerald       = Color(0xFF2EB87A)
private val EmeraldDim    = Color(0xFF0D3D29)

@Composable
fun PagoScreen(
    metaId: Int,
    userId: Int,
    onPagoRegistrado: () -> Unit,
    onBack: (() -> Unit)? = null
) {
    val viewModel: PagoViewModel = viewModel(factory = ViewModelFactory())
    var monto    by remember { mutableStateOf("") }
    var cargando by remember { mutableStateOf(false) }
    var exito    by remember { mutableStateOf(false) }

    val montoDouble = monto.toDoubleOrNull() ?: 0.0
    val isValid = monto.isNotBlank() && montoDouble > 0

    // Animación de entrada
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible = true }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BgDeep)
    ) {
        // ── Orbes decorativos ─────────────────────────────────────────────
        Box(
            modifier = Modifier
                .size(280.dp)
                .align(Alignment.TopEnd)
                .offset(x = 90.dp, y = (-70).dp)
                .background(
                    Brush.radialGradient(listOf(GoldDim.copy(alpha = 0.22f), Color.Transparent)),
                    CircleShape
                )
        )
        Box(
            modifier = Modifier
                .size(200.dp)
                .align(Alignment.BottomStart)
                .offset(x = (-60).dp, y = 60.dp)
                .background(
                    Brush.radialGradient(listOf(Color(0xFF0D2030).copy(alpha = 0.7f), Color.Transparent)),
                    CircleShape
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) {
            // ── Top bar ───────────────────────────────────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                if (onBack != null) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(BgCard)
                            .border(1.dp, BgCardBorder, CircleShape)
                            .clickable { onBack() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Volver",
                            tint = TextSecondary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
                Column {
                    Text(
                        "APORTE",
                        color = Gold,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 4.sp
                    )
                    Text(
                        "Registrar pago",
                        color = TextPrimary,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // ── Separador ─────────────────────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .height(1.dp)
                    .background(
                        Brush.horizontalGradient(listOf(Gold.copy(alpha = 0.35f), Color.Transparent))
                    )
            )

            Spacer(Modifier.height(36.dp))

            // ── Contenido central ─────────────────────────────────────────
            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(tween(500)) + slideInVertically(tween(500)) { 40 }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    // ── Badge de meta ─────────────────────────────────────
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(BgCard)
                            .border(1.dp, BgCardBorder, RoundedCornerShape(12.dp))
                            .padding(horizontal = 16.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(Gold)
                        )
                        Text(
                            "META  #$metaId",
                            color = TextSecondary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            letterSpacing = 2.sp
                        )
                    }

                    Spacer(Modifier.height(40.dp))

                    // ── Input de monto grande ─────────────────────────────
                    MontoInput(
                        value = monto,
                        onValueChange = { if (it.all { c -> c.isDigit() || c == '.' }) monto = it }
                    )

                    Spacer(Modifier.height(12.dp))

                    // ── Chips de montos rápidos ───────────────────────────
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("10000", "50000", "100000", "200000").forEach { sugerido ->
                            val seleccionado = monto == sugerido
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (seleccionado) GoldDim else BgCard)
                                    .border(
                                        1.dp,
                                        if (seleccionado) Gold else BgCardBorder,
                                        RoundedCornerShape(8.dp)
                                    )
                                    .clickable { monto = sugerido }
                                    .padding(horizontal = 10.dp, vertical = 7.dp)
                            ) {
                                Text(
                                    "$$sugerido",
                                    color = if (seleccionado) GoldLight else TextSecondary,
                                    fontSize = 11.sp,
                                    fontWeight = if (seleccionado) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        }
                    }

                    Spacer(Modifier.height(48.dp))

                    // ── Resumen del aporte ────────────────────────────────
                    AnimatedVisibility(
                        visible = isValid,
                        enter = fadeIn(tween(300)) + expandVertically(tween(300)),
                        exit  = fadeOut(tween(200)) + shrinkVertically(tween(200))
                    ) {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(EmeraldDim.copy(alpha = 0.5f))
                                    .border(1.dp, Emerald.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
                                    .padding(horizontal = 20.dp, vertical = 14.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text("Total a aportar", color = TextSecondary, fontSize = 11.sp)
                                        Spacer(Modifier.height(2.dp))
                                        Text(
                                            "$$montoDouble",
                                            color = Emerald,
                                            fontSize = 22.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                    Box(
                                        modifier = Modifier
                                            .size(36.dp)
                                            .clip(CircleShape)
                                            .background(Emerald.copy(alpha = 0.15f))
                                            .border(1.dp, Emerald.copy(alpha = 0.4f), CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text("✓", color = Emerald, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                            Spacer(Modifier.height(20.dp))
                        }
                    }

                    // ── Botón de confirmar ────────────────────────────────
                    ConfirmarButton(
                        enabled = isValid && !cargando,
                        cargando = cargando,
                        onClick = {
                            if (isValid && !cargando) {
                                cargando = true
                                viewModel.registrarPago(metaId, userId, montoDouble) {
                                    cargando = false
                                    exito = true
                                    onPagoRegistrado()
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}

// ── Input de monto grande centrado ────────────────────────────────────────────

@Composable
private fun MontoInput(value: String, onValueChange: (String) -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    val lineColor by animateColorAsState(
        targetValue = if (isFocused) Gold else BgCardBorder,
        animationSpec = tween(300),
        label = "lineColor"
    )
    val prefixColor by animateColorAsState(
        targetValue = if (value.isNotEmpty()) GoldLight else TextHint,
        animationSpec = tween(300),
        label = "prefix"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            "¿Cuánto quieres aportar?",
            color = TextSecondary,
            fontSize = 13.sp,
            letterSpacing = 0.5.sp
        )
        Spacer(Modifier.height(20.dp))

        // Campo centrado con signo grande
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .drawBehind {
                    val y = size.height + 8.dp.toPx()
                    drawLine(
                        color = lineColor,
                        start = Offset(size.width * 0.1f, y),
                        end   = Offset(size.width * 0.9f, y),
                        strokeWidth = if (isFocused) 1.5f else 1f
                    )
                }
                .padding(bottom = 16.dp)
        ) {
            Text(
                "$",
                color = prefixColor,
                fontSize = 36.sp,
                fontWeight = FontWeight.Light,
                modifier = Modifier.padding(end = 4.dp, top = 8.dp)
            )
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                textStyle = TextStyle(
                    color = TextPrimary,
                    fontSize = 52.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                cursorBrush = SolidColor(Gold),
                interactionSource = interactionSource,
                modifier = Modifier.widthIn(min = 60.dp, max = 280.dp),
                decorationBox = { inner ->
                    Box {
                        if (value.isEmpty()) {
                            Text(
                                "0",
                                color = TextHint,
                                fontSize = 52.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                        }
                        inner()
                    }
                }
            )
        }
    }
}

// ── Botón confirmar ───────────────────────────────────────────────────────────

@Composable
private fun ConfirmarButton(enabled: Boolean, cargando: Boolean, onClick: () -> Unit) {
    val bgBrush = if (enabled) {
        Brush.linearGradient(listOf(GoldDim, Gold, GoldLight))
    } else {
        Brush.linearGradient(listOf(Color(0xFF1E1E2A), Color(0xFF1E1E2A)))
    }
    val textColor = if (enabled) BgDeep else TextHint

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(bgBrush)
            .then(if (enabled && !cargando) Modifier.clickable { onClick() } else Modifier),
        contentAlignment = Alignment.Center
    ) {
        AnimatedContent(
            targetState = cargando,
            transitionSpec = {
                fadeIn(tween(200)) togetherWith fadeOut(tween(200))
            },
            label = "buttonContent"
        ) { loading ->
            if (loading) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    CircularProgressIndicator(
                        color = BgDeep,
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        "Procesando…",
                        color = BgDeep,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }
            } else {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    if (enabled) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(BgDeep.copy(alpha = 0.25f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.Check,
                                contentDescription = null,
                                tint = BgDeep,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                    Text(
                        if (enabled) "Confirmar Aporte" else "Ingresa un monto",
                        color = textColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        letterSpacing = 0.5.sp
                    )
                }
            }
        }
    }
}