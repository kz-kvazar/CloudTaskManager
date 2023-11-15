package com.qazar.taskmanager.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.qazar.taskmanager.Task
import java.util.*

class TaskFormView {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun TaskForm(
        task: Task?
    ) {
        var title by remember { mutableStateOf("") }
        var description by remember { mutableStateOf("") }
        val selectedDate by remember { mutableStateOf<Date?>(null) }

        Column(modifier = Modifier.fillMaxSize().background(Color.Green)) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(text = "Task title:", modifier = Modifier.fillMaxWidth(0.2f))
            }
            BasicTextField(
                value = title, onValueChange = { newTitle ->
                    title = newTitle
                }, singleLine = true, modifier = Modifier.fillMaxWidth()
            )

            Row(modifier = Modifier.fillMaxWidth()) {
                Text(text = "Task description", modifier = Modifier.fillMaxWidth(0.2f))
            }
            BasicTextField(
                value = description, onValueChange = { newDescription ->
                    description = newDescription
                }, singleLine = true, modifier = Modifier.fillMaxWidth()
            )

            Row(modifier = Modifier.fillMaxWidth()) {
                Text(text = "Expiration date", modifier = Modifier.fillMaxWidth(0.2f))
            }
            val state = rememberDatePickerState(initialDisplayMode = DisplayMode.Companion.Input)
            DatePicker(
                state = state,
            )

            Button(onClick = {
//                val newTask = Task(
//                    title = title, description = description, time = selectedDate?.time
//                )
                // onTaskCreated(newTask)
                //title = ""
                //description = ""
                //selectedDate = null
            }) {
                Text(text = "Create Task")
            }
        }
    }

}

