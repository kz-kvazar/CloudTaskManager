package com.qazar.taskmanager.activity

import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.qazar.taskmanager.Task
import com.qazar.taskmanager.compose.TaskFormView
import com.qazar.taskmanager.ui.theme.TaskManagerTheme

class TaskFormActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContent {
            TaskManagerTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    val view = TaskFormView()
                    val taskObject = intent.getParcelableExtra<Task>("task")
                    view.TaskForm(taskObject)
                }
            }
        }
    }
}