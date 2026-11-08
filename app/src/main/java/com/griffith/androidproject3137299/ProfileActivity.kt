package com.griffith.androidproject3137299

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import com.griffith.androidproject3137299.ui.profile.ProfileScreen
import com.griffith.androidproject3137299.ui.theme.AppTheme

class ProfileActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Data sent by the explicit intent that opened this Activity
        val name = intent.getStringExtra("user_name") ?: "Guest"
        val prefs = getSharedPreferences("Settings", MODE_PRIVATE)

        setContent {
            // Same saved theme choice as the rest of the app
            val isDarkMode = prefs.getBoolean("dark mode", isSystemInDarkTheme())

            AppTheme(darkTheme = isDarkMode) {
                Surface(color = MaterialTheme.colorScheme.background) {
                    ProfileScreen(name = name, onBack = { finish() })
                }
            }
        }
    }
}

// Opens the profile page with an explicit intent, passing the user's name
fun openProfile(context: Context, name: String) {
    val intent = Intent(context, ProfileActivity::class.java)
    intent.putExtra("user_name", name)
    context.startActivity(intent)
}