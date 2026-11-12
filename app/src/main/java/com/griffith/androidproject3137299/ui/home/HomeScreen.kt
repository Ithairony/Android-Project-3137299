package com.griffith.androidproject3137299.ui.home

import android.content.Intent
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.AddTask
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.griffith.androidproject3137299.openProfile
import com.griffith.androidproject3137299.sensors.rememberStepCount
import com.griffith.androidproject3137299.ui.components.SectionLabel
import com.griffith.androidproject3137299.ui.components.StreakCard

// Declaring a fake Habit just for design purposes
data class Habit(
    val id: Int,
    val name: String,
    val description: String,
    val time: String,
    val isDone: Boolean,
    val stepGoal: Int? = null   // Only set for step based habits
)
// Fake data for designing the Home screen, replaced by the database later
//val fakeHabits = emptyList<Habit>()

// Fake data for designing the Home screen, replaced by the database later
val fakeHabits = listOf(
    Habit(1, "Read for 15 minutes", "Mobile development notes", "08:00", isDone = true),
    Habit(2, "Apply for 2 jobs", "Android roles", "18:30", isDone = true),
    Habit(3, "Review code notes", "Summarize the day", "21:00", isDone = false),
    Habit(4, "Go to the gym", "Biceps and chest", "22:00", isDone = false),
    Habit(5, "Drink 2L of water", "Keep a bottle on the desk", "10:00", isDone = false),
    Habit(6, "Walk 8,000 steps", "Lunch break walk", "13:00", isDone = false, stepGoal = 30),
    Habit(7, "Practice Kotlin", "One small exercise", "16:00", isDone = false),
    Habit(8, "Meditate for 10 minutes", "Breathing before dinner", "19:30", isDone = false),
    Habit(9, "No phone after 23:00", "Wind down for sleep", "23:00", isDone = false)
)

@Composable
fun HabitRow(habit: Habit, steps: Int?, onToggle: () -> Unit) {
    OutlinedCard(
        onClick = onToggle, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Check circle: filled with a tick when done, just an outline when not
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
                    .background(
                        color = if (habit.isDone) MaterialTheme.colorScheme.primary
                        else Color.Transparent
                    ), contentAlignment = Alignment.Center
            ) {
                // If a habit is done it adds a check Icon
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
            Column(Modifier.weight(1f)) {
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
                // Live progress, only for habits with a step goal
                if (habit.stepGoal != null) {
                    Text(
                        text = "${steps ?: 0} / ${habit.stepGoal} steps",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
            // Reminder time in a small chip
            Surface(
                color = MaterialTheme.colorScheme.primaryContainer, shape = RoundedCornerShape(8.dp)
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

// CTA to add habit if habits list is empty
@Composable
fun EmptyHabits() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Icon in a soft circle
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.AddTask,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(36.dp)
            )
        }
        Spacer(Modifier.height(16.dp))
        Text(
            text = "No habits yet",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = "Tap + to add your first habit",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun HomeScreen(onAddHabit: () -> Unit) {
    // A list Compose watches: when an item changes, the screen redraws
    val habits = remember { fakeHabits.toMutableStateList() }

    // Variable to store remaining tasks
    val remaining = habits.count { !it.isDone }

    val context = LocalContext.current

    // Number of habits completed today
    val done = habits.count { it.isDone }
    // Hardcoded until the database can calculate it
    val streakDays = 12

    // Has the user allowed activity recognition (step counting)?
    var hasStepPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context, android.Manifest.permission.ACTIVITY_RECOGNITION
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

// Shows Android's permission dialog and receives the answer
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted -> hasStepPermission = granted }

    // Is there any habit that needs the step counter?
    val hasStepHabit = habits.any { it.stepGoal != null }

    // Ask for permission only if a step habit exists
    LaunchedEffect(hasStepHabit) {
        if (hasStepHabit && !hasStepPermission) {
            permissionLauncher.launch(android.Manifest.permission.ACTIVITY_RECOGNITION)
        }
    }

    // Listen to the sensor only when it's useful
    val steps = rememberStepCount(enabled = hasStepPermission && hasStepHabit)

    // Every time the step count changes, complete any step habit that reached its goal
    LaunchedEffect(steps) {
        if (steps == null) return@LaunchedEffect
        habits.forEachIndexed { index, habit ->
            val goal = habit.stepGoal
            if (goal != null && !habit.isDone && steps >= goal) {
                habits[index] = habit.copy(isDone = true)
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // One scrolling list for the whole screen: header first, then the habits
        LazyColumn(
            modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(
                start = 16.dp, end = 16.dp, top = 16.dp, bottom = 88.dp
            ), verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "Today",
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
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
                    }
                    // Round container to store user Initials
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .clickable { openProfile(context, "Igor Thairony") }
                            .background(MaterialTheme.colorScheme.surfaceContainerHighest),
                        contentAlignment = Alignment.Center) {
                        Text(
                            text = "IT",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

            }

            item {
                StreakCard(
                    days = streakDays, onShare = {
                        // Implicit intent: share the current streak
                        val shareIntent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(
                                Intent.EXTRA_TEXT, "I'm on a $streakDays day habit streak!"
                            )
                        }
                        context.startActivity(
                            Intent.createChooser(
                                shareIntent, "Share your streak"
                            )
                        )
                    })
            }

            if (habits.isEmpty()) {
                // Display the call to action
                item { EmptyHabits() }
            } else {
                // Item for the progress bar on top as depending on the amount of habits it stays out of the screen
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Progress",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.SemiBold
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

                        // Share button only appears once every habit is done
                        if (remaining == 0) {
                            TextButton(
                                onClick = {
                                    // Implicit intent: Android shows every app that can send text
                                    val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                        type = "text/plain"
                                        // Message to be shown when sharing intent
                                        putExtra(
                                            Intent.EXTRA_TEXT,
                                            "I've completed all ${habits.size} of my habits today!"
                                        )
                                    }
                                    context.startActivity(
                                        Intent.createChooser(
                                            shareIntent, "Share your progress"
                                        )
                                    )
                                }) {
                                Icon(
                                    imageVector = Icons.Outlined.Share,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(Modifier.width(6.dp))
                                Text("Share progress")
                            }
                        }
                    }
                }
                item { SectionLabel("Today's checklist") }
                // One HabitRow per habit
                items(habits, key = { it.id }) { habit ->
                    HabitRow(
                        habit = habit,
                        steps = steps,
                        onToggle = {
                            val index = habits.indexOf(habit)
                            habits[index] = habit.copy(isDone = !habit.isDone)
                        }
                    )
                }
            }
        }

        FloatingActionButton(
            onClick = onAddHabit,
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(14.dp)
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Add habit")
        }
    }
}
