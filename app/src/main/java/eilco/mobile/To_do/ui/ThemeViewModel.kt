package eilco.mobile.To_do.ui

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.compose.ui.graphics.Color
import com.google.firebase.database.FirebaseDatabase

class ThemeViewModel : ViewModel() {
    val themeColor: MutableState<Color> = mutableStateOf(Color(0xFF24A19C))

    fun setThemeColor(color: Color) {
        themeColor.value = color
    }
    fun fetchThemeColor(userId: String, viewModel: ThemeViewModel) {
        val database = FirebaseDatabase.getInstance()
        val userRef = database.getReference("users/$userId/theme")

        userRef.get().addOnSuccessListener { snapshot ->
            val colorName = snapshot.getValue(String::class.java)
            val color = when (colorName) {
                "Green" -> Color(0xFF5CAF54)
                "Black" -> Color(0xFF000000)
                "Red" -> Color(0xFFFF4C4C)
                "Blue" -> Color(0xFF4A90E2)
                else -> Color.Gray
            }
            viewModel.setThemeColor(color)
        }.addOnFailureListener {
            Log.e("ThemeFetch", "Failed to fetch theme: ${it.message}")
        }
    }
}
