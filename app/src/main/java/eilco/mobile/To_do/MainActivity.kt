package eilco.mobile.To_do

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import eilco.mobile.To_do.ui.AppPager

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppPager()
        }
    }
}
