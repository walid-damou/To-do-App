package eilco.mobile.To_do.ui.screens

import android.app.TimePickerDialog
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import java.util.*

@Composable
fun TimePicker(
    onTimeSelected: (String) -> Unit
) {
    val context = LocalContext.current
    val calendar = remember { Calendar.getInstance() }
    val timeState = rememberUpdatedState(onTimeSelected)

    // Remember selected time
    val selectedTime = remember { mutableStateOf("") }

    TimePickerDialog(
        context,
        { _, hour: Int, minute: Int ->
            selectedTime.value = String.format("%02d:%02d", hour, minute)
            timeState.value(selectedTime.value) // Pass the selected time
        },
        calendar.get(Calendar.HOUR_OF_DAY),
        calendar.get(Calendar.MINUTE),
        true
    ).show()
}

@Composable
fun TimePickerScreen(onTimeSelected: (String) -> Unit) {
    val timeState = remember { mutableStateOf("") }
    val showTimePicker = remember { mutableStateOf(false) }

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
                onClick = {
                    showTimePicker.value = true
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Pick Time")
            }

            if (showTimePicker.value) {
                TimePicker(onTimeSelected = { time ->
                    timeState.value = time
                    onTimeSelected(time)
                    showTimePicker.value = false
                })
            }
        }
    }
}
