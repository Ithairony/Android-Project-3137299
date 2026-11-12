package com.griffith.androidproject3137299.sensors

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext

// Listens to GPS updates while the screen is visible.
// Returns the latest known location, or null if there isn't one yet.
@SuppressLint("MissingPermission") // HomeScreen only enables this once permission is granted
@Composable
fun rememberLocation(enabled: Boolean): Location? {
    val context = LocalContext.current
    var location by remember { mutableStateOf<Location?>(null) }

    DisposableEffect(enabled) {
        val locationManager =
            context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        // Called every time the position changes
        val listener = LocationListener { newLocation -> location = newLocation }

        if (enabled) {
            // Show the last known position straight away, if Android has one
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)

            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                5000L,  // at most one update every 5 seconds
                10f,    // only when the user moved at least 10 metres
                listener
            )
        }

        // Stop listening when the screen leaves, to save battery
        onDispose { locationManager.removeUpdates(listener) }
    }

    return location
}

// Distance in metres from the user's location to a habit's location
fun distanceInMetres(from: Location, latitude: Double, longitude: Double): Float {
    val result = FloatArray(1)
    Location.distanceBetween(from.latitude, from.longitude, latitude, longitude, result)
    return result[0]
}