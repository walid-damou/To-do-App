package eilco.mobile.To_do.ui.screens

import android.app.DatePickerDialog
import android.util.Log
import android.widget.DatePicker
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import eilco.mobile.To_do.ui.ThemeViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun CalendarPickerScreen(onDateSelected: (String) -> Unit, viewModel: ThemeViewModel) {
    val calendarState = remember { mutableStateOf("") }
    val context = LocalContext.current
    val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())

    fun openDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            context,
            { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
                calendar.set(year, month, dayOfMonth)
                val selectedDate = dateFormat.format(calendar.time)
                calendarState.value = selectedDate
                onDateSelected(selectedDate)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Select a Date",
                style = MaterialTheme.typography.h5.copy(fontWeight = FontWeight.Bold, fontSize = 24.sp),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Text(
                text = if (calendarState.value.isNotEmpty()) "Selected Date: ${calendarState.value}" else "No Date Selected",
                style = MaterialTheme.typography.body1.copy(color = Color.Gray, fontSize = 18.sp),
                modifier = Modifier.padding(bottom = 24.dp)
            )

            Button(
                onClick = { openDatePickerDialog() },
                shape = MaterialTheme.shapes.medium,
                colors = ButtonDefaults.buttonColors(backgroundColor = viewModel.themeColor.value),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .height(50.dp)
            ) {
                Text(
                    text = "Select Date",
                    style = MaterialTheme.typography.button.copy(color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CalendarPickerScreenPreview() {
    CalendarPickerScreen(onDateSelected = { selectedDate ->
        println("Selected date: $selectedDate")
        Log.d("CalendarPickerScreen", "Selected date: $selectedDate")
    }, viewModel = ThemeViewModel())
}
