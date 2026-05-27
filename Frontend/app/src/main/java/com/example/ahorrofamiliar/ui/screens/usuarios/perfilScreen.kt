package com.example.ahorrofamiliar.ui.screens.usuarios

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ahorrofamiliar.ui.viewmodel.PerfilViewModel

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
fun PerfilScreen(
    viewModel: PerfilViewModel,
    idDispositivo: String,
    onNavigateToLista: () -> Unit
) {
    val usuario by viewModel.usuario.collectAsState()
    var nombre  by remember { mutableStateOf("") }
    var guardado by remember { mutableStateOf(false) }

    val esNuevoUsuario = usuario == null
    val isValid = nombre.isNotBlank()

    LaunchedEffect(Unit)    { viewModel.cargarUsuario(idDispositivo) }
    LaunchedEffect(usuario) { usuario?.let { nombre = it.nombre } }

    // Resetear feedback de guardado
    LaunchedEffect(guardado) {
        if (guardado) {
            kotlinx.coroutines.delay(2000)
            guardado = false
        }
    }

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
                .size(320.dp)
                .align(Alignment.TopCenter)
                .offset(y = (-120).dp)
                .background(
                    Brush.radialGradient(listOf(GoldDim.copy(alpha = 0.2f), Color.Transparent)),
                    CircleShape
                )
        )
        Box(
            modifier = Modifier
                .size(180.dp)
                .align(Alignment.BottomStart)
                .offset(x = (-50).dp, y = 50.dp)
                .background(
                    Brush.radialGradient(listOf(Color(0xFF0D1A2E).copy(alpha = 0.8f), Color.Transparent)),
                    CircleShape
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(tween(600)) + slideInVertically(tween(600)) { -30 }
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {

                    // ── Avatar con inicial ────────────────────────────────
                    Box(
                        modifier = Modifier
                            .size(90.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.radialGradient(listOf(GoldDim.copy(0.6f), Color(0xFF0A0A12)))
                            )
                            .border(1.dp, GoldDim, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        // Resplandor interior
                        Box(
                            modifier = Modifier
                                .size(70.dp)
                                .clip(CircleShape)
                                .background(
                                    Brush.radialGradient(listOf(GoldDim.copy(0.4f), Color.Transparent))
                                )
                        )
                        Text(
                            text = nombre.firstOrNull()?.uppercase() ?: "?",
                            color = GoldLight,
                            fontSize = 34.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(Modifier.height(24.dp))

                    // ── Encabezado ────────────────────────────────────────
                    Text(
                        if (esNuevoUsuario) "BIENVENIDO" else "MI PERFIL",
                        color = Gold,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 4.sp
                    )
                    Spacer(Modifier.height(6.dp))
                    Text(
                        if (esNuevoUsuario) "Crea tu perfil" else nombre.ifBlank { "Editar perfil" },
                        color = TextPrimary,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )

                    Spacer(Modifier.height(6.dp))

                    // ID de dispositivo
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(BgCard)
                            .border(1.dp, BgCardBorder, RoundedCornerShape(8.dp))
                            .padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(Modifier.size(5.dp).clip(CircleShape).background(GoldDim))
                        Text(
                            "ID: ${idDispositivo.take(12)}…",
                            color = TextHint,
                            fontSize = 10.sp,
                            letterSpacing = 1.sp
                        )
                    }

                    Spacer(Modifier.height(48.dp))

                    // ── Separador decorativo ──────────────────────────────
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.6f)
                            .height(1.dp)
                            .background(
                                Brush.horizontalGradient(
                                    listOf(Color.Transparent, Gold.copy(0.3f), Color.Transparent)
                                )
                            )
                    )

                    Spacer(Modifier.height(40.dp))

                    // ── Campo de nombre ───────────────────────────────────
                    NombreField(
                        value = nombre,
                        onValueChange = { nombre = it },
                        esNuevo = esNuevoUsuario
                    )

                    Spacer(Modifier.height(40.dp))

                    // ── Botón guardar ─────────────────────────────────────
                    GuardarButton(
                        enabled = isValid,
                        guardado = guardado,
                        esNuevo = esNuevoUsuario,
                        onClick = {
                            if (isValid) {
                                if (esNuevoUsuario) {
                                    viewModel.crearUsuario(nombre, idDispositivo)
                                } else {
                                    viewModel.editarUsuario(nombre)
                                }
                                guardado = true
                            }
                        }
                    )

                    // ── Ir a lista (si ya existe el usuario) ──────────────
                    AnimatedVisibility(
                        visible = !esNuevoUsuario,
                        enter = fadeIn(tween(400)) + expandVertically(tween(400)),
                        exit  = fadeOut(tween(300)) + shrinkVertically(tween(300))
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Spacer(Modifier.height(16.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .border(1.dp, BgCardBorder, RoundedCornerShape(12.dp))
                                    .clickable { onNavigateToLista() }
                                    .padding(horizontal = 28.dp, vertical = 12.dp)
                            ) {
                                Text(
                                    "Ir a mis metas  →",
                                    color = TextSecondary,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// ── Campo de nombre elegante ──────────────────────────────────────────────────

@Composable
private fun NombreField(value: String, onValueChange: (String) -> Unit, esNuevo: Boolean) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    val lineColor by animateColorAsState(
        targetValue = if (isFocused) Gold else BgCardBorder,
        animationSpec = tween(300),
        label = "line"
    )
    val labelColor by animateColorAsState(
        targetValue = if (isFocused) Gold else TextSecondary,
        animationSpec = tween(300),
        label = "label"
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        // Etiqueta con número de paso
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(26.dp)
                    .clip(CircleShape)
                    .background(if (isFocused) GoldDim else BgCard)
                    .border(1.dp, if (isFocused) GoldDim else BgCardBorder, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text("01", color = if (isFocused) GoldLight else TextSecondary, fontSize = 9.sp, fontWeight = FontWeight.Bold)
            }
            Text(
                if (esNuevo) "¿Cómo te llamas?" else "Tu nombre",
                color = labelColor,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 0.3.sp
            )
        }

        Spacer(Modifier.height(12.dp))

        // Campo con línea inferior
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .drawBehind {
                    val y = size.height
                    drawLine(
                        color = lineColor,
                        start = Offset(0f, y),
                        end   = Offset(size.width, y),
                        strokeWidth = if (isFocused) 1.5f else 1f
                    )
                }
                .padding(bottom = 10.dp)
        ) {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                textStyle = TextStyle(
                    color = TextPrimary,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.SemiBold
                ),
                singleLine = true,
                cursorBrush = SolidColor(Gold),
                interactionSource = interactionSource,
                modifier = Modifier.fillMaxWidth(),
                decorationBox = { inner ->
                    Box {
                        if (value.isEmpty()) {
                            Text(
                                "Tu nombre aquí…",
                                color = TextHint,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Light
                            )
                        }
                        inner()
                    }
                }
            )
        }
    }
}

// ── Botón guardar ─────────────────────────────────────────────────────────────

@Composable
private fun GuardarButton(
    enabled: Boolean,
    guardado: Boolean,
    esNuevo: Boolean,
    onClick: () -> Unit
) {
    val bgBrush = when {
        guardado -> Brush.linearGradient(listOf(EmeraldDim, Emerald.copy(alpha = 0.8f)))
        enabled  -> Brush.linearGradient(listOf(GoldDim, Gold, GoldLight))
        else     -> Brush.linearGradient(listOf(Color(0xFF1E1E2A), Color(0xFF1E1E2A)))
    }
    val textColor = if (enabled || guardado) BgDeep else TextHint

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(bgBrush)
            .then(if (enabled && !guardado) Modifier.clickable { onClick() } else Modifier),
        contentAlignment = Alignment.Center
    ) {
        AnimatedContent(
            targetState = guardado,
            transitionSpec = { fadeIn(tween(250)) togetherWith fadeOut(tween(250)) },
            label = "btnState"
        ) { isGuardado ->
            if (isGuardado) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(BgDeep.copy(alpha = 0.25f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Check, null, tint = BgDeep, modifier = Modifier.size(14.dp))
                    }
                    Text("¡Guardado!", color = BgDeep, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }
            } else {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (enabled) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(BgDeep.copy(alpha = 0.25f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Check, null, tint = BgDeep, modifier = Modifier.size(14.dp))
                        }
                    }
                    Text(
                        when {
                            !enabled -> "Ingresa tu nombre"
                            esNuevo  -> "Crear perfil"
                            else     -> "Guardar cambios"
                        },
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