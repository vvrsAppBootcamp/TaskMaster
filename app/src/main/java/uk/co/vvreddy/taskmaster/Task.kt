package uk.co.vvreddy.taskmaster

data class Task(
    val id: Long,
    val title: String,
    var isCompleted: Boolean
)
