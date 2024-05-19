package uk.co.vvreddy.taskmaster

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Add
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
import androidx.compose.ui.text.font.FontWeight
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
    var sampleTasks = remember {
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

            TaskList(taskList = sampleTasks)
        }
    }
}


@Composable
fun TaskList(taskList: List<Task>) {
    LazyColumn {
        items(taskList){
            task -> TaskItem(task)
        }
    }
}

@Composable
fun TaskItem(task: Task) {
    var taskState by remember { mutableStateOf(task.isCompleted) }
    Row(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = task.title,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp)
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