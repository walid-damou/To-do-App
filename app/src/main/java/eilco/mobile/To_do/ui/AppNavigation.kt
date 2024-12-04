package eilco.mobile.To_do.ui.screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun AppNavigation() {
    val navController: NavHostController = rememberNavController()

    NavHost(navController, startDestination = "onboarding") {
        composable("onboarding") { OnboardingScreen() }
        composable("createAccount") { CreateAccountScreen() }
        composable("login") { LoginScreen() }
    }
}
