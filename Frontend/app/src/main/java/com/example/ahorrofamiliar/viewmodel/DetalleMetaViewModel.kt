package com.example.ahorrofamiliar.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ahorrofamiliar.data.model.Meta
import com.example.ahorrofamiliar.data.model.Pago
import com.example.ahorrofamiliar.data.repository.MetaRepository
import com.example.ahorrofamiliar.data.repository.UsuarioRepository
import com.example.ahorrofamiliar.data.model.Usuario
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetalleMetaViewModel(
    private val metaRepository: MetaRepository,
    private val usuarioRepository: UsuarioRepository
) : ViewModel() {

    data class MiembroConAporte(
        val id: Int,
        val nombre: String,
        val montoTotal: Double
    )

    private val _meta = MutableStateFlow<UiState<Meta>>(UiState.Loading)
    val meta: StateFlow<UiState<Meta>> = _meta.asStateFlow()

    private val _pagos = MutableStateFlow<List<Pago>>(emptyList())
    val pagos: StateFlow<List<Pago>> = _pagos.asStateFlow()

    private val _amigosDisponibles = MutableStateFlow<List<Usuario>>(emptyList())
    val amigosDisponibles: StateFlow<List<Usuario>> = _amigosDisponibles.asStateFlow()

    private val _nombresUsuarios = MutableStateFlow<Map<Int, String>>(emptyMap())
    val nombresUsuarios: StateFlow<Map<Int, String>> = _nombresUsuarios.asStateFlow()

    private val _miembrosAportes = MutableStateFlow<List<MiembroConAporte>>(emptyList())
    val miembrosAportes: StateFlow<List<MiembroConAporte>> = _miembrosAportes.asStateFlow()

    fun cargarDetalle(metaId: Int, userId: Int) {
        viewModelScope.launch {
            _meta.value = UiState.Loading
            try {
                // 1. Cargar todos los usuarios para tener los nombres
                val todosLosUsuarios = usuarioRepository.obtenerUsuarios()
                val nombresMap = todosLosUsuarios.associate { it.id to it.nombre }
                _nombresUsuarios.value = nombresMap

                // 2. Cargar meta
                val metaData = metaRepository.obtenerMeta(metaId, userId)
                _meta.value = UiState.Success(metaData)

                // 3. Cargar pagos
                val pagosData = metaRepository.obtenerPagos(metaId)
                _pagos.value = pagosData

                // 4. Calcular aportes por miembro
                calcularAportes(metaData.miembros, pagosData, nombresMap)

                // 5. Cargar amigos para invitar
                val usuarioActual = todosLosUsuarios.find { it.id == userId }
                val amigosIds = usuarioActual?.amigos ?: emptyList()
                _amigosDisponibles.value = todosLosUsuarios.filter {
                    amigosIds.contains(it.id) && !metaData.miembros.contains(it.id)
                }

            } catch (e: Exception) {
                _meta.value = UiState.Error(e.message ?: "Error al cargar detalle")
            }
        }
    }

    private fun calcularAportes(
        miembrosIds: List<Int>,
        pagos: List<Pago>,
        nombres: Map<Int, String>
    ) {
        val aportes = miembrosIds.map { id ->
            val totalAportado = pagos.filter { it.idUsuario == id }.sumOf { it.monto }
            MiembroConAporte(
                id = id,
                nombre = nombres[id] ?: "Usuario #$id",
                montoTotal = totalAportado
            )
        }.sortedByDescending { it.montoTotal }
        
        _miembrosAportes.value = aportes
    }

    fun agregarMiembro(metaId: Int, idAmigo: Int, idSolicitante: Int) {
        viewModelScope.launch {
            try {
                metaRepository.agregarMiembro(metaId, idAmigo, idSolicitante)
                cargarDetalle(metaId, idSolicitante) // Recargar
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
