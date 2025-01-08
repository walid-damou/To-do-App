package eilco.mobile.To_do.ui.screens

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.firebase.database.FirebaseDatabase
import eilco.mobile.To_do.ui.ThemeViewModel

fun updateUserTheme(userId: String, selectedTheme: String, onSuccess: () -> Unit, onError: (Exception) -> Unit) {
    val database = FirebaseDatabase.getInstance()
    val userRef = database.getReference("users/$userId/theme")

    userRef.setValue(selectedTheme)
        .addOnSuccessListener {
            onSuccess()
        }
        .addOnFailureListener { exception ->
            onError(exception)
        }
}

@Composable
fun ChooseThemeScreen(
    userId: String,
    viewModel: ThemeViewModel,
    onProceed: (Color) -> Unit,
    onSkip: (Color) -> Unit
) {
    val colorThemes = listOf(
        "Green" to Color(0xFF5CAF54),
        "Black" to Color(0xFF000000),
        "Red" to Color(0xFFFF4C4C),
        "Blue" to Color(0xFF4A90E2)
    )
    val selectedTheme = remember { mutableStateOf(colorThemes[0]) }
    val isLoading = remember { mutableStateOf(true) }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        val database = FirebaseDatabase.getInstance()
        val userRef = database.getReference("users/$userId/theme")

        userRef.get().addOnSuccessListener { snapshot ->
            val colorName = snapshot.getValue(String::class.java)
            if (!colorName.isNullOrEmpty()) {
                val color = when (colorName) {
                    "Green" -> Color(0xFF5CAF54)
                    "Black" -> Color(0xFF000000)
                    "Red" -> Color(0xFFFF4C4C)
                    "Blue" -> Color(0xFF4A90E2)
                    else -> Color.Gray
                }
                isLoading.value = false
                onSkip(color)
            } else {
                isLoading.value = false
            }
        }.addOnFailureListener {
            isLoading.value = false
        }
    }

    if (isLoading.value) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxSize().padding(16.dp)
        ) {
            Text(
                text = "Choose Your Theme",
                style = MaterialTheme.typography.h4,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(colorThemes) { theme ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clickable { selectedTheme.value = theme },
                        backgroundColor = theme.second,
                        elevation = if (selectedTheme.value == theme) 8.dp else 4.dp
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (selectedTheme.value == theme) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = "Selected Theme",
                                    tint = Color.White,
                                    modifier = Modifier.padding(end = 8.dp)
                                )
                            }
                            Text(
                                text = theme.first,
                                color = Color.White,
                                style = MaterialTheme.typography.body1
                            )
                        }
                    }
                }
            }
            Button(
                onClick = {
                    updateUserTheme(
                        userId = userId,
                        selectedTheme = selectedTheme.value.first,
                        onSuccess = {
                            Toast.makeText(context, "Theme saved!", Toast.LENGTH_SHORT).show()
                            onProceed(selectedTheme.value.second)
                        },
                        onError = {
                            Toast.makeText(context, "Failed to save theme.", Toast.LENGTH_SHORT).show()
                        }
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = selectedTheme.value.second
                )
            ) {
                Text(
                    text="Open To-DoPro",
                    color = Color.White
                )
            }
        }
    }
}
