package com.example.recordatoriosapp.model

/**
 * Sealed class para modelar las Prioridades de forma restringida y segura.
 */
sealed class Prioridad(val nombre: String, val nivel: Int) {
    object Alta : Prioridad("Alta", 3)
    object Media : Prioridad("Media", 2)
    object Baja : Prioridad("Baja", 1)
}

/**
 * Sealed class para modelar el Estado de cada recordatorio.
 */
sealed class EstadoRecordatorio(val descripcion: String) {
    object Pendiente : EstadoRecordatorio("Pendiente")
    object Completado : EstadoRecordatorio("Completado")
    data class Vencido(val motivo: String) : EstadoRecordatorio("Vencido ($motivo)")
}

/**
 * Data class inmutable para representar una Categoría.
 * Al ser data class, implementa automáticamente equals() y hashCode(),
 * permitiendo que un Set<Categoria> elimine duplicados sin esfuerzo.
 */
data class Categoria(
    val id: String,
    val nombre: String
)

/**
 * Data class inmutable principal para el Recordatorio.
 * Incluye datos opcionales (nullable) para ubicación y aviso horario.
 */
data class Recordatorio(
    val id: Int,
    val titulo: String,
    val descripcion: String? = null,
    val categoria: Categoria,
    val prioridad: Prioridad,
    val estado: EstadoRecordatorio = EstadoRecordatorio.Pendiente,
    val ubicacion: String? = null, // Opcional: contexto geográfico
    val horaAviso: String? = null  // Opcional: aviso programado
) {
    /**
     * Regla de validación para evitar estados inválidos en el modelo.
     */
    fun validar(): Result<Unit> {
        return when {
            titulo.isBlank() -> Result.failure(IllegalArgumentException("El título no puede estar vacío."))
            titulo.length < 3 -> Result.failure(IllegalArgumentException("El título debe tener al menos 3 caracteres."))
            categoria.nombre.isBlank() -> Result.failure(IllegalArgumentException("La categoría es obligatoria."))
            else -> Result.success(Unit)
        }
    }
}