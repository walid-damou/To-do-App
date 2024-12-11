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
                onCreateAccount = { navController.navigate("createAccount") }
            )
        }
        composable("createAccount") {
            CreateAccountScreen(
                onFinish = { navController.navigate("chooseTheme") }
            )
        }
        composable("chooseTheme") {
            ChooseThemeScreen(
                userId = "user5",
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
            AddTaskScreen(onProceed = { navController.navigate("priorityPicker") })
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
