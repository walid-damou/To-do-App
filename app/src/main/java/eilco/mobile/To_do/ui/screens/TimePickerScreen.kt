package eilco.mobile.To_do.ui.screens

import android.app.TimePickerDialog
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import eilco.mobile.To_do.ui.ThemeViewModel
import java.util.*

@Composable
fun TimePickerScreen(onTimeSelected: (String) -> Unit, viewModel: ThemeViewModel) {
    val timeState = remember { mutableStateOf("") }
    val showTimePicker = remember { mutableStateOf(false) }
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    fun openTimePickerDialog() {
        TimePickerDialog(
            context,
            { _, hour: Int, minute: Int ->
                val formattedTime = String.format("%02d:%02d", hour, minute)
                timeState.value = formattedTime
                onTimeSelected(formattedTime)
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
                Text("Pick Time",
                    style = MaterialTheme.typography.button.copy(color = Color.White))
            }
        }
    }
}
