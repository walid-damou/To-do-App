package eilco.mobile.To_do.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.*
import androidx.compose.ui.unit.dp

@Composable
fun CalendarPickerScreen(onDateSelected: (String) -> Unit) {
    val calendarState = remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Select a Date",
            style = MaterialTheme.typography.h4,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        // Add your calendar view or date picker logic here
        Text(
            text = "Selected Date: ${calendarState.value}",
            style = MaterialTheme.typography.body1,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = { onDateSelected(calendarState.value) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Select Date")
        }
    }
}
