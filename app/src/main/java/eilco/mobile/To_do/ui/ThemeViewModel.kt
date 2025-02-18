package eilco.mobile.To_do.ui

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.compose.ui.graphics.Color
import com.google.firebase.database.FirebaseDatabase
import eilco.mobile.To_do.ui.screens.Task
import com.google.firebase.auth.FirebaseAuth

class ThemeViewModel : ViewModel() {
    val themeColor: MutableState<Color> = mutableStateOf(Color(0xFF24A19C)) // Default color

    var selectedTask: MutableState<Task?> = mutableStateOf(null)
    val currentUserId: MutableState<String?> = mutableStateOf(null)

    fun fetchUserId() {
        val user = FirebaseAuth.getInstance().currentUser
        currentUserId.value = user?.uid
    }

    fun setThemeColor(color: Color) {
        themeColor.value = color
    }

    fun setSelectedTask(task: Task) {
        selectedTask.value = task
    }

    fun fetchThemeColor(userId: String) {
        val database = FirebaseDatabase.getInstance()
        val userRef = database.getReference("users/$userId/theme")

        userRef.get()
            .addOnSuccessListener { snapshot ->
                val colorName = snapshot.getValue(String::class.java)
                val color = when (colorName) {
                    "Green" -> Color(0xFF5CAF54)
                    "Black" -> Color(0xFF000000)
                    "Red" -> Color(0xFFFF4C4C)
                    "Blue" -> Color(0xFF4A90E2)
                    else -> Color(0xFF24A19C)
                }
                setThemeColor(color)
            }
            .addOnFailureListener { exception ->
                Log.e("ThemeFetch", "Failed to fetch theme: ${exception.message}")
            }
    }
}
