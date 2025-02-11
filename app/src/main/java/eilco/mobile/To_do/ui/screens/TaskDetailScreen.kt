import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import eilco.mobile.To_do.R
import eilco.mobile.To_do.ui.ThemeViewModel
import eilco.mobile.To_do.ui.screens.Task

@Composable
fun TaskDetailScreen(viewModel: ThemeViewModel) {
    val task = viewModel.selectedTask.value
    if (task != null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 50.dp)
                .verticalScroll(rememberScrollState()) // Allow scrolling if content overflows
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Detail Task",
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier.weight(1f)
                )
                IconButton(
                    onClick = { viewModel.selectedTask.value = null },
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close"
                    )
                }
            }
            Divider()
            Spacer(modifier = Modifier.height(10.dp))
            TaskDetailView(task)
            Spacer(modifier = Modifier.height(10.dp))
            TextInput()
        }
    } else {
        Text("Task not found", modifier = Modifier.padding(16.dp))
    }
}

@Composable
fun TaskDetailView(task: Task) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(task.title, style = MaterialTheme.typography.h5)
        Text(task.description, style = MaterialTheme.typography.body1)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                Icons.Filled.Schedule,
                contentDescription = "Time",
                modifier = Modifier.size(20.dp)
            )
            Text(
                task.time,
                fontSize = 14.sp,
                modifier = Modifier.padding(start = 4.dp),
                style = MaterialTheme.typography.body2
            )
        }

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Filled.Favorite, contentDescription = "Like", modifier = Modifier.padding(4.dp))
            Icon(Icons.Filled.Share, contentDescription = "Share", modifier = Modifier.padding(4.dp))
            Icon(
                painterResource(id = R.drawable.ic_priority),
                modifier = Modifier.size(30.dp),
                contentDescription = "Priority"
            )
            Icon(Icons.Filled.MoreVert, contentDescription = "More", modifier = Modifier.padding(4.dp))
        }
    }
}

@Composable
fun TextInput() {
    var text by remember { mutableStateOf("") }
    var submittedText by remember { mutableStateOf<String?>(null) }

    Column(modifier = Modifier.padding(16.dp)) {
        TextField(
            value = text,
            onValueChange = { text = it },
            label = { Text("Enter details") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                if (text.isNotBlank()) {
                    submittedText = text
                    text = "" // Clear input field after submission
                }
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Submit")
        }

        submittedText?.let {
            Spacer(modifier = Modifier.height(16.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = 4.dp
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("User Input:", style = MaterialTheme.typography.h6)
                    Text(it, style = MaterialTheme.typography.body1)
                }
            }
        }
    }
}
