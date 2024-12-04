package eilco.mobile.To_do.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.*
import androidx.compose.ui.unit.dp

@Composable
fun ChooseThemeScreen(onProceed: () -> Unit) {
    val colorThemes = listOf("Green", "Black", "Red", "Blue")

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Create to do list",
                style = MaterialTheme.typography.h4,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = "Choose your to-do list color theme:",
                style = MaterialTheme.typography.body1,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                items(colorThemes) { color ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .background(
                                color = when (color) {
                                    "Green" -> Color.Green
                                    "Black" -> Color.Black
                                    "Red" -> Color.Red
                                    "Blue" -> Color.Blue
                                    else -> Color.Gray
                                }
                            )
                            .height(50.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = color,
                            style = MaterialTheme.typography.body1,
                            color = Color.White
                        )
                    }
                }
            }
            Button(
                onClick = onProceed,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Open To-DoPro")
            }
        }
    }
}
