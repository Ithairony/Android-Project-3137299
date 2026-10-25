package com.griffith.androidproject3137299.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.griffith.androidproject3137299.ui.theme.AppTheme

// Declaring a fake Habit just for design purposes
data class Habit(
    val id: Int,
    val name: String,
    val description: String,
    val time: String,
    val isDone: Boolean
)
// Fake data for designing the Home screen, replaced by the database later
val fakeHabits = listOf(
    Habit(1, "Read for 15 minutes", "Mobile development notes", "08:00", isDone = true),
    Habit(2, "Apply for 2 jobs", "Android roles", "18:30", isDone = true),
    Habit(3, "Review code notes", "Summarize the day", "21:00", isDone = false),
    Habit(4, "Go to the gym", "Biceps and chest", "22:00", isDone = false),
    Habit(5, "Drink 2L of water", "Keep a bottle on the desk", "10:00", isDone = false),
    Habit(6, "Walk 8,000 steps", "Lunch break walk", "13:00", isDone = false),
    Habit(7, "Practice Kotlin", "One small exercise", "16:00", isDone = false),
    Habit(8, "Meditate for 10 minutes", "Breathing before dinner", "19:30", isDone = false),
    Habit(9, "No phone after 23:00", "Wind down for sleep", "23:00", isDone = false)
)

@Composable
fun HabitRow(habit: Habit, onToggle: () -> Unit) {
    OutlinedCard(
        onClick = onToggle,
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(20.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Check circle: filled with a tick when done, just an outline when not
            Box( modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
                .background(
                    color = if (habit.isDone) MaterialTheme.colorScheme.primary
                            else Color.Transparent),
                contentAlignment = Alignment.Center
                ) {
                // If an habit is done it adds a check Icon
                    if (habit.isDone) {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = "Done",
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(16.dp)
                        )
                    }
            }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)){
                Text(
                    text = habit.name,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = habit.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            // Reminder time in a small chip
            Surface(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = habit.time,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }
}

@Composable
fun HomeScreen() {
    // A list Compose watches: when an item changes, the screen redraws
    val habits = remember { fakeHabits.toMutableStateList() }

    // Variable to store remaining tasks
    val remaining = habits.count { !it.isDone }

    // One scrolling list for the whole screen: header first, then the habits
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column (
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Today",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                }
                // Round container to store user Initials
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceContainerHighest),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "IT",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

        }

        // Item for the progress bar on top as depending on the ammount of habits it stays out of the screen
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = when {
                        habits.isEmpty() -> "No habits yet, add your first one"
                        remaining == 0 -> "All done for today!"
                        remaining == 1 -> "1 habit to complete"
                        else -> "$remaining habits to complete"
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.height(8.dp))
                LinearProgressIndicator(
                    progress = { habits.count { it.isDone } / habits.size.toFloat() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.surfaceContainerHighest
                )
            }
        }

        // One HabitRow per habit
        items(habits, key = { it.id }) { habit ->
            HabitRow(
                habit = habit,
                onToggle = {
                    val index = habits.indexOf(habit)
                    habits[index] = habit.copy(isDone = !habit.isDone)
                }
            )
        }


    }
}
