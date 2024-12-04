package eilco.mobile.To_do.ui

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.*
import eilco.mobile.To_do.ui.screens.CreateAccountScreen
import eilco.mobile.To_do.ui.screens.LoginScreen
import eilco.mobile.To_do.ui.screens.OnboardingScreen

@OptIn(ExperimentalPagerApi::class)
@Composable
fun AppPager() {
    // Create the pager state
    val pagerState = rememberPagerState()

    Column(modifier = Modifier.fillMaxSize()) {
        HorizontalPager(
            count = 3,
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) { page ->
            when (page) {
                0 -> OnboardingScreen()
                1 -> LoginScreen()
                2 -> CreateAccountScreen()
            }
        }

        // Add a pager indicator
        HorizontalPagerIndicator(
            pagerState = pagerState,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(16.dp)
        )
    }
}
