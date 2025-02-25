import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import eilco.mobile.To_do.ui.ThemeViewModel
import eilco.mobile.To_do.ui.screens.Task
import com.google.firebase.database.FirebaseDatabase


@Composable
fun TaskDetailScreen(viewModel: ThemeViewModel) {
    val task = viewModel.selectedTask.value
    if (task != null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF7F7F7))
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Task Details",
                    style = MaterialTheme.typography.h5.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.weight(1f)
                )
                IconButton(
                    onClick = { viewModel.selectedTask.value = null }
                ) {
                    Icon(Icons.Default.Close, contentDescription = "Close")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            TaskDetailView(viewModel.selectedTask.value!!, viewModel)
            Spacer(modifier = Modifier.height(24.dp))
            TextInput()
        }
    } else {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Task not found", style = MaterialTheme.typography.h6)
        }
    }
}

@Composable
fun TaskDetailView(task: Task, viewModel: ThemeViewModel) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Confirm Deletion") },
            text = { Text("Are you sure you want to delete this task?") },
            confirmButton = {
                Button(
                    onClick = {
                        deleteTaskFromFirebase(task.id)
                        viewModel.selectedTask.value = null // Close detail screen
                        showDeleteDialog = false
                    }
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                Button(onClick = { showDeleteDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = 8.dp,
        shape = RoundedCornerShape(16.dp),
        backgroundColor = Color.White
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            // Task Title with Delete Icon
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.h5.copy(fontWeight = FontWeight.Bold)
                )
                IconButton(onClick = { showDeleteDialog = true }) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete Task", tint = Color.Red)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Task Priority Badge
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(getPriorityColor(task.priorityLabel))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = task.priorityLabel,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Task Description
            Text(
                text = task.description,
                style = MaterialTheme.typography.body1.copy(color = Color.DarkGray)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Task Time Info
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Schedule, contentDescription = "Time")
                Text(
                    text = task.time,
                    modifier = Modifier.padding(start = 8.dp),
                    style = MaterialTheme.typography.body2.copy(fontSize = 14.sp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            Divider()
        }
    }
}

// 🔥 Function to delete task from Firebase
fun deleteTaskFromFirebase(taskId: String) {
    val userId = FirebaseAuth.getInstance().currentUser?.uid
    if (userId != null) {
        val taskRef = FirebaseDatabase.getInstance().getReference("tasks").child(userId).child(taskId)
        taskRef.removeValue().addOnSuccessListener {
            Log.d("TaskDeletion", "Task deleted successfully!")
        }.addOnFailureListener {
            Log.e("TaskDeletion", "Failed to delete task.")
        }
    }
}

// 🎨 Function to determine color based on priority level
fun getPriorityColor(priorityLabel: String): Color {
    return when (priorityLabel.lowercase()) {
        "high" -> Color(0xFFE74C3C) // Red
        "medium" -> Color(0xFFF1C40F) // Yellow
        "low" -> Color(0xFF2ECC71) // Green
        else -> Color.Gray // Default for unknown priorities
    }
}

@Composable
fun TextInput() {
    var text by remember { mutableStateOf("") }
    var submittedText by remember { mutableStateOf<String?>(null) }

    Card(
        elevation = 6.dp,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            TextField(
                value = text,
                onValueChange = { text = it },
                label = { Text("Add Comment") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color(0xFFF0F0F0),
                    focusedIndicatorColor = MaterialTheme.colors.primary
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = {
                    if (text.isNotBlank()) {
                        submittedText = text
                        text = ""
                    }
                },
                modifier = Modifier.align(Alignment.End),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Submit")
            }

            submittedText?.let {
                Spacer(modifier = Modifier.height(16.dp))
                Text("Your Comment:", fontWeight = FontWeight.Bold)
                Text(it)
            }
        }
    }
}
