package eilco.mobile.To_do.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.runtime.*
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
                val color = colorThemes.find { it.first == colorName }?.second ?: Color(0xFFE7ECF5)
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
                modifier = Modifier.padding(top = 40.dp, bottom = 12.dp)
            )
            LazyColumn(
                contentPadding = PaddingValues(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(colorThemes) { theme ->
                    ThemeCard(
                        themeName = theme.first,
                        themeColor = theme.second,
                        isSelected = selectedTheme.value == theme,
                        onClick = { selectedTheme.value = theme }
                    )
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
                    .padding(top = 10.dp, bottom = 50.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = selectedTheme.value.second
                )
            ) {
                Text(
                    text = "Open To-DoPro",
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun ThemeCard(
    themeName: String,
    themeColor: Color,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .width(327.dp)
            .height(100.dp)
            .padding(8.dp)
            .clickable { onClick() }
    ) {
        Card(
            elevation = 4.dp,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Box(
                    modifier = Modifier
                        .width(44.dp)
                        .fillMaxHeight()
                        .background(themeColor)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(Color(0xFFE7ECF5), shape = CircleShape)
                    )

                    Spacer(modifier = Modifier.width(16.dp))
                }
            }
        }

        if (isSelected) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = "Selected Theme",
                tint = themeColor,
                modifier = Modifier
                    .size(24.dp)
                    .align(Alignment.TopStart)
                    .offset(x = -16.dp, y = -16.dp)
            )
        }
    }
}
