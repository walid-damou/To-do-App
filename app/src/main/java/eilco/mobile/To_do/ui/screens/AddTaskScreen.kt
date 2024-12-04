package eilco.mobile.To_do.ui.screens
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.text.style.*
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.filled.Flag

@Composable
fun AddTaskScreen(onTaskAdded: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = "",
            onValueChange = { /* Update Task Name */ },
            label = { Text("Task Name") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = "",
            onValueChange = { /* Update Description */ },
            label = { Text("Description") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = { /* Open Date Picker */ }) {
                Icon(Icons.Default.DateRange, contentDescription = "Date Picker")
            }
            IconButton(onClick = { /* Open Priority Picker */ }) {
                Icon(Icons.Filled.Flag, contentDescription = "Priority Picker")
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = onTaskAdded,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add Task")
        }
    }
}
