package com.griffith.androidproject3137299

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.core.content.edit
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.griffith.androidproject3137299.ui.addhabit.AddHabitScreen
import com.griffith.androidproject3137299.ui.home.HomeScreen
import com.griffith.androidproject3137299.ui.settings.SettingsScreen
import com.griffith.androidproject3137299.ui.theme.AppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Gets the preferences file
        val prefs = getSharedPreferences("Settings", MODE_PRIVATE)

        setContent {

            // Declaring variable to change to dark mode and vice versa
            val systemDark = isSystemInDarkTheme()
            // Reads the saved values from the file
            var isDarkMode by rememberSaveable {
                mutableStateOf(
                    prefs.getBoolean(
                        "dark mode",
                        systemDark
                    )
                )
            }

            AppTheme(darkTheme = isDarkMode) {

                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "main") {

                    // The main app with the bottom bar
                    composable("main") {
                        AppScaffold(
                            isDarkMode = isDarkMode,
                            onDarkModeChange = {
                                isDarkMode = it
                                prefs.edit { putBoolean("dark mode", it) }
                            },
                            onAddHabit = { navController.navigate("addHabit") }
                        )
                    }

                    // Full page, opened from the + button
                    composable("addHabit") {
                        AddHabitScreen(onBack = { navController.popBackStack() })
                    }
                }
            }
        }
    }
}
// Enum Class to hold values for the Bottom Bar "Links"
enum class Tab(val label: String, val icon: ImageVector) {
    Home("Home", Icons.Filled.Home),
    Reports("Reports", Icons.Filled.DateRange),
    Settings("Settings", Icons.Filled.Settings)
}

@Composable
fun AppScaffold(isDarkMode: Boolean, onDarkModeChange: (Boolean) -> Unit, onAddHabit: () -> Unit) {

    var selectedTab by rememberSaveable { mutableStateOf(Tab.Home) }
    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.primary,
                ){
                // One NavigationBarItem per tab
                Tab.entries.forEach { tab ->
                    NavigationBarItem(
                        selected = selectedTab == tab,
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                            selectedTextColor = MaterialTheme.colorScheme.onPrimary,
                            indicatorColor = MaterialTheme.colorScheme.primary,
                            unselectedIconColor = MaterialTheme.colorScheme.inversePrimary,
                            unselectedTextColor = MaterialTheme.colorScheme.inversePrimary
                    ) ,
                        onClick = { selectedTab = tab },
                        icon = { Icon(tab.icon, contentDescription = tab.label) },
                        label = { Text(tab.label) }
                    )
                }
            }
        }
    ) { innerPadding ->
        // innerPadding stops the content from drawing under the bottom bar
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            // Show a different screen depending on the selected tab
            when (selectedTab) {
                Tab.Home -> HomeScreen(onAddHabit = onAddHabit)
                Tab.Reports -> Text("Reports screen")
                Tab.Settings -> SettingsScreen(
                    isDarkMode = isDarkMode,
                    onDarkModeChange = onDarkModeChange
                )
            }
        }
    }
}