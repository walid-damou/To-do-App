package eilco.mobile.To_do.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import eilco.mobile.To_do.R
import kotlinx.coroutines.delay

@Composable
fun OnboardingScreen(navController: NavController) {
    LaunchedEffect(Unit) {
        delay(6000)  // Show onboarding for 3 seconds
        navController.navigate("login") {
            popUpTo("onboarding") { inclusive = true }  // Prevent returning to onboarding
        }
    }

    // Animation for logo scaling effect
    val scale = remember { Animatable(0.5f) }
    LaunchedEffect(Unit) {
        scale.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 1000,
                easing = FastOutSlowInEasing
            )
        )
    }

    // Gradient background
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF387C56), Color(0xFF9EF89E))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_app_logo),
                contentDescription = "App Logo",
                modifier = Modifier
                    .size(100.dp * scale.value)
                    .padding(16.dp),
                tint = Color.White
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "To-DoPro",
                style = MaterialTheme.typography.h4.copy(
                    fontSize = 32.sp
                ),
                color = Color.White
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Organize, plan, and achieve!",
                style = MaterialTheme.typography.subtitle1.copy(
                    fontSize = 18.sp
                ),
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 32.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))

            // Progress Indicator
            LinearProgressIndicator(
                color = Color.White,
                backgroundColor = Color.White.copy(alpha = 0.3f),
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .height(6.dp)
            )
        }
    }
}