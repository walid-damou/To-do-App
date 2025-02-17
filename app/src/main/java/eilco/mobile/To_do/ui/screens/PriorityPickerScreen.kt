package eilco.mobile.To_do.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import eilco.mobile.To_do.ui.ThemeViewModel

@Composable
fun PriorityPickerScreen(navController: NavController, viewModel: ThemeViewModel) {
    val taskList = listOf("Very High", "High", "Medium", "Low")
    val selectedPriority = remember { mutableStateOf<String?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            for (i in taskList.indices step 2) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    for (j in 0..1) {
                        if (i + j < taskList.size) {
                            val task = taskList[i + j]
                            val isSelected = task == selectedPriority.value

                            Card(
                                modifier = Modifier
                                    .clickable {
                                        selectedPriority.value = task

                                        // ✅ Update selected task in ViewModel
                                        val selectedTask = viewModel.selectedTask.value
                                        if (selectedTask != null) {
                                            val updatedTask = selectedTask.copy(priorityLabel = task)
                                            viewModel.setSelectedTask(updatedTask) // Update ViewModel
                                        }

                                        // ✅ Navigate to CalendarPickerScreen
                                        navController.navigate("calendarPicker")
                                    }
                                    .size(150.dp)
                                    .padding(8.dp),
                                shape = MaterialTheme.shapes.medium,
                                backgroundColor = if (isSelected) MaterialTheme.colors.primary else Color.LightGray,
                                elevation = 4.dp
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(12.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = "Priority Icon",
                                        tint = if (isSelected) Color.White else Color.Gray,
                                        modifier = Modifier.size(32.dp)
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = task,
                                        style = MaterialTheme.typography.body2.copy(
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                        ),
                                        color = if (isSelected) Color.White else Color.Black,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}
