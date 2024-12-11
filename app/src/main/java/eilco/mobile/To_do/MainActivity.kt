package eilco.mobile.To_do

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import eilco.mobile.To_do.ui.screens.AppNavigation
import androidx.activity.viewModels
import eilco.mobile.To_do.ui.ThemeViewModel

class MainActivity : ComponentActivity() {
    private val themeViewModel by viewModels<ThemeViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppNavigation(viewModel = themeViewModel)
        }
    }
}

