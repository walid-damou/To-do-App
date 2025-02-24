package eilco.mobile.To_do

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import eilco.mobile.To_do.ui.ThemeViewModel
import eilco.mobile.To_do.ui.AppTheme
import eilco.mobile.To_do.ui.AppNavigation
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {
    private val themeViewModel by viewModels<ThemeViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)

        setContent {
            val isUserLoggedIn = FirebaseAuth.getInstance().currentUser != null
            val themeColor = themeViewModel.themeColor.value
            AppTheme(themeColor = themeColor) {
                AppNavigation(viewModel = themeViewModel, isUserLoggedIn = isUserLoggedIn)
            }
        }
    }
}