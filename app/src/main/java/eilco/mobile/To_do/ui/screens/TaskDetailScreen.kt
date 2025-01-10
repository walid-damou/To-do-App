package eilco.mobile.To_do.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import eilco.mobile.To_do.ui.ThemeViewModel
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material.*

@Composable
fun TaskDetailScreen(taskId: String?, viewModel: ThemeViewModel) {
    // Sample tasks data might ideally come from your ViewModel or a repository
    val tasks = remember {
        mutableStateOf(listOf(
            Task("1","Masyla Website Project", "Priority task 1", "test description", "8:30 PM", 1, 2, "Mon, 19 Jul 2022"),
            Task("2","Medical Design System", "Priority task 3", "another test description", "8:30 PM", 1, 2, "Mon, 19 Jul 2022")
        ))
    }

    // Correctly find the task by ID
    val task = tasks.value.find { it.id == taskId }

    if (task != null) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Detail Task", style = MaterialTheme.typography.h6)
            Divider()
            Text(task.title, style = MaterialTheme.typography.h5)
            Text(task.description)
            Text("Time: ${task.time}")
            // Add more elements as per the screenshot details
        }
    } else {
        Text("Task not found")
    }
}


