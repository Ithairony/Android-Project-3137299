package com.griffith.androidproject3137299.sensors

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.delay

// Listens to the step counter while the screen is visible.
// Returns the steps taken since listening started, or null if no data yet.
@Composable
fun rememberStepCount(enabled: Boolean): Int? {
    val context = LocalContext.current
    var steps by remember { mutableStateOf<Int?>(null) }

    // Runs again whenever "enabled" changes (for example, once permission is granted)
    DisposableEffect(enabled) {
        val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        // First value received, so we only count steps from now on
        var baseline: Float? = null

        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                val total = event.values[0]
                if (baseline == null) baseline = total
                steps = (total - baseline!!).toInt()
            }
            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }

        if (enabled && stepSensor != null) {
            sensorManager.registerListener(listener, stepSensor, SensorManager.SENSOR_DELAY_UI)
        }

        // Stop listening when the screen leaves, to save battery
        onDispose { sensorManager.unregisterListener(listener) }
    }

    // The emulator has no step counter, so I need to simulate a user "walking"
    val hasSensor = remember {
        (context.getSystemService(Context.SENSOR_SERVICE) as SensorManager)
            .getDefaultSensor(Sensor.TYPE_STEP_COUNTER) != null
    }
    if (enabled && !hasSensor) {
        LaunchedEffect(Unit) {
            var simulated = 0
            while (true) {
                delay(1000)          // one fake step every half second
                simulated += 1
                steps = simulated
            }
        }
    }
    // Returns steps
    return steps
}
