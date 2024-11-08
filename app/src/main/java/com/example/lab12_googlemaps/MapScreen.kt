package com.example.lab12_googlemaps

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getDrawable
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.Marker
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.Dash
import com.google.android.gms.maps.model.Dot
import com.google.android.gms.maps.model.Gap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.Polygon
import com.google.maps.android.compose.Polyline

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen() {
    var mapType by remember { mutableStateOf(MapType.NORMAL) }
    var expanded by remember { mutableStateOf(false) }

    val arequipaLocation = LatLng(-16.4040102, -71.559611)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(arequipaLocation, 13f)
    }
    val context = LocalContext.current

    //Convertir el recurso drawable a BitmapDescriptor
    val customMarker = bitmapDescriptorFromVector(
        context = context,
        vectorResId = R.drawable.valor // Asegúrate que este es el nombre correcto de tu imagen
    )

    Box(modifier = Modifier.fillMaxSize()) {
        // Mapa
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = MapProperties(mapType = mapType)
        ) {
            Marker(
                state = rememberMarkerState(position = arequipaLocation),
                icon = customMarker,
                title = "Arequipa",
                snippet = "Ciudad Blanca"
            )
        }

        // Menú desplegable compacto
        Box(
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.TopStart)
        ) {
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                // Botón principal que muestra el tipo actual
                OutlinedButton(
                    onClick = { expanded = true },
                    modifier = Modifier
                        .menuAnchor()
                        .background(Color.White), // Fondo blanco
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Blue) // Texto azul
                ) {
                    Text(
                        when (mapType) {
                            MapType.NORMAL -> "Normal"
                            MapType.SATELLITE -> "Satélite"
                            MapType.HYBRID -> "Híbrido"
                            MapType.TERRAIN -> "Terreno"
                            else -> "Seleccionar tipo"
                        }
                    )
                }

                // Menú desplegable
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Normal") },
                        onClick = {
                            mapType = MapType.NORMAL
                            expanded = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Satélite") },
                        onClick = {
                            mapType = MapType.SATELLITE
                            expanded = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Híbrido") },
                        onClick = {
                            mapType = MapType.HYBRID
                            expanded = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Terreno") },
                        onClick = {
                            mapType = MapType.TERRAIN
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

//Función para convertir el vector/imagen a BitmapDescriptor
fun bitmapDescriptorFromVector(context: Context, vectorResId: Int): BitmapDescriptor? {
    return try {
        val vectorDrawable = ContextCompat.getDrawable(context, vectorResId)
        vectorDrawable?.let {
            val width = it.intrinsicWidth/4
            val height = it.intrinsicHeight/4

            // Asegura que el Bitmap usa ARGB_8888 para canal alfa (transparente)
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            val canvas = android.graphics.Canvas(bitmap)

            // Dibuja el vector en el Canvas del Bitmap
            it.setBounds(0, 0, width, height)
            it.draw(canvas)

            // Convierte el Bitmap en BitmapDescriptor
            BitmapDescriptorFactory.fromBitmap(bitmap)
        }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}