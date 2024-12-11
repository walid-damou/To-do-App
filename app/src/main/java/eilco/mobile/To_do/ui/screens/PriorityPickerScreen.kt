package eilco.mobile.To_do.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.unit.dp

@Composable
fun PriorityPickerScreen(onPrioritySelected: (String) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            items(listOf("Priority task 1", "Priority task 2", "Priority task 3", "Priority task 4")) { task ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onPrioritySelected(task) }
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Star, contentDescription = "Priority Icon")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = task,
                        style = MaterialTheme.typography.body1
                    )
                }
            }
        }
    }
}
