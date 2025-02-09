package eilco.mobile.To_do.ui

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.pager.*
import eilco.mobile.To_do.ui.screens.CreateAccountScreen
import eilco.mobile.To_do.ui.screens.LoginScreen
import eilco.mobile.To_do.ui.screens.InboxScreen
import eilco.mobile.To_do.ui.screens.OnboardingScreen

@OptIn(ExperimentalPagerApi::class)
@Composable
fun AppPager(
    onLoginSuccess: () -> Unit,
    onCreateAccount: () -> Unit,
    viewModel: ThemeViewModel
) {
    val pagerState = rememberPagerState()
    val navController = rememberNavController()
    Column(modifier = Modifier.fillMaxSize()) {
        HorizontalPager(
            count = 3,
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) { page ->
            when (page) {
                // 0 -> OnboardingScreen()

                0 -> InboxScreen(
                    viewModel = viewModel
                )
                // 0 -> TaskDetailScreen("1", viewModel = viewModel)
                1 -> LoginScreen(
                    onLoginSuccess = { onLoginSuccess() },
                    viewModel = viewModel
                )
                2 -> CreateAccountScreen(
                    onFinish = { onCreateAccount() },
                    viewModel = viewModel
                )
            }
        }

        HorizontalPagerIndicator(
            pagerState = pagerState,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(16.dp)
        )
    }
}
