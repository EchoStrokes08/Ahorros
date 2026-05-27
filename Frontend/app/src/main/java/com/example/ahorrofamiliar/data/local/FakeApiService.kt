/*package com.example.ahorrofamiliar.data.local

import com.example.ahorrofamiliar.data.model.Meta
import com.example.ahorrofamiliar.data.model.Miembro
import com.example.ahorrofamiliar.data.model.Pago
import com.example.ahorrofamiliar.data.remote.ApiService
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object FakeApiService : ApiService {

    private val miembrosPorMeta: Map<Int, List<Miembro>> = mapOf(
        1 to listOf(
            Miembro(1, "Mamá"),
            Miembro(2, "Papá"),
            Miembro(3, "Hermano")
        ),
        2 to listOf(
            Miembro(4, "Papá"),
            Miembro(5, "Tío Carlos")
        ),
        3 to listOf(
            Miembro(6, "Mamá"),
            Miembro(7, "Papá"),
            Miembro(8, "Abuela"),
            Miembro(9, "Hermana")
        ),
        4 to listOf(
            Miembro(10, "Mamá"),
            Miembro(11, "Papá")
        )
    )

    private val pagos: MutableList<Pago> = mutableListOf(
        Pago(1, 1, 1, "Mamá", 600000.0, "2026-03-12"),
        Pago(2, 1, 2, "Papá", 700000.0, "2026-03-15"),
        Pago(3, 1, 3, "Hermano", 325000.0, "2026-04-02"),
        Pago(4, 2, 4, "Papá", 1500000.0, "2026-02-20"),
        Pago(5, 2, 5, "Tío Carlos", 600000.0, "2026-04-10"),
        Pago(6, 3, 6, "Mamá", 1200000.0, "2026-01-15"),
        Pago(7, 3, 7, "Papá", 1500000.0, "2026-02-05"),
        Pago(8, 3, 8, "Abuela", 800000.0, "2026-03-01"),
        Pago(9, 3, 9, "Hermana", 1000000.0, "2026-04-20"),
        Pago(10, 4, 10, "Mamá", 80000.0, "2026-05-01")
    )

    private val metasBase: List<Meta> = listOf(
        Meta(1, "TV 55 pulgadas", 2_500_000.0, 0.0, "https://picsum.photos/seed/tv/600/400"),
        Meta(2, "Moto familiar", 7_000_000.0, 0.0, "https://picsum.photos/seed/moto/600/400"),
        Meta(3, "Vacaciones en Santa Marta", 5_000_000.0, 0.0, "https://picsum.photos/seed/playa/600/400"),
        Meta(4, "Nevera nueva", 1_800_000.0, 0.0, "https://picsum.photos/seed/nevera/600/400")
    )

    private fun construirMeta(base: Meta): Meta {
        val pagosDeLaMeta = pagos.filter { it.metaId == base.id }
        val totalAhorrado = pagosDeLaMeta.sumOf { it.monto }
        val miembros = (miembrosPorMeta[base.id] ?: emptyList()).map { miembro ->
            val totalAportado = pagosDeLaMeta
                .filter { it.miembroId == miembro.id }
                .sumOf { it.monto }
            miembro.copy(totalAportado = totalAportado)
        }
        return base.copy(totalAhorrado = totalAhorrado, miembros = miembros)
    }

    override suspend fun obtenerMetas(): List<Meta> =
        metasBase.map(::construirMeta)

    override suspend fun obtenerMeta(id: Int): Meta {
        val base = metasBase.firstOrNull { it.id == id }
            ?: throw IllegalArgumentException("Meta $id no existe")
        return construirMeta(base)
    }

    override suspend fun obtenerPagos(metaId: Int): List<Pago> =
        pagos.filter { it.metaId == metaId }.sortedByDescending { it.fecha }

    override suspend fun registrarPago(pago: Pago): Pago {
        val nuevoId = (pagos.maxOfOrNull { it.id ?: 0 } ?: 0) + 1
        val fechaActual = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val nombreMiembro = miembrosPorMeta[pago.metaId]
            ?.firstOrNull { it.id == pago.miembroId }
            ?.nombre
        val nuevo = pago.copy(id = nuevoId, miembroNombre = nombreMiembro, fecha = fechaActual)
        pagos.add(nuevo)
        return nuevo
    }
}
*/