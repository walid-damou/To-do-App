package eilco.mobile.To_do.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import eilco.mobile.To_do.ui.ThemeViewModel

data class NotificationItem(
    val id: String,
    val title: String,
    val message: String,
    val time: String
)

@Composable
fun NotificationScreen(navController: NavController, viewModel: ThemeViewModel) {
    val notifications = remember {
        mutableStateListOf(
            NotificationItem("1", "New Task Assigned", "You have a new task to complete", "2 min ago"),
            NotificationItem("2", "Meeting Reminder", "Team meeting scheduled for 3 PM", "1 hour ago"),
            NotificationItem("3", "Update Available", "New app update is available", "Yesterday"),
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Notifications", fontSize = 20.sp, fontWeight = FontWeight.Bold) },
                backgroundColor = viewModel.themeColor.value,
                contentColor = Color.White
            )
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            items(notifications) { notification ->
                NotificationCard(notification)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun NotificationCard(notification: NotificationItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        elevation = 4.dp,
        backgroundColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = notification.title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = notification.message,
                fontSize = 14.sp,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = notification.time,
                fontSize = 12.sp,
                color = Color.LightGray
            )
        }
    }
}
