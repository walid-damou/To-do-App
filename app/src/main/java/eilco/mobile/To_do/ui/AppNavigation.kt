package eilco.mobile.To_do.ui.screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
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
                    navController.navigate("addTask")
                },
                onSkip = { themeColor ->
                    viewModel.setThemeColor(themeColor)
                    navController.navigate("addTask")
                }
            )
        }
        composable("addTask") {
            AddTaskScreen(
                viewModel = viewModel,
                onProceed = { navController.navigate("priorityPicker") }
            )
        }
        composable("priorityPicker") {
            PriorityPickerScreen(
                viewModel = viewModel,
                onPrioritySelected = { navController.navigate("calendarPicker") }
            )
        }
        composable("calendarPicker") {
            CalendarPickerScreen(
                viewModel = viewModel,
                onDateSelected = { navController.navigate("timePicker") }
            )
        }
        composable("timePicker") {
            TimePickerScreen(
                viewModel = viewModel,
                onTimeSelected = { /* Final action */ }
            )
        }
    }
}
