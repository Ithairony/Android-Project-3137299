package com.griffith.androidproject3137299.ui.addhabit

import android.app.DatePickerDialog
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import android.app.TimePickerDialog
import android.widget.Toast
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.griffith.androidproject3137299.ui.settings.SettingsGroup
import com.griffith.androidproject3137299.ui.settings.SettingsSwitchRow
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun AddHabitScreen(onBack: () -> Unit) {

    val context = LocalContext.current

    // Variables to stores user input when adding a new habit
    var habitName by remember { mutableStateOf("") }
    var habitDescription by remember { mutableStateOf("") }

    // Variables to store frequency of a habit
    var everyDay by remember { mutableStateOf(true) }
    // 0 - Monday --> 6 - Sunday
    var selectedDays by remember { mutableStateOf(setOf(0, 1, 2, 3, 4, 5, 6)) }
    val dayLetters = listOf("M", "T", "W", "T", "F", "S", "S")
    val dayNames = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")

    // The day the habit starts, today by default
    var startDate by remember { mutableStateOf(LocalDate.now()) }
    val dateFormatter = DateTimeFormatter.ofPattern("d MMMM yyyy")

    // Variables for the reminder
    var remindMe by remember { mutableStateOf(true)}
    var timeOfReminder by remember { mutableStateOf(LocalTime.of(12,0))}

    // Variable to store text that goes under selected days
    val frequencyText = when {
        selectedDays.size == 7 -> "Repeats every day."  // If every day
        selectedDays.isEmpty() -> "Pick at least one day."  // If no days are selected
        //  Else display selected days followed by ", " and a "." at the end
        else -> "Repeats on " + selectedDays.sorted().joinToString(", ") { dayNames[it] } + "."
    }

    // Making sure no habit is saved without a name and at least one day
    val canSave = habitName.isNotBlank() && selectedDays.isNotEmpty()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 24.dp)
            .verticalScroll(rememberScrollState()) // Makes sure the form is scrollable on small screens
    ) {
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
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Name of habit name
            FieldLabel("Habit name")
            OutlinedTextField(
                value = habitName,
                onValueChange = { habitName = it },
                placeholder = { Text("e.g. Study") },
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            )

            // Description of habit
            FieldLabel("Description")
            OutlinedTextField(
                value = habitDescription,
                onValueChange = { habitDescription = it },
                placeholder = { Text("e.g. Study for exam") },
                minLines = 1,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            )

            // Frequency of habit
            FieldLabel("Frequency")
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ChoicePill(
                    text = "Every day",
                    selected = everyDay,
                    onClick = {
                        everyDay = true
                        selectedDays = setOf(0, 1, 2, 3, 4, 5, 6)
                    },
                    modifier = Modifier.weight(1f)
                )
                ChoicePill(
                    text = "Custom days",
                    selected = !everyDay,
                    onClick = { everyDay = false },
                    modifier = Modifier.weight(1f)
                )
            }

            // If every day is selected this is not shown
            if (!everyDay) {
                // Row to store selectable days letter, only shown on custom days mode
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
                                    selectedDays =
                                        if (isSelected) selectedDays - index else selectedDays + index
                                }

                                .background(
                                    if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent
                                ),
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

            Text(
                text = frequencyText,
                style = MaterialTheme.typography.bodySmall,
                color = if (selectedDays.isEmpty()) MaterialTheme.colorScheme.error
                else MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.height(8.dp))

            // Reminder card, reusing the components from Settings screen
            SettingsGroup {
                SettingsSwitchRow(
                    icon = Icons.Outlined.Notifications,
                    title = "Remind me",
                    description = "A nudge to stay on track",
                    checked = remindMe,
                    onCheckedChange = { remindMe = it }
                )

                if (remindMe) {
                    OutlinedCard(
                        onClick = {
                            TimePickerDialog(
                                context,
                                { _, hour, minute -> timeOfReminder = LocalTime.of(hour, minute) },
                                timeOfReminder.hour,
                                timeOfReminder.minute,
                                true // 24-hour clock
                            ).show()
                        },
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Schedule,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(Modifier.width(12.dp))
                            Text(
                                text = "Reminder time",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = timeOfReminder.format(DateTimeFormatter.ofPattern("HH:mm")),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Icon(
                                imageVector = Icons.Filled.KeyboardArrowDown,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }

            // Habit start date
            Row(
                modifier = Modifier
                    .clickable {
                        val dialog = DatePickerDialog(
                            context,
                            { _, year, month, day ->
                                // DatePickerDialog counts months from 0, LocalDate from 1
                                startDate = LocalDate.of(year, month + 1, day)
                            },
                            startDate.year,
                            startDate.monthValue - 1,
                            startDate.dayOfMonth
                        )
                        // A habit can't start in the past
                        dialog.datePicker.minDate = System.currentTimeMillis()
                        dialog.show()
                    }
                    .padding(top = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.CalendarMonth,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = if (startDate == LocalDate.now()) "Starts today · ${startDate.format(dateFormatter)}"
                    else "Starts on ${startDate.format(dateFormatter)}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.Filled.KeyboardArrowDown,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(18.dp)
                )
            }

            Spacer(Modifier.height(16.dp))

            // Save button: disabled until the form is valid
            Button(
                onClick = {
                    // No database yet, so just confirm and go back
                    Toast.makeText(context, "Habit \"$habitName\" saved", Toast.LENGTH_SHORT).show()
                    onBack()
                },
                enabled = canSave,
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text(text = "SAVE HABIT", fontWeight = FontWeight.SemiBold)
            }
            Text(
                text = "You can change these details anytime.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

// Function to display field label
@Composable
private fun FieldLabel(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.primary,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier.padding(top = 8.dp)
    )
}

// "Every day" " Custom days" selection pill
@Composable
fun ChoicePill(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.height(52.dp),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(
            width = if (selected) 2.dp else 1.dp,
            color = if (selected) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.outlineVariant
        ),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = if (selected) MaterialTheme.colorScheme.primaryContainer
            else Color.Transparent
        )
    ) {
        if (selected) {
            Icon(
                imageVector = Icons.Filled.Check,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(18.dp)
            )
            Spacer(Modifier.width(6.dp))
        }
        Text(
            text = text,
            color = if (selected) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
        )
    }
}


