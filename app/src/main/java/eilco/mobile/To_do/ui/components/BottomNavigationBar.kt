package eilco.mobile.To_do.ui.components

import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import eilco.mobile.To_do.ui.theme.AppTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*

data class BottomNavItem(val route: String, val icon: androidx.compose.ui.graphics.vector.ImageVector, val label: String)

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        BottomNavItem("inbox", Icons.Filled.Home, "Home"),
        BottomNavItem("notifications", Icons.Filled.Notifications, "Notifications"),
        BottomNavItem("calendar", Icons.Filled.DateRange, "Calendar"),
        BottomNavItem("profile", Icons.Filled.Person, "Profile")
    )

    BottomNavigation(
        backgroundColor = Color.White,
        contentColor = AppTheme.colors.primary,
        elevation = 8.dp,
        modifier = Modifier.height(56.dp).padding(bottom = 16.dp)
    ) {
        val currentRoute = navController.currentDestination?.route
        items.forEach { item ->
            BottomNavigationItem(
                icon = { Icon(imageVector = item.icon, contentDescription = item.label) },
                label = { Text(item.label) },
                selected = currentRoute == item.route,
                onClick = { navController.navigate(item.route) }
            )
        }
    }
}
