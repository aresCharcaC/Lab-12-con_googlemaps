package com.example.lab12_googlemaps

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
import com.google.android.gms.maps.model.Dash
import com.google.android.gms.maps.model.Dot
import com.google.android.gms.maps.model.Gap
import com.google.maps.android.compose.Polygon
import com.google.maps.android.compose.Polyline

@Composable
fun MapScreen() {
    val arequipaLocation = LatLng(-16.4040102, -71.559611)
    val cameraPositionState = rememberCameraPositionState {
        position = com.google.android.gms.maps.model.CameraPosition.fromLatLngZoom(arequipaLocation, 13f)
    }
    val context = LocalContext.current  
    //Convertir el recurso drawable a BitmapDescriptor
    val customMarker = bitmapDescriptorFromVector(
        context = context,
        vectorResId = R.drawable.valor // Asegúrate que este es el nombre correcto de tu imagen
    )

    // Definir los polígonos de los centros comerciales
    val mallAventuraPolygon = listOf(
        LatLng(-16.432292, -71.509145),
        LatLng(-16.432757, -71.509626),
        LatLng(-16.433013, -71.509310),
        LatLng(-16.432566, -71.508853)
    )

    val parqueLambramaniPolygon = listOf(
        LatLng(-16.422704, -71.530830),
        LatLng(-16.422920, -71.531340),
        LatLng(-16.423264, -71.531110),
        LatLng(-16.423050, -71.530600)
    )

    // Definir rutas (polylines) entre puntos de interés
    val rutaTuristica = listOf(
        LatLng(-16.398866, -71.536961), // Plaza de Armas
        LatLng(-16.422704, -71.530830), // Parque Lambramani
        LatLng(-16.432292, -71.509145)  // Mall Aventura
    )

    // Ruta alternativa
    val rutaAlternativa = listOf(
        LatLng(-16.398866, -71.536961), // Plaza de Armas
        LatLng(-16.405373, -71.532669), // Punto intermedio
        LatLng(-16.432292, -71.509145)  // Mall Aventura
    )

    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState
        ) {
            // Dibujar polígonos de los centros comerciales
            Polygon(
                points = mallAventuraPolygon,
                strokeColor = Color.Red,
                fillColor = Color(0x330000FF), // Azul semi-transparente
                strokeWidth = 5f
            )

            Polygon(
                points = parqueLambramaniPolygon,
                strokeColor = Color.Red,
                fillColor = Color(0x330000FF),
                strokeWidth = 5f
            )

            // Dibujar las rutas (polylines)
            Polyline(
                points = rutaTuristica,
                color = Color.Green,
                width = 8f,
                pattern = listOf(
                    Dash(30f),
                    Gap(20f)
                )
            )

            Polyline(
                points = rutaAlternativa,
                color = Color.Blue,
                width = 8f,
                pattern = listOf(
                    Dot(),
                    Gap(10f)
                )
            )

            // Añadir marcadores en los puntos de interés
            Marker(
                state = rememberMarkerState(position = LatLng(-16.398866, -71.536961)),
                icon = customMarker,
                title = "Plaza de Armas"
            )

            Marker(
                state = rememberMarkerState(position = LatLng(-16.432292, -71.509145)),
                icon = customMarker,
                title = "Mall Aventura"
            )

            Marker(
                state = rememberMarkerState(position = LatLng(-16.422704, -71.530830)),
                icon = customMarker,
                title = "Parque Lambramani"
            )
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