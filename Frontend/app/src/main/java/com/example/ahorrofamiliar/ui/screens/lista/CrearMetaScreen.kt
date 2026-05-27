package com.example.ahorrofamiliar.ui.screens.lista

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.ahorrofamiliar.viewmodel.CrearMetaViewModel
import com.example.ahorrofamiliar.viewmodel.ViewModelFactory

// ─── Paleta compartida (misma que DetalleMetaScreen) ─────────────────────────
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
private val ErrorRed      = Color(0xFFE05252)

@Composable
fun CrearMetaScreen(
    userId: Int,
    onMetaCreada: () -> Unit,
    onBack: (() -> Unit)? = null
) {
    val viewModel: CrearMetaViewModel = viewModel(factory = ViewModelFactory())

    var titulo  by remember { mutableStateOf("") }
    var monto   by remember { mutableStateOf("") }
    var imagen  by remember { mutableStateOf("") }

    val isFormValid = titulo.isNotBlank() && monto.isNotBlank() && monto.toDoubleOrNull() != null
    val montoValido = monto.isEmpty() || monto.toDoubleOrNull() != null

    // Animaciones de entrada escalonadas
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible = true }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BgDeep)
    ) {
        // ── Orbes decorativos de fondo ────────────────────────────────────
        Box(
            modifier = Modifier
                .size(300.dp)
                .offset(x = (-80).dp, y = (-60).dp)
                .background(
                    Brush.radialGradient(listOf(GoldDim.copy(alpha = 0.25f), Color.Transparent)),
                    CircleShape
                )
        )
        Box(
            modifier = Modifier
                .size(200.dp)
                .align(Alignment.BottomEnd)
                .offset(x = 60.dp, y = 60.dp)
                .background(
                    Brush.radialGradient(listOf(Color(0xFF1A2A3A).copy(alpha = 0.6f), Color.Transparent)),
                    CircleShape
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 40.dp)
        ) {

            // ── Barra superior ────────────────────────────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
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
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver", tint = TextSecondary, modifier = Modifier.size(18.dp))
                    }
                }
                Column {
                    Text(
                        "NUEVA META",
                        color = Gold,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 4.sp
                    )
                    Text(
                        "Definir objetivo",
                        color = TextPrimary,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

            // ── Preview de la meta ────────────────────────────────────────
            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(tween(600)) + slideInVertically(tween(600)) { -20 }
            ) {
                PreviewCard(
                    titulo = titulo.ifBlank { "Nombre de tu meta" },
                    monto = monto.toDoubleOrNull() ?: 0.0,
                    imagenUrl = imagen
                )
            }

            Spacer(Modifier.height(32.dp))

            // ── Formulario ────────────────────────────────────────────────
            Column(
                modifier = Modifier.padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(0.dp)
            ) {

                AnimatedVisibility(
                    visible = visible,
                    enter = fadeIn(tween(500, delayMillis = 100)) + slideInVertically(tween(500, delayMillis = 100)) { 30 }
                ) {
                    ElegantField(
                        step = "01",
                        label = "¿Qué quieres lograr?",
                        placeholder = "Ej. Viaje a Europa, Laptop nueva…",
                        value = titulo,
                        onValueChange = { titulo = it }
                    )
                }

                Spacer(Modifier.height(24.dp))

                AnimatedVisibility(
                    visible = visible,
                    enter = fadeIn(tween(500, delayMillis = 200)) + slideInVertically(tween(500, delayMillis = 200)) { 30 }
                ) {
                    ElegantField(
                        step = "02",
                        label = "Monto objetivo",
                        placeholder = "0.00",
                        value = monto,
                        onValueChange = { if (it.isEmpty() || it.matches(Regex("^\\d*\\.?\\d*$"))) monto = it },
                        keyboardType = KeyboardType.Decimal,
                        prefix = "$",
                        isError = !montoValido,
                        errorMessage = "Ingresa un número válido"
                    )
                }

                Spacer(Modifier.height(24.dp))

                AnimatedVisibility(
                    visible = visible,
                    enter = fadeIn(tween(500, delayMillis = 300)) + slideInVertically(tween(500, delayMillis = 300)) { 30 }
                ) {
                    ElegantField(
                        step = "03",
                        label = "Imagen de inspiración",
                        placeholder = "https://…",
                        value = imagen,
                        onValueChange = { imagen = it },
                        keyboardType = KeyboardType.Uri,
                        hint = "URL de la imagen para tu meta (opcional)"
                    )
                }

                Spacer(Modifier.height(40.dp))

                // ── Botón de creación ─────────────────────────────────────
                AnimatedVisibility(
                    visible = visible,
                    enter = fadeIn(tween(500, delayMillis = 400)) + slideInVertically(tween(500, delayMillis = 400)) { 30 }
                ) {
                    CreateButton(
                        enabled = isFormValid,
                        onClick = {
                            val montoDouble = monto.toDoubleOrNull() ?: 0.0
                            val urlFinal = imagen.ifBlank {
                                "https://cdn-icons-png.flaticon.com/512/1077/1077114.png"
                            }
                            viewModel.crearMeta(titulo, montoDouble, urlFinal, userId) {
                                onMetaCreada()
                            }
                        }
                    )
                }
            }
        }
    }
}

// ── Preview card ──────────────────────────────────────────────────────────────

