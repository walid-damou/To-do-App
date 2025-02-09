package eilco.mobile.To_do.ui.screens

import TaskDetailScreen
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
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.rememberInsetsPaddingValues
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.*

import kotlin.random.Random

import eilco.mobile.To_do.R
import eilco.mobile.To_do.ui.ThemeViewModel
import eilco.mobile.To_do.ui.screens.*

@Composable
fun InboxScreen(viewModel: ThemeViewModel) {
    ProvideWindowInsets {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Inbox") },
                    navigationIcon = {
                        IconButton(onClick = { /* Handle back navigation */ }) {
                            Icon(
                                painterResource(id = R.drawable.ic_back),
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
                FloatingActionButton(onClick = { /* Handle add task */ }) {
                    Icon(Icons.Filled.Add, contentDescription = "Add Task")
                }
            }
        ) { innerPadding ->
            Column(modifier = Modifier.padding(innerPadding)) {
                if (viewModel.selectedTask.value != null) {
                    TaskDetailScreen(viewModel = viewModel)
                } else {
                    LazyColumn {
                        items(viewModel.tasks) { task ->
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
    val id: String,
    val title: String,
    val priorityLabel: String,
    val description: String,
    val time: String,
    val comments: Int,
    val shares: Int,
    val date: String
)
