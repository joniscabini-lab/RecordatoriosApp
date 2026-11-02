package com.example.recordatoriosapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.recordatoriosapp.data.RepositorioSimulado
import com.example.recordatoriosapp.model.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaRecordatorios() {
    val todosLosRecordatorios = remember { RepositorioSimulado.obtenerRecordatoriosValidos() }
    val categoriasUnicas = remember { RepositorioSimulado.categoriasUnicas }
    val mapaCategorias = remember { RepositorioSimulado.configuracionCategorias }

    var prioridadSeleccionada by remember { mutableStateOf<Prioridad?>(null) }
    var soloPendientes by remember { mutableStateOf(false) }

    val recordatoriosFiltrados = remember(prioridadSeleccionada, soloPendientes) {
        val porPrioridad = RepositorioSimulado.filtrarPorPrioridad(todosLosRecordatorios, prioridadSeleccionada)
        RepositorioSimulado.filtrarSoloPendientes(porPrioridad, soloPendientes)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Recordatorios Contextuales", fontWeight = FontWeight.Bold)
                        Text(
                            text = "${categoriasUnicas.size} categorías activas (Set) • ${recordatoriosFiltrados.size} tareas",
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Barra de filtros interactivos para demostrar el filtrado de Lists
            Text(
                text = "Filtrar por Prioridad y Estado:",
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = prioridadSeleccionada == null,
                    onClick = { prioridadSeleccionada = null },
                    label = { Text("Todas") }
                )
                FilterChip(
                    selected = prioridadSeleccionada is Prioridad.Alta,
                    onClick = {
                        prioridadSeleccionada = if (prioridadSeleccionada is Prioridad.Alta) null else Prioridad.Alta
                    },
                    label = { Text("Alta") }
                )
                FilterChip(
                    selected = prioridadSeleccionada is Prioridad.Media,
                    onClick = {
                        prioridadSeleccionada = if (prioridadSeleccionada is Prioridad.Media) null else Prioridad.Media
                    },
                    label = { Text("Media") }
                )
                FilterChip(
                    selected = soloPendientes,
                    onClick = { soloPendientes = !soloPendientes },
                    label = { Text("Solo Pendientes") }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Lista de Recordatorios con LazyColumn
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(recordatoriosFiltrados, key = { it.id }) { recordatorio ->
                    val infoCategoria = mapaCategorias[recordatorio.categoria]
                        ?: Pair(recordatorio.categoria.nombre, Color.Gray)

                    TarjetaRecordatorio(
                        recordatorio = recordatorio,
                        etiquetaCategoria = infoCategoria.first,
                        colorCategoria = infoCategoria.second
                    )
                }
            }
        }
    }
}

@Composable
fun TarjetaRecordatorio(
    recordatorio: Recordatorio,
    etiquetaCategoria: String,
    colorCategoria: Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Fila superior: Etiqueta del Map de Categoría + Prioridad
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = colorCategoria.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = etiquetaCategoria,
                        color = colorCategoria,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }

                BadgePrioridad(prioridad = recordatorio.prioridad)
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Título del recordatorio
            Text(
                text = recordatorio.titulo,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            // Descripción opcional
            recordatorio.descripcion?.let { desc ->
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = desc,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(8.dp))

            // Fila inferior: Estado y datos contextuales opcionales (Ubicación / Hora)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Estado: ${recordatorio.estado.descripcion}",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = when (recordatorio.estado) {
                        is EstadoRecordatorio.Completado -> Color(0xFF2E7D32)
                        is EstadoRecordatorio.Pendiente -> Color(0xFFEF6C00)
                        is EstadoRecordatorio.Vencido -> Color(0xFFC62828)
                    }
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    recordatorio.ubicacion?.let { ubi ->
                        Text(text = "📍 $ubi", style = MaterialTheme.typography.labelSmall)
                    }
                    recordatorio.horaAviso?.let { hora ->
                        Text(text = "⏰ $hora", style = MaterialTheme.typography.labelSmall)
                    }
                }
            }
        }
    }
}

@Composable
fun BadgePrioridad(prioridad: Prioridad) {
    val (colorFondo, colorTexto) = when (prioridad) {
        is Prioridad.Alta -> Pair(Color(0xFFFFEBEE), Color(0xFFC62828))
        is Prioridad.Media -> Pair(Color(0xFFFFF8E1), Color(0xFFF57F17))
        is Prioridad.Baja -> Pair(Color(0xFFE8F5E9), Color(0xFF2E7D32))
    }

    Surface(
        color = colorFondo,
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(
            text = "Prioridad ${prioridad.nombre}",
            color = colorTexto,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}