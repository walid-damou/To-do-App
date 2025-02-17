package eilco.mobile.To_do.ui

import android.annotation.SuppressLint
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import eilco.mobile.To_do.ui.screens.*
import eilco.mobile.To_do.ui.components.BottomNavigationBar

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun AppNavigation(viewModel: ThemeViewModel) {
    val navController: NavHostController = rememberNavController()
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        scaffoldState = scaffoldState,
        bottomBar = { BottomNavigationBar(navController) }
    ) {
        NavHost(navController = navController, startDestination = "inbox") {
            composable("onboarding") {
                AppPager(
                    onLoginSuccess = { navController.navigate("chooseTheme") },
                    onCreateAccount = { navController.navigate("login") },
                    viewModel = viewModel,
                    navController = navController
                )
            }
            composable("addTask") {
                AddTaskScreen(
                    viewModel = viewModel,
                    navController = navController
                )
            }
            composable("createAccount") {
                CreateAccountScreen(
                    viewModel = viewModel,
                    onFinish = { navController.navigate("login") }
                )
            }
            composable("login") {
                LoginScreen(
                    onLoginSuccess = { navController.navigate("chooseTheme") },
                    viewModel = viewModel
                )
            }
            composable("chooseTheme") {
                val userId = viewModel.currentUserId.value
                ChooseThemeScreen(
                    userId = userId ?: "unknown_user",
                    viewModel = viewModel,
                    onProceed = { themeColor ->
                        viewModel.setThemeColor(themeColor)
                        navController.navigate("inbox")
                    },
                    onSkip = { themeColor ->
                        viewModel.setThemeColor(themeColor)
                        navController.navigate("inbox")
                    }
                )
            }
            composable("inbox") {
                InboxScreen(
                    viewModel = viewModel,
                    navController = navController
                )
            }
            composable("notifications") {
                NotificationScreen(
                    navController = navController,
                    viewModel = viewModel
                )
            }
            composable("calendarPicker") {
                CalendarPickerScreen(
                    viewModel = viewModel,
                    onDateSelected = { selectedDate ->
                        val selectedTask = viewModel.selectedTask.value
                        if (selectedTask != null) {
                            val updatedTask = selectedTask.copy(date = selectedDate)
                            viewModel.setSelectedTask(updatedTask) // Ensure date is set
                            navController.navigate("timePicker")
                        } else {
                            println("Error: No task selected before setting date!")
                        }
                    }
                )
            }
            composable("profile") {
                ProfileScreen(
                    navController = navController,
                    viewModel = viewModel
                )
            }
            composable("priorityPicker") {
                PriorityPickerScreen(
                    viewModel = viewModel,
                    navController = navController
                )
            }
            composable("timePicker") {
                TimePickerScreen(
                    viewModel = viewModel,
                    navController = navController,
                    scaffoldState = scaffoldState
                )
            }
        }
    }
}
