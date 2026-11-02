package com.example.recordatoriosapp.data

import androidx.compose.ui.graphics.Color
import com.example.recordatoriosapp.model.*

object RepositorioSimulado {

    // Categorías base (incluyendo una repetida a propósito para demostrar el uso de Set)
    private val catTrabajo = Categoria("cat_1", "Trabajo")
    private val catEstudio = Categoria("cat_2", "Estudio")
    private val catHogar = Categoria("cat_3", "Hogar")
    private val catSalud = Categoria("cat_4", "Salud")
    private val catTrabajoDuplicada = Categoria("cat_1", "Trabajo")

    /**
     * SET: Almacena categorías únicas evitando duplicados automáticamente.
     */
    val categoriasUnicas: Set<Categoria> = setOf(
        catTrabajo,
        catEstudio,
        catHogar,
        catSalud,
        catTrabajoDuplicada // El Set la ignora por tener igual id y nombre
    )

    /**
     * MAP: Asocia cada Categoría con un par (Etiqueta descriptiva, Color de Material).
     */
    val configuracionCategorias: Map<Categoria, Pair<String, Color>> = mapOf(
        catTrabajo to Pair("💼 Laboral", Color(0xFF1E88E5)),
        catEstudio to Pair("📚 Académico", Color(0xFF8E24AA)),
        catHogar to Pair("🏠 Doméstico", Color(0xFF43A047)),
        catSalud to Pair("⚕️ Bienestar", Color(0xFFE53935))
    )

    /**
     * LIST: Lista inmutable con recordatorios simulados (incluyendo datos opcionales).
     */
    private val listaBrutaRecordatorios: List<Recordatorio> = listOf(
        Recordatorio(
            id = 1,
            titulo = "Entregar Actividad 1 de Mobile",
            descripcion = "Subir PDF con capturas y enlace de GitHub",
            categoria = catEstudio,
            prioridad = Prioridad.Alta,
            estado = EstadoRecordatorio.Pendiente,
            ubicacion = "Plataforma Virtual",
            horaAviso = "23:59 hs"
        ),
        Recordatorio(
            id = 2,
            titulo = "Reunión diaria de equipo (Daily)",
            descripcion = "Revisar avance del modelo de datos en Kotlin",
            categoria = catTrabajo,
            prioridad = Prioridad.Alta,
            estado = EstadoRecordatorio.Completado,
            horaAviso = "09:30 hs"
        ),
        Recordatorio(
            id = 3,
            titulo = "Comprar repuesto del filtro de agua",
            descripcion = null, // Sin descripción opcional
            categoria = catHogar,
            prioridad = Prioridad.Media,
            estado = EstadoRecordatorio.Pendiente,
            ubicacion = "Centro comercial"
        ),
        Recordatorio(
            id = 4,
            titulo = "Turno control oftalmológico",
            descripcion = "Llevar estudios previos",
            categoria = catSalud,
            prioridad = Prioridad.Baja,
            estado = EstadoRecordatorio.Vencido("Reprogramar para el mes próximo"),
            ubicacion = "Clínica Regional",
            horaAviso = "16:00 hs"
        ),
        Recordatorio(
            id = 5,
            titulo = "Repasar Sealed Classes y Compose",
            descripcion = "Lectura del módulo 1",
            categoria = catEstudio,
            prioridad = Prioridad.Media,
            estado = EstadoRecordatorio.Pendiente
        ),
        // Ejemplo de recordatorio inválido (título vacío) que será descartado por la validación
        Recordatorio(
            id = 6,
            titulo = " ",
            categoria = catHogar,
            prioridad = Prioridad.Baja
        )
    )

    /**
     * Devuelve únicamente los recordatorios que superan las reglas de validación.
     */
    fun obtenerRecordatoriosValidos(): List<Recordatorio> {
        return listaBrutaRecordatorios.filter { it.validar().isSuccess }
    }

    /**
     * Filtra recordatorios según su prioridad (o devuelve todos si es null).
     */
    fun filtrarPorPrioridad(
        lista: List<Recordatorio>,
        prioridad: Prioridad?
    ): List<Recordatorio> {
        if (prioridad == null) return lista
        return lista.filter { it.prioridad == prioridad }
    }

    /**
     * Filtra recordatorios según si están pendientes, completados o todos.
     */
    fun filtrarSoloPendientes(
        lista: List<Recordatorio>,
        soloPendientes: Boolean
    ): List<Recordatorio> {
        if (!soloPendientes) return lista
        return lista.filter { it.estado is EstadoRecordatorio.Pendiente }
    }
}