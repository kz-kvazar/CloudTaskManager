package com.qazar.taskmanager.compose

import android.widget.DatePicker
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.qazar.taskmanager.Task
import androidx.compose.foundation.layout.*
import java.util.*

class TaskFormView {
    @Composable
    fun TaskForm(
        onTaskCreated: (Task) -> Unit
    ) {
        var title by remember { mutableStateOf("") }
        var description by remember { mutableStateOf("") }
        var selectedDate by remember { mutableStateOf<Date?>(null) }

        Column(modifier = Modifier.fillMaxSize()) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(text = "Title")
            }
            BasicTextField(
                value = title,
                onValueChange = { newTitle ->
                    title = newTitle
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Row(modifier = Modifier.fillMaxWidth()) {
                Text(text = "Description")
            }
            BasicTextField(
                value = description,
                onValueChange = { newDescription ->
                    description = newDescription
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Row(modifier = Modifier.fillMaxWidth()) {
                Text(text = "Due Date")
            }
            DatePicker(
                selectedDate = selectedDate,
                onDateSelected = { date ->
                    selectedDate = date
                },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    val newTask = Task(
                        title = title,
                        description = description,
                        time = selectedDate?.time
                    )
                    onTaskCreated(newTask)
                    title = ""
                    description = ""
                    selectedDate = null
                }
            ) {
                Text(text = "Create Task")
            }
        }
    }

}

