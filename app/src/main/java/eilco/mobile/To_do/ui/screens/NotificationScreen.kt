package eilco.mobile.To_do.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import eilco.mobile.To_do.ui.ThemeViewModel

@Composable
fun NotificationScreen(navController: NavController, viewModel: ThemeViewModel) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Notifications", style = MaterialTheme.typography.h4)
    }
}