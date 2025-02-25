import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import eilco.mobile.To_do.ui.ThemeViewModel
import java.text.SimpleDateFormat
import java.util.*

data class NotificationItem(
    val id: String,
    val title: String,
    val message: String,
    val time: String
)

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun NotificationScreen(navController: NavController, viewModel: ThemeViewModel) {
    val notifications = remember { mutableStateListOf<NotificationItem>() }
    val isLoading = remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val userId = currentUser?.uid ?: "unknown_user"

        fetchUpcomingTasks(userId) { tasks ->
            notifications.clear()
            notifications.addAll(tasks)
            isLoading.value = false
        }
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
        if (isLoading.value) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (notifications.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        painter = painterResource(id = android.R.drawable.ic_dialog_info),
                        contentDescription = "No Notifications",
                        modifier = Modifier.size(64.dp),
                        tint = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("No notifications at the moment.", color = Color.Gray)
                }
            }
        } else {
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
}

@Composable
fun NotificationCard(notification: NotificationItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        elevation = 6.dp,
        shape = RoundedCornerShape(16.dp),
        backgroundColor = Color(0xFFF5F5F5)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = notification.title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2C3E50)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = notification.message,
                fontSize = 14.sp,
                color = Color(0xFF34495E)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = android.R.drawable.ic_menu_my_calendar),
                    contentDescription = "Due Date",
                    tint = Color(0xFF2980B9),
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = notification.time,
                    fontSize = 12.sp,
                    color = Color(0xFF7F8C8D)
                )
            }
        }
    }
}
// Function to fetch tasks from Firebase due in 24 hours
fun fetchUpcomingTasks(userId: String, onResult: (List<NotificationItem>) -> Unit) {
    // Get the Firebase Database reference for the user's tasks
    val tasksRef = FirebaseDatabase.getInstance()
        .getReference("tasks")
        .child(userId)

    val notifications = mutableListOf<NotificationItem>()
    val currentTime = System.currentTimeMillis()
    val timeIn24Hours = currentTime + (24 * 60 * 60 * 1000) // 24 hours in milliseconds
    val dateTimeFormat = SimpleDateFormat("dd MMMM yyyy HH:mm", Locale.getDefault()) // Match Firebase format

    tasksRef.addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            Log.d("NotificationDebug", "Fetched data for user: $userId, Children count: ${snapshot.childrenCount}")

            for (taskSnapshot in snapshot.children) {
                val title = taskSnapshot.child("title").getValue(String::class.java) ?: "Untitled Task"
                val description = taskSnapshot.child("description").getValue(String::class.java) ?: ""
                val date = taskSnapshot.child("date").getValue(String::class.java) ?: continue
                val time = taskSnapshot.child("time").getValue(String::class.java) ?: "00:00"

                // Combine date and time into a timestamp
                val dateTimeString = "$date $time"
                val taskDueDate = try {
                    dateTimeFormat.parse(dateTimeString)?.time ?: continue
                } catch (e: Exception) {
                    Log.d("NotificationDebug", "Date parsing failed for: $dateTimeString")
                    continue
                }

                // Check if the task is due within the next 24 hours
                if (taskDueDate in currentTime..timeIn24Hours) {
                    notifications.add(
                        NotificationItem(
                            id = taskSnapshot.key ?: "",
                            title = "Upcoming Task",
                            message = "Task '$title' is due soon: $description",
                            time = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date(taskDueDate))
                        )
                    )
                }
            }
            Log.d("NotificationDebug", "Total upcoming tasks found: ${notifications.size}")
            onResult(notifications)
        }

        override fun onCancelled(error: DatabaseError) {
            Log.e("NotificationDebug", "Database Error: ${error.message}")
            onResult(emptyList())
        }
    })
}
