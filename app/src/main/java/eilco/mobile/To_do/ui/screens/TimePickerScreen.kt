package eilco.mobile.To_do.ui.screens

import android.app.TimePickerDialog
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import eilco.mobile.To_do.ui.ThemeViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

@Composable
fun TimePickerScreen(
    navController: NavController,
    viewModel: ThemeViewModel,
    scaffoldState: ScaffoldState
) {
    val timeState = remember { mutableStateOf("") }
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    fun openTimePickerDialog() {
        TimePickerDialog(
            context,
            { _, hour: Int, minute: Int ->
                val formattedTime = String.format("%02d:%02d", hour, minute)
                timeState.value = formattedTime

                val selectedTask = viewModel.selectedTask.value
                if (selectedTask != null) {
                    val taskWithTime = selectedTask.copy(time = formattedTime)
                    viewModel.setSelectedTask(taskWithTime) // Retain updated date
                    saveTaskToFirebase(taskWithTime, navController, scaffoldState)
                } else {
                    println("Error: No task is selected before saving!")
                }
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        ).show()
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Select a Time",
                style = MaterialTheme.typography.h4,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Text(
                text = "Selected Time: ${timeState.value}",
                style = MaterialTheme.typography.body1,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Button(
                onClick = { openTimePickerDialog() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = viewModel.themeColor.value)
            ) {
                Text("Pick Time", style = MaterialTheme.typography.button.copy(color = Color.White))
            }
        }
    }
}

fun saveTaskToFirebase(
    task: Task,
    navController: NavController,
    scaffoldState: ScaffoldState
) {
    val database = FirebaseDatabase.getInstance()
    val user = FirebaseAuth.getInstance().currentUser

    if (user == null) {
        Log.e("FirebaseError", "User not authenticated, cannot save task")
        return
    }

    if (task.title.isBlank() || task.description.isBlank()) {
        CoroutineScope(Dispatchers.Main).launch {
            scaffoldState.snackbarHostState.showSnackbar("Task title and description required!")
        }
        return
    }

    // Prevent saving if date is still "TBD"
    if (task.date == "TBD") {
        CoroutineScope(Dispatchers.Main).launch {
            scaffoldState.snackbarHostState.showSnackbar("Please select a date before saving.")
        }
        return
    }

    val tasksRef = database.getReference("tasks/${user.uid}").push()
    val taskId = tasksRef.key ?: return

    val finalTask = task.copy(id = taskId)

    tasksRef.setValue(finalTask)
        .addOnSuccessListener {
            Log.d("FirebaseSuccess", "Task successfully added: ${task.title}")

            CoroutineScope(Dispatchers.Main).launch {
                scaffoldState.snackbarHostState.showSnackbar("Task added successfully!")
            }

            navController.navigate("inbox") {
                popUpTo("inbox") { inclusive = true }
            }
        }
        .addOnFailureListener { e ->
            Log.e("FirebaseError", "Error saving task: ${e.message}")
            CoroutineScope(Dispatchers.Main).launch {
                scaffoldState.snackbarHostState.showSnackbar("Failed to add task. Try again.")
            }
        }
}