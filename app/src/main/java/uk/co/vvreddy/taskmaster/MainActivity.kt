package uk.co.vvreddy.taskmaster

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Add
import androidx.compose.material.icons.twotone.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uk.co.vvreddy.taskmaster.ui.theme.TaskMasterTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TaskMasterTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppScaffold()
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppScaffold() {
    val tasks = listOf(
        Task(id = 1, title = "Buy groceries", isCompleted = false),
        Task(id = 2, title = "Walk the dog", isCompleted = true),
        Task(id = 3, title = "Read a book", isCompleted = false)
    )
    val sampleTasks = remember {
        mutableStateListOf<Task>()
    }

    LaunchedEffect(Unit) {
        sampleTasks.addAll(tasks)
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.app_name)) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            AddTaskInput {
                taskTitle -> sampleTasks.add(
                                        Task(
                                            id = sampleTasks.size.toLong()+1,
                                            title = taskTitle,
                                            isCompleted = false
                                        )
                )
            }

            TaskList(taskList = sampleTasks, onDelete = { task -> sampleTasks.remove(task) })
        }
    }
}


@Composable
fun TaskList(taskList: List<Task>, onDelete: (Task) -> Unit) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
    ) {
        items(taskList){
            task -> TaskItem(task, onDelete = onDelete)
            if(task != taskList.last()) Divider()
        }
    }
}

@Composable
fun TaskItem(task: Task, onDelete: (Task) -> Unit) {
    var showDialog by remember { mutableStateOf(false) }
    var taskState by remember { mutableStateOf(task.isCompleted) }

    if(showDialog){
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Delete Task") },
            text = { Text("Are you sure you want to delete this task?") },
            confirmButton = {
                TextButton(onClick = {
                    onDelete(task)
                    showDialog = false
                }) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
    Row(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = task.title,
            fontWeight = FontWeight.Bold,
            style = TextStyle(
                fontSize = 20.sp,
                textDecoration = if(taskState) TextDecoration.LineThrough else null
            ),
            modifier = Modifier.padding(16.dp)
        )
        Row(
          verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { showDialog = true }) {
                Icon(
                    imageVector = Icons.TwoTone.Delete,
                    contentDescription = "Delete the task ${task.title}"
                )
            }
            Divider(
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .fillMaxHeight()
                    .width(1.dp)
                    .padding(horizontal = 8.dp)
            )
            Switch(
                checked = taskState,
                onCheckedChange = {
                    taskState = it
                    task.isCompleted = it
                }
            )
        }
    }
}

@Composable
fun AddTaskInput(onAddTask:(String) -> Unit) {
    var taskTitle by remember { mutableStateOf("") }
    Row(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        OutlinedTextField(
            value = taskTitle,
            onValueChange = {taskTitle = it},
            label = { Text("New Task") },
            modifier = Modifier.weight(1f),
            maxLines = 1
        )
        OutlinedButton(
            onClick = {
                if(taskTitle.isNotBlank()){
                    onAddTask(taskTitle)
                    taskTitle = ""
                }
            },
            shape = CircleShape,
            contentPadding = PaddingValues(0.dp),
            modifier = Modifier.padding(8.dp),
        ){
            Icon(
                imageVector = Icons.TwoTone.Add,
                contentDescription = "Add Note"
            )
        }
    }
}
