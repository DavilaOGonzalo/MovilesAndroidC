package com.davila.campusgps.ui

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Map
import androidx.compose.material.icons.rounded.MyLocation
import androidx.compose.material.icons.rounded.NearMe
import androidx.compose.material.icons.rounded.Place
import androidx.compose.material.icons.rounded.School
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.davila.campusgps.model.CategoriaFiltro
import com.davila.campusgps.model.Lugar
import com.davila.campusgps.model.TipoErrorUbicacion
import com.davila.campusgps.model.UbicacionUi
import com.davila.campusgps.ui.theme.CampusGpsDimensions
import com.davila.campusgps.util.formatearDistancia
import com.davila.campusgps.viewmodel.MapaViewModel

private val permisosUbicacion = arrayOf(
    Manifest.permission.ACCESS_FINE_LOCATION,
    Manifest.permission.ACCESS_COARSE_LOCATION,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaMapa(viewModel: MapaViewModel) {
    val context = LocalContext.current
    val estado by viewModel.estado.collectAsState()
    val lugaresParaMapa by viewModel.lugaresParaMapa.collectAsState()
    val lugaresConDistancia by viewModel.lugaresConDistancia.collectAsState()
    val seleccionadoId by viewModel.seleccionadoId.collectAsState()
    val lugarSeleccionado by viewModel.lugarSeleccionado.collectAsState()
    val lugarMasCercano by viewModel.lugarMasCercano.collectAsState()
    val categoriaSeleccionada by viewModel.categoriaSeleccionada.collectAsState()
    val radioMetros by viewModel.radioMetros.collectAsState()
    val errorPersistencia by viewModel.errorPersistencia.collectAsState()
    val posicionUsuario = (estado as? UbicacionUi.Disponible)?.posicion

    val solicitudPermisos = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
    ) { permisosConcedidos ->
        val permisoOtorgado = permisosConcedidos[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
            permisosConcedidos[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        if (permisoOtorgado) viewModel.buscar() else viewModel.permisoDenegado()
    }

    val obtenerUbicacion = {
        val tienePermisoPreciso = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION,
        ) == PackageManager.PERMISSION_GRANTED
        val tienePermisoAproximado = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION,
        ) == PackageManager.PERMISSION_GRANTED

        if (tienePermisoPreciso || tienePermisoAproximado) {
            viewModel.buscar()
        } else {
            solicitudPermisos.launch(permisosUbicacion)
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("CampusGPS", style = MaterialTheme.typography.titleLarge)
                        Text(
                            "NAVEGACIÓN DE CAMPUS",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                ),
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = CampusGpsDimensions.PageHorizontal)
                .padding(top = CampusGpsDimensions.CompactGap, bottom = CampusGpsDimensions.PageVertical),
            verticalArrangement = Arrangement.spacedBy(CampusGpsDimensions.SectionGap),
        ) {
            ElevatedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .heightIn(min = CampusGpsDimensions.MapMinimumHeight),
                shape = MaterialTheme.shapes.large,
            ) {
                Box(Modifier.fillMaxSize()) {
                    MapaUbicacion(
                        posicionUsuario = posicionUsuario,
                        lugares = lugaresParaMapa,
                        lugarSeleccionado = lugarSeleccionado,
                        lugarMasCercanoId = lugarMasCercano?.lugar?.id,
                        radioMetros = radioMetros,
                        modifier = Modifier.fillMaxSize().clip(MaterialTheme.shapes.large),
                    )

                    Surface(
                        modifier = Modifier.align(Alignment.TopStart).padding(12.dp),
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.96f),
                        shadowElevation = 3.dp,
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(7.dp),
                        ) {
                            Icon(
                                imageVector = if (posicionUsuario == null) Icons.Rounded.School else Icons.Rounded.MyLocation,
                                contentDescription = null,
                                tint = if (posicionUsuario == null) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.tertiary,
                                modifier = Modifier.size(18.dp),
                            )
                            Text(
                                text = if (posicionUsuario == null) "TECSUP · LIMA" else "GPS ACTIVO",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                            )
                        }
                    }

                    if (estado is UbicacionUi.Buscando) {
                        LinearProgressIndicator(
                            modifier = Modifier.fillMaxWidth().align(Alignment.TopCenter),
                            color = MaterialTheme.colorScheme.secondary,
                        )
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomCenter)
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        Button(
                            onClick = obtenerUbicacion,
                            enabled = estado !is UbicacionUi.Buscando,
                            modifier = Modifier.weight(1f).heightIn(min = CampusGpsDimensions.TouchTarget),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                        ) {
                            if (estado is UbicacionUi.Buscando) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(18.dp),
                                    strokeWidth = 2.dp,
                                    color = MaterialTheme.colorScheme.onSecondary,
                                )
                            } else {
                                Icon(Icons.Rounded.MyLocation, contentDescription = null, modifier = Modifier.size(18.dp))
                            }
                            Spacer(Modifier.width(8.dp))
                            AnimatedContent(targetState = estado is UbicacionUi.Buscando, label = "ubicacion-action") { buscando ->
                                Text(if (buscando) "Buscando…" else "Mi ubicación", maxLines = 1)
                            }
                        }

                        FilledTonalButton(
                            onClick = viewModel::irAlMasCercano,
                            enabled = lugarMasCercano != null,
                            modifier = Modifier.weight(1f).heightIn(min = CampusGpsDimensions.TouchTarget),
                            colors = ButtonDefaults.filledTonalButtonColors(
                                containerColor = MaterialTheme.colorScheme.tertiary,
                                contentColor = MaterialTheme.colorScheme.onTertiary,
                            ),
                        ) {
                            Icon(Icons.Rounded.NearMe, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(Modifier.width(8.dp))
                            Text("Más cercano", maxLines = 1)
                        }
                    }
                }
            }

            AnimatedVisibility(
                visible = lugarSeleccionado != null,
                enter = fadeIn(tween(180)) + expandVertically(tween(220)),
                exit = fadeOut(tween(120)) + shrinkVertically(tween(180)),
            ) {
                lugarSeleccionado?.let { lugar ->
                    LugarSeleccionadoCard(
                        lugar = lugar,
                        distancia = lugaresParaMapa.firstOrNull { it.lugar.id == lugar.id }?.metros,
                        esMasCercano = lugar.id == lugarMasCercano?.lugar?.id,
                    )
                }
            }

            AnimatedVisibility(
                visible = estado is UbicacionUi.PermisoDenegado || estado is UbicacionUi.Error || errorPersistencia,
                enter = fadeIn(tween(180)) + expandVertically(tween(220)),
                exit = fadeOut(tween(120)) + shrinkVertically(tween(180)),
            ) {
                when (val ui = estado) {
                    UbicacionUi.PermisoDenegado -> MensajeErrorUbicacion(
                        mensaje = "Permite el acceso a ubicación para mostrar distancias y lugares cercanos.",
                        tipo = TipoErrorUbicacion.SIN_PERMISO,
                        onReintentar = obtenerUbicacion,
                    )
                    is UbicacionUi.Error -> MensajeErrorUbicacion(
                        mensaje = ui.mensaje,
                        tipo = ui.tipo,
                        onReintentar = obtenerUbicacion,
                    )
                    else -> if (errorPersistencia) {
                        Text(
                            "No se pudo guardar el último lugar visitado.",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                        )
                    }
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(CampusGpsDimensions.CompactGap)) {
                SectionHeading(
                    titulo = "Explora por categoría",
                    detalle = "Filtra los espacios del campus",
                    icono = Icons.Rounded.Map,
                )
                Row(
                    modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    CategoriaFiltro.entries.forEach { categoria ->
                        FilterChip(
                            selected = categoriaSeleccionada == categoria,
                            onClick = { viewModel.seleccionarCategoria(categoria) },
                            label = { Text(categoria.etiqueta) },
                            leadingIcon = {
                                Icon(
                                    imageVector = iconoCategoria(categoria),
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp),
                                )
                            },
                            modifier = Modifier.heightIn(min = CampusGpsDimensions.TouchTarget),
                        )
                    }
                }
            }

            ElevatedCard(
                modifier = Modifier.fillMaxWidth().animateContentSize(),
                shape = MaterialTheme.shapes.medium,
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 10.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Rounded.Place,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp),
                        )
                        Spacer(Modifier.width(8.dp))
                        Text("Radio de cercanía", style = MaterialTheme.typography.titleSmall)
                        Spacer(Modifier.weight(1f))
                        Text("${radioMetros} m", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
                    }
                    SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                        MapaViewModel.RADIOS_CERCANIA_METROS.forEachIndexed { index, radio ->
                            SegmentedButton(
                                selected = radioMetros == radio,
                                onClick = { viewModel.seleccionarRadio(radio) },
                                shape = SegmentedButtonDefaults.itemShape(
                                    index = index,
                                    count = MapaViewModel.RADIOS_CERCANIA_METROS.size,
                                ),
                                label = { Text("$radio m") },
                            )
                        }
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Lugares cercanos", style = MaterialTheme.typography.titleMedium)
                    Text(
                        text = if (posicionUsuario == null) "En Tecsup Lima" else "Ordenados por distancia",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primaryContainer,
                ) {
                    Text(
                        text = lugaresConDistancia.size.toString(),
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                    )
                }
            }

            if (lugaresConDistancia.isEmpty()) {
                EmptyLugaresState(modifier = Modifier.weight(0.8f).fillMaxWidth())
            } else {
                ListaLugares(
                    lugares = lugaresConDistancia,
                    seleccionadoId = seleccionadoId,
                    radioMetros = radioMetros,
                    onSeleccionar = viewModel::seleccionar,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.8f)
                        .heightIn(min = CampusGpsDimensions.ListMinimumHeight),
                )
            }
        }
    }
}