@Composable
private fun PreviewCard(titulo: String, monto: Double, imagenUrl: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .height(160.dp)
            .clip(RoundedCornerShape(24.dp))
            .border(1.dp, BgCardBorder, RoundedCornerShape(24.dp))
    ) {
        // Imagen de fondo
        if (imagenUrl.isNotBlank()) {
            AsyncImage(
                model = imagenUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.linearGradient(
                            listOf(Color(0xFF1A1A2E), Color(0xFF0D1117))
                        )
                    )
            )
        }

        // Overlay gradiente
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.linearGradient(
                        0f to Color(0xCC0A0A12),
                        0.5f to Color(0x880A0A12),
                        1f to Color(0x220A0A12),
                        start = Offset.Zero,
                        end = Offset(Float.POSITIVE_INFINITY, 0f)
                    )
                )
        )

        // Etiqueta PREVIEW
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(12.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(GoldDim.copy(alpha = 0.8f))
                .padding(horizontal = 8.dp, vertical = 3.dp)
        ) {
            Text("PREVIEW", color = GoldLight, fontSize = 8.sp, fontWeight = FontWeight.Bold, letterSpacing = 2.sp)
        }

        // Contenido de texto
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(20.dp)
        ) {
            Text(
                "META",
                color = Gold.copy(alpha = 0.7f),
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 3.sp
            )
            Spacer(Modifier.height(2.dp))
            Text(
                titulo,
                color = TextPrimary,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            if (monto > 0) {
                Spacer(Modifier.height(4.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(EmeraldDim)
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text("$$monto", color = Emerald, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                    Text("objetivo", color = TextSecondary, fontSize = 11.sp)
                }
            }
        }
    }
}

// ── Campo elegante personalizado ──────────────────────────────────────────────

@Composable
private fun ElegantField(
    step: String,
    label: String,
    placeholder: String,
    value: String,
    onValueChange: (String) -> Unit,
    keyboardType: KeyboardType = KeyboardType.Text,
    prefix: String? = null,
    hint: String? = null,
    isError: Boolean = false,
    errorMessage: String? = null
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    val lineColor by animateColorAsState(
        targetValue = when {
            isError   -> ErrorRed
            isFocused -> Gold
            else      -> BgCardBorder
        },
        animationSpec = tween(300),
        label = "lineColor"
    )
    val labelColor by animateColorAsState(
        targetValue = when {
            isError   -> ErrorRed
            isFocused -> Gold
            else      -> TextSecondary
        },
        animationSpec = tween(300),
        label = "labelColor"
    )

    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Número de paso
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(if (isFocused) GoldDim else BgCard)
                    .border(1.dp, if (isFocused) GoldDim else BgCardBorder, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(step, color = if (isFocused) GoldLight else TextSecondary, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            }
            Text(label, color = labelColor, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, letterSpacing = 0.3.sp)
        }

        Spacer(Modifier.height(10.dp))

        // Campo de entrada con línea inferior
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .drawBehind {
                    val y = size.height
                    drawLine(
                        color = lineColor,
                        start = Offset(0f, y),
                        end = Offset(size.width, y),
                        strokeWidth = if (isFocused) 1.5f else 1f
                    )
                }
                .padding(bottom = 10.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (prefix != null) {
                    Text(
                        prefix,
                        color = if (isFocused) GoldLight else TextSecondary,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Light,
                        modifier = Modifier.padding(end = 6.dp)
                    )
                }
                BasicTextField(
                    value = value,
                    onValueChange = onValueChange,
                    textStyle = TextStyle(
                        color = TextPrimary,
                        fontSize = if (prefix != null) 22.sp else 17.sp,
                        fontWeight = if (prefix != null) FontWeight.SemiBold else FontWeight.Normal
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
                    singleLine = true,
                    cursorBrush = SolidColor(Gold),
                    interactionSource = interactionSource,
                    modifier = Modifier.fillMaxWidth(),
                    decorationBox = { inner ->
                        Box {
                            if (value.isEmpty()) {
                                Text(
                                    placeholder,
                                    color = TextHint,
                                    fontSize = if (prefix != null) 22.sp else 17.sp,
                                    fontWeight = FontWeight.Light
                                )
                            }
                            inner()
                        }
                    }
                )
            }
        }

        // Hint o error
        AnimatedVisibility(visible = isError && errorMessage != null) {
            Row(
                modifier = Modifier.padding(top = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text("✕", color = ErrorRed, fontSize = 10.sp)
                Text(errorMessage ?: "", color = ErrorRed, fontSize = 11.sp)
            }
        }
        if (hint != null && !isError) {
            Spacer(Modifier.height(4.dp))
            Text(hint, color = TextHint, fontSize = 11.sp)
        }
    }
}

// ── Botón de crear ────────────────────────────────────────────────────────────

@Composable
private fun CreateButton(enabled: Boolean, onClick: () -> Unit) {
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
            .then(
                if (enabled) Modifier.clickable { onClick() } else Modifier
            ),
        contentAlignment = Alignment.Center
    ) {
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
                    Icon(Icons.Default.Check, contentDescription = null, tint = BgDeep, modifier = Modifier.size(14.dp))
                }
            }
            Text(
                if (enabled) "Crear Meta de Ahorro" else "Completa los campos",
                color = textColor,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                letterSpacing = 0.5.sp
            )
        }
    }
}