package eilco.mobile.To_do.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.*
import androidx.compose.ui.res.painterResource
import eilco.mobile.To_do.R

@Composable
fun OnboardingScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF5CAF54)) // Green background color
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(id = R.drawable.logo), // Replace with your logo resource
            contentDescription = "App Logo",
            modifier = Modifier.size(100.dp),
            tint = Color.White
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "To-DoPro",
            style = MaterialTheme.typography.h4,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "The best to-do list application for you",
            style = MaterialTheme.typography.body1,
            color = Color.White,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.weight(1f))
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            repeat(3) { index ->
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(
                            color = if (index == 0) Color.White else Color.Gray,
                            shape = CircleShape
                        )
                        .padding(4.dp)
                )
            }
        }
    }
}
