package eilco.mobile.To_do.ui.screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import eilco.mobile.To_do.ui.AppPager

@Composable
fun AppNavigation() {
    val navController: NavHostController = rememberNavController()

    NavHost(navController = navController, startDestination = "onboarding") {
        // Swiping logic for onboarding
        composable("onboarding") {
            AppPager(
                onLoginSuccess = { navController.navigate("addTask") },
                onCreateAccount = { navController.navigate("createAccount") }
            )
        }
        // Create account flow
        composable("createAccount") {
            CreateAccountScreen(
                onFinish = { navController.navigate("addTask") }
            )
        }
        // Add task and other screens
        composable("addTask") {
            AddTaskScreen(onTaskAdded = { navController.navigate("chooseTheme") })
        }
        composable("chooseTheme") {
            ChooseThemeScreen(onProceed = { navController.navigate("priorityPicker") })
        }
        composable("priorityPicker") {
            PriorityPickerScreen(onPrioritySelected = { navController.navigate("calendarPicker") })
        }
        composable("calendarPicker") {
            CalendarPickerScreen(onDateSelected = { navController.navigate("timePicker") })
        }
        composable("timePicker") {
            TimePickerScreen(onTimeSelected = { /* Final action */ })
        }
    }
}
