package eilco.mobile.To_do.data

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

object TaskRepository {
    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance()

    fun saveTask(
        title: String,
        description: String,
        time: String,
        date: String,
        priority: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val userId = auth.currentUser?.uid
        if (userId == null) {
            onFailure(Exception("User not authenticated"))
            return
        }

        val taskId = database.reference.child("tasks").child(userId).push().key
        if (taskId == null) {
            onFailure(Exception("Failed to generate task ID"))
            return
        }

        if (title.isBlank() || description.isBlank() || time.isBlank() || date.isBlank() || priority.isBlank()) {
            onFailure(Exception("All fields are required"))
            return
        }

        val task = Task(
            id = taskId,
            title = title,
            description = description,
            time = time,
            date = date,
            priority = priority
        )

        database.reference
            .child("tasks")
            .child(userId)
            .child(taskId)
            .setValue(task)
            .addOnSuccessListener {
                Log.d("TaskRepository", "Task saved successfully: $taskId")
                onSuccess()
            }
            .addOnFailureListener {
                Log.e("TaskRepository", "Failed to save task: ${it.message}")
                onFailure(it)
            }
    }
}
