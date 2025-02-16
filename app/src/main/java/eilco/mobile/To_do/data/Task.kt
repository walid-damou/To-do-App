package eilco.mobile.To_do.data

data class Task(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val time: String = "",
    val date: String = "",
    val priority: String = "",
    val isCompleted: Boolean = false
)
