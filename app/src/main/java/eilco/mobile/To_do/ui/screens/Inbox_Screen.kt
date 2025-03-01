package eilco.mobile.To_do.ui.screens

import TaskDetailScreen
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.rememberInsetsPaddingValues
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

import kotlin.random.Random

import eilco.mobile.To_do.R
import eilco.mobile.To_do.ui.ThemeViewModel
import eilco.mobile.To_do.ui.screens.*

@Composable
fun InboxScreen(viewModel: ThemeViewModel, navController: NavController) {

    val userId = FirebaseAuth.getInstance().currentUser?.uid
    // Ensure userId is not null
    if (userId == null) {
        // Handle user not logged in, e.g., navigate to login screen or show error.
        Text("User not logged in")
        return
    }

    // Create a mutable state list that Compose will observe
    val tasksList = remember { mutableStateListOf<Task>() }

    // Get the Firebase Database reference for the user's tasks
    val tasksRef = FirebaseDatabase.getInstance()
        .getReference("tasks")
        .child(userId)

    // Attach a listener using DisposableEffect so it is lifecycle-aware
    DisposableEffect(tasksRef) {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Clear the current list to avoid duplicates
                tasksList.clear()
                // Iterate over each child in the snapshot
                for (taskSnapshot in snapshot.children) {
                    // Convert the snapshot to a Task object
                    val task = taskSnapshot.getValue(Task::class.java)
                    if (task != null) {
                        tasksList.add(task)
                    }
                }
                // Optionally log the list size or details for debugging
                Log.d("InboxScreen", "Loaded ${tasksList.size} tasks from Firebase")
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("InboxScreen", "Error reading tasks", error.toException())
            }
        }
        tasksRef.addValueEventListener(listener)
        onDispose {
            tasksRef.removeEventListener(listener)
        }
    }

    ProvideWindowInsets {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Home") },
                    navigationIcon = {
                        IconButton(onClick = { /* Handle back navigation */ }) {
                            Icon(
                                painterResource(id = R.drawable.ic_launcher_foreground),
                                contentDescription = "Back",
                                modifier = Modifier.size(30.dp),
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = { /* Handle search action */ }) {
                            Icon(
                                painterResource(id = R.drawable.ic_search),
                                modifier = Modifier.size(30.dp),
                                contentDescription = "Search"
                            )
                        }
                    },
                    modifier = Modifier.padding(top = rememberInsetsPaddingValues(insets = LocalWindowInsets.current.statusBars).calculateTopPadding())
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { navController.navigate("addTask") },
                    modifier = Modifier.padding(bottom = 64.dp)
                ) {
                    Icon(Icons.Filled.Add, contentDescription = "Add Task")
                }
            }
        ) { innerPadding ->
            Column(modifier = Modifier.padding(innerPadding)) {
                if (viewModel.selectedTask.value != null) {
                    TaskDetailScreen(viewModel = viewModel)
                } else {
                    LazyColumn {
                        val tasksRef = FirebaseDatabase.getInstance().getReference("tasks").child(userId.toString())
                        Log.d("InboxScreen----------", "tasksRef: $tasksList")


                        items(tasksList) { task ->
                            TaskItem(task = task, viewModel = viewModel)
                        }
                    }
                }
            }
        }
    }
}
@Composable
fun TaskItem(task: Task, viewModel: ThemeViewModel) {
    val priorityColor = remember { randomColor() }

    Card(
        shape = RoundedCornerShape(10.dp),
        elevation = 2.dp,
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .fillMaxWidth()
            .clickable { viewModel.selectedTask.value = task }
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .background(color = priorityColor)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Icon(
                    painterResource(id = R.drawable.ic_priority), // Ensure this drawable resource exists
                    contentDescription = "Priority Flag",
                    tint = MaterialTheme.colors.onSurface,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = task.priorityLabel,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    Icons.Filled.MoreVert,
                    contentDescription = "More options",
                    modifier = Modifier.size(24.dp)
                )
            }

            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    task.title,
                    fontSize = 21.sp,
                    modifier = Modifier.padding(start = 4.dp)
                )

            }
            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Filled.Schedule,
                    contentDescription = "Time",
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    task.time,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(start = 4.dp),
                    style = MaterialTheme.typography.body2
                )
                Spacer(Modifier.weight(0.05f))
                Icon(
                    Icons.Filled.Comment,
                    contentDescription = "comment",
                    modifier = Modifier.size(20.dp)
                )
                Spacer(Modifier.weight(1f))
                Text(
                    task.date,
                    fontSize = 12.sp,
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
                )

            }

        }
    }
}

fun randomColor(): Color {
    val random = Random
    return Color(
        red = random.nextFloat(),
        green = random.nextFloat(),
        blue = random.nextFloat(),
        alpha = 0.2f  // Setting a light transparency
    )
}

data class Task(
    val id: String = "",
    val title: String = "",
    val priorityLabel: String = "",
    val description: String = "",
    val time: String = "",
    val comments: Int = 0,
    val shares: Int = 0,
    val date: String = ""
)