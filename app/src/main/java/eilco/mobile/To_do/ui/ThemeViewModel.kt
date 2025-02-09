package eilco.mobile.To_do.ui

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.compose.ui.graphics.Color
import com.google.firebase.database.FirebaseDatabase
import eilco.mobile.To_do.ui.screens.Task

class ThemeViewModel : ViewModel() {
    val themeColor: MutableState<Color> = mutableStateOf(Color(0xFF24A19C)) // Default color
    var tasks = listOf(
        Task("1","Masyla Website Project", "Priority task 1", "Le Lorem Ipsum est simplement du faux texte employé dans la composition et la mise en page avant impression. Le Lorem Ipsum est le faux texte standard de l'imprimerie depuis les années 1500, quand un imprimeur anonyme assembla ", "8:30 PM", 1, 2, "Mon, 19 Jul 2022"),
        Task("2","Medical Design System", "Priority task 3", "another test description", "8:30 PM", 1, 2, "Mon, 19 Jul 2022")
    )
    var selectedTask: MutableState<Task?> = mutableStateOf(null)
    fun setThemeColor(color: Color) {
        themeColor.value = color
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