@Composable
private fun SectionHeading(
    titulo: String,
    detalle: String,
    icono: androidx.compose.ui.graphics.vector.ImageVector,
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icono, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
        Spacer(Modifier.width(8.dp))
        Column {
            Text(titulo, style = MaterialTheme.typography.titleSmall)
            Text(detalle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun LugarSeleccionadoCard(
    lugar: Lugar,
    distancia: Float?,
    esMasCercano: Boolean,
) {
    val categoryColor = colorCategoria(lugar)
    val backgroundColor by animateColorAsState(
        targetValue = if (esMasCercano) MaterialTheme.colorScheme.tertiaryContainer else MaterialTheme.colorScheme.surface,
        animationSpec = tween(220),
        label = "selected-place-card",
    )
    ElevatedCard(
        modifier = Modifier.fillMaxWidth().animateContentSize(),
        shape = MaterialTheme.shapes.medium,
        colors = androidx.compose.material3.CardDefaults.elevatedCardColors(containerColor = backgroundColor),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 11.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Surface(shape = CircleShape, color = categoryColor.copy(alpha = 0.14f), modifier = Modifier.size(42.dp)) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(iconoCategoria(CategoriaFiltro.entries.firstOrNull { it.categoria == lugar.categoria } ?: CategoriaFiltro.TODOS), null, tint = categoryColor)
                }
            }
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(lugar.nombre, style = MaterialTheme.typography.titleSmall, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text(lugar.categoria, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Column(horizontalAlignment = Alignment.End) {
                if (esMasCercano) {
                    Text("MÁS CERCANO", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.tertiary)
                }
                Text(formatearDistancia(distancia), style = MaterialTheme.typography.labelLarge)
            }
        }
    }
}

@Composable
private fun EmptyLugaresState(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surfaceVariant,
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(Icons.Rounded.Map, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(30.dp))
            Spacer(Modifier.width(8.dp))
            Text("No hay lugares en este radio", style = MaterialTheme.typography.titleSmall)
            Text("Prueba un radio mayor o cambia la categoría.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
