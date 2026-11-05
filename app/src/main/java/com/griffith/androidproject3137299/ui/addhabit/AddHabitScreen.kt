package com.griffith.androidproject3137299.ui.addhabit

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun AddHabitScreen(onBack: () -> Unit) {

    // Variables to stores user input when adding a new habit
    var habitName by remember { mutableStateOf("") }
    var habitDescription by remember { mutableStateOf("") }

    // Variables to store frequency of a habit
    var everyDay by remember { mutableStateOf(false) }
    // 0 - Monday --> 6 - Sunday
    var selectedDays by remember { mutableStateOf(setOf(0, 1, 2, 3, 4, 5, 6)) }
    val dayLetters = listOf("M", "T", "W", "T", "F", "S", "S")

    Column {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    tint = MaterialTheme.colorScheme.primary,
                    contentDescription = "Go back to previous page")
            }
            Column {
                Text(
                    text = "Add Habit",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Make time for a small daily win.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

        }
        // Form
        Column(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Fields for the form
            OutlinedTextField(
                value= habitName,
                onValueChange = { habitName = it },
                label = { Text("Habit name") },
                placeholder = { Text("e.g. Study") },
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value= habitDescription,
                onValueChange = { habitDescription= it },
                label = { Text("Habit description") },
                placeholder = { Text("e.g. Study for exam") },
                minLines = 1,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                dayLetters.forEachIndexed { index, letter ->
                    val isSelected = index in selectedDays

                    // Round container to store weekdays Initials
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .border(1.dp, MaterialTheme.colorScheme.primary, CircleShape)
                            .clickable(enabled = !everyDay) {
                                selectedDays = if (isSelected) selectedDays - index else selectedDays + index
                            }

                            .background(
                                if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = letter,
                            color = if (isSelected) MaterialTheme.colorScheme.onPrimary
                                    else MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }



    }


}
