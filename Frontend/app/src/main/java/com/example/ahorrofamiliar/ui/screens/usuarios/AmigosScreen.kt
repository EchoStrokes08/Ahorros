package com.example.ahorrofamiliar.ui.screens.amigos

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ahorrofamiliar.data.model.Usuario
import com.example.ahorrofamiliar.ui.viewmodel.AmigosViewModel

// ─── Paleta compartida ────────────────────────────────────────────────────────
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
fun AmigosScreen(
    viewModel: AmigosViewModel,
    usuarioActual: Usuario,
    onAmigoAdded: () -> Unit = {}
) {
    val usuarios by viewModel.usuarios.collectAsState()

    LaunchedEffect(Unit) { viewModel.cargarUsuarios() }

    val otraPersonas = usuarios.filter { it.id != usuarioActual.id }
    val totalAmigos  = otraPersonas.count { usuarioActual.amigos.contains(it.id) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BgDeep)
    ) {
        // ── Orbes decorativos ─────────────────────────────────────────────
        Box(
            modifier = Modifier
                .size(240.dp)
                .align(Alignment.TopEnd)
                .offset(x = 70.dp, y = (-50).dp)
                .background(
                    Brush.radialGradient(listOf(GoldDim.copy(alpha = 0.25f), Color.Transparent)),
                    CircleShape
                )
        )
        Box(
            modifier = Modifier
                .size(180.dp)
                .align(Alignment.BottomStart)
                .offset(x = (-50).dp, y = 50.dp)
                .background(
                    Brush.radialGradient(listOf(Color(0xFF0D1F30).copy(alpha = 0.6f), Color.Transparent)),
                    CircleShape
                )
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 40.dp)
        ) {

            // ── Encabezado ────────────────────────────────────────────────
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(start = 24.dp, end = 24.dp, top = 24.dp, bottom = 20.dp)
                ) {
                    Text(
                        "COMUNIDAD",
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
                            "Mis\nAmigos",
                            color = TextPrimary,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            lineHeight = 36.sp
                        )
                        if (totalAmigos > 0) {
                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    "$totalAmigos",
                                    color = Gold,
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text("conectados", color = TextSecondary, fontSize = 11.sp)
                            }
                        }
                    }

                    Spacer(Modifier.height(20.dp))
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
                    Spacer(Modifier.height(8.dp))

                    // Perfil propio
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Avatar grande propio
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(
                                    Brush.linearGradient(listOf(GoldDim, Color(0xFF3A2E0A)))
                                )
                                .border(1.5.dp, Gold, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                usuarioActual.nombre.firstOrNull()?.uppercase() ?: "?",
                                color = GoldLight,
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            )
                        }
                        Column {
                            Text(
                                usuarioActual.nombre,
                                color = TextPrimary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Text("Tú", color = TextSecondary, fontSize = 12.sp)
                        }
                        Spacer(Modifier.weight(1f))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(GoldDim.copy(alpha = 0.4f))
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text("Mi perfil", color = Gold, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }

                    Spacer(Modifier.height(4.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(BgCardBorder)
                    )
                }
            }

            // ── Título sección lista ──────────────────────────────────────
            item {
                Row(
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .width(3.dp)
                            .height(16.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(Gold)
                    )
                    Text(
                        "Personas",
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Text(
                        "(${otraPersonas.size})",
                        color = TextSecondary,
                        fontSize = 13.sp
                    )
                }
                Spacer(Modifier.height(12.dp))
            }

            // ── Estado vacío ──────────────────────────────────────────────
            if (otraPersonas.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp, vertical = 32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Box(
                                modifier = Modifier
                                    .size(72.dp)
                                    .clip(CircleShape)
                                    .background(
                                        Brush.radialGradient(listOf(GoldDim.copy(0.35f), Color.Transparent))
                                    )
                                    .border(1.dp, GoldDim, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("◈", fontSize = 28.sp, color = GoldDim)
                            }
                            Spacer(Modifier.height(16.dp))
                            Text(
                                "No hay otros usuarios",
                                color = TextPrimary,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 16.sp,
                                textAlign = TextAlign.Center
                            )
                            Spacer(Modifier.height(4.dp))
                            Text(
                                "Cuando otros se unan,\naparecerán aquí",
                                color = TextSecondary,
                                fontSize = 13.sp,
                                textAlign = TextAlign.Center,
                                lineHeight = 19.sp
                            )
                        }
                    }
                }
            }

            // ── Lista de usuarios ─────────────────────────────────────────
            itemsIndexed(otraPersonas) { index, usuario ->
                val esAmigo = usuarioActual.amigos.contains(usuario.id)

                var visible by remember { mutableStateOf(false) }
                LaunchedEffect(Unit) {
                    kotlinx.coroutines.delay(index * 60L)
                    visible = true
                }

                AnimatedVisibility(
                    visible = visible,
                    enter = fadeIn(tween(350)) + slideInVertically(tween(350)) { 30 }
                ) {
                    UsuarioRow(
                        usuario = usuario,
                        esAmigo = esAmigo,
                        onAgregar = {
                            viewModel.agregarAmigo(
                                usuarioActual.id,
                                usuario.id,
                                onSuccess = onAmigoAdded
                            )
                        }
                    )
                }
            }
        }
    }
}

// ── Fila de usuario ───────────────────────────────────────────────────────────

@Composable
private fun UsuarioRow(
    usuario: Usuario,
    esAmigo: Boolean,
    onAgregar: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 5.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(BgCard)
            .border(
                1.dp,
                if (esAmigo) Emerald.copy(alpha = 0.25f) else BgCardBorder,
                RoundedCornerShape(18.dp)
            )
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Avatar con inicial
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(
                    if (esAmigo)
                        Brush.linearGradient(listOf(EmeraldDim, Color(0xFF0A2A1E)))
                    else
                        Brush.linearGradient(listOf(Color(0xFF1A1A28), Color(0xFF111120)))
                )
                .border(
                    1.dp,
                    if (esAmigo) Emerald.copy(alpha = 0.5f) else BgCardBorder,
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                usuario.nombre.firstOrNull()?.uppercase() ?: "?",
                color = if (esAmigo) Emerald else TextSecondary,
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp
            )
        }

        // Nombre e indicador
        Column(modifier = Modifier.weight(1f)) {
            Text(
                usuario.nombre,
                color = TextPrimary,
                fontWeight = FontWeight.SemiBold,
                fontSize = 15.sp
            )
            Spacer(Modifier.height(2.dp))
            if (esAmigo) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(5.dp)
                            .clip(CircleShape)
                            .background(Emerald)
                    )
                    Text("Amigo", color = Emerald, fontSize = 11.sp, fontWeight = FontWeight.Medium)
                }
            } else {
                Text("No es tu amigo aún", color = TextSecondary, fontSize = 11.sp)
            }
        }

        // Acción
        if (esAmigo) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(EmeraldDim)
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text("✓ Amigos", color = Emerald, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
        } else {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(Brush.linearGradient(listOf(GoldDim, Gold)))
                    .clickable { onAgregar() }
                    .padding(horizontal = 14.dp, vertical = 8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Icon(
                        Icons.Default.PersonAdd,
                        contentDescription = null,
                        tint = BgDeep,
                        modifier = Modifier.size(13.dp)
                    )
                    Text(
                        "Agregar",
                        color = BgDeep,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}