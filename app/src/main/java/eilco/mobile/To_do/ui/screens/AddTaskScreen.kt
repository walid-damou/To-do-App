package eilco.mobile.To_do.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import eilco.mobile.To_do.ui.ThemeViewModel

@Composable
fun AddTaskScreen(onProceed: () -> Unit, viewModel: ThemeViewModel) {
    val taskTitle = remember { mutableStateOf("") }
    val taskDescription = remember { mutableStateOf("") }
    val errorMessage = remember { mutableStateOf("") }

    val isFormValid = taskTitle.value.isNotEmpty() && taskDescription.value.isNotEmpty()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Create Task",
                style = MaterialTheme.typography.h4,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            OutlinedTextField(
                value = taskTitle.value,
                onValueChange = {
                    taskTitle.value = it
                    errorMessage.value = ""
                },
                label = { Text("Task Title") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = taskDescription.value,
                onValueChange = {
                    taskDescription.value = it
                    errorMessage.value = ""
                },
                label = { Text("Task Description") },
                modifier = Modifier.fillMaxWidth()
            )

            if (errorMessage.value.isNotEmpty()) {
                Text(
                    text = errorMessage.value,
                    color = Color.Red,
                    style = MaterialTheme.typography.body2,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (isFormValid) {
                        onProceed()
                        taskTitle.value = ""
                        taskDescription.value = ""
                    } else {
                        errorMessage.value = "Please fill out both fields."
                    }
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = viewModel.themeColor.value),
                modifier = Modifier.fillMaxWidth(),
                enabled = isFormValid
            ) {
                Text("Next")
            }
        }
    }
}
