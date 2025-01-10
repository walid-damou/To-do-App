package eilco.mobile.To_do.ui.screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.database.FirebaseDatabase
import eilco.mobile.To_do.ui.AppPager
import eilco.mobile.To_do.ui.ThemeViewModel

@Composable
fun AppNavigation(viewModel: ThemeViewModel) {
    val navController: NavHostController = rememberNavController()

    NavHost(navController = navController, startDestination = "onboarding") {
        composable("onboarding") {
            AppPager(
                onLoginSuccess = { navController.navigate("chooseTheme") },
                onCreateAccount = { navController.navigate("login") },
                viewModel = viewModel
            )
        }
        composable("createAccount") {
            CreateAccountScreen(
                viewModel = viewModel,
                onFinish = { navController.navigate("login") })
        }
        composable("login") {
            LoginScreen(
                onLoginSuccess = { navController.navigate("chooseTheme") },
                viewModel = viewModel
            )
        }
        composable("taskDetail/{taskId}") { backStackEntry ->
            // Get the task ID from the navigation argument
            val taskId = backStackEntry.arguments?.getString("taskId")
            TaskDetailScreen(taskId, viewModel)
        }
        composable("chooseTheme") {
            ChooseThemeScreen(
                userId = "user7",
                viewModel = viewModel,
                onProceed = { themeColor ->
                    viewModel.setThemeColor(themeColor)
                    navController.navigate("addTask")
                    //CHANGE TO HOME PAGE CENTRALIZING ALL TASKS
                },
                onSkip = { themeColor ->
                    viewModel.setThemeColor(themeColor)
                    navController.navigate("addTask")
                    //CHANGE TO HOME PAGE CENTRALIZING ALL TASKS
                }
            )
        }
        composable("addTask") {
            AddTaskScreen(viewModel = viewModel, onProceed = { navController.navigate("priorityPicker") })
        }
        composable("priorityPicker") {
            PriorityPickerScreen(viewModel = viewModel, onPrioritySelected = { navController.navigate("calendarPicker") })
        }
        composable("calendarPicker") {
            CalendarPickerScreen(viewModel = viewModel, onDateSelected = { navController.navigate("timePicker") })
        }
        composable("timePicker") {
            TimePickerScreen(viewModel = viewModel, onTimeSelected = { /* Final action */ })
        }
    }
}
