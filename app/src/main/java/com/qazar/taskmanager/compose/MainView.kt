package com.qazar.taskmanager.compose

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.qazar.taskmanager.FirebaseDAO
import com.qazar.taskmanager.Task

class MainView {
    private val taskList = mutableStateListOf<Task>()
    var firebaseDAO: FirebaseDAO? = null
        set(value) {
            field = value
            firebaseDAO?.database?.addChildEventListener(createChildEventListener())
        }

    private fun createChildEventListener(): ChildEventListener {
        return object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
                Log.d("TASK", "onChildAdded:" + dataSnapshot.key!!)
                val task = dataSnapshot.getValue(Task::class.java)
                task?.let { taskList.add(it) }
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, previousChildName: String?) {
                Log.d("TASK", "onChildChanged: ${dataSnapshot.key}")

            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                Log.d("TASK", "onChildRemoved:" + snapshot.key!!)
                val task = snapshot.getValue(Task::class.java)
                task?.let { taskList.remove(it) }
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                Log.d("TASK", "onChildMoved:" + snapshot.key!!)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("TASK", "onCancelled: $error")
            }
        }
    }


    //@Preview(showBackground = true)
    @Composable
    fun Preview(
        imgTask: Int,
        imgDelete: Int,
        imgDoCloud: Int,
        imgNotCloud: Int,
    ) {
        val list = remember { taskList }
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier.fillMaxHeight(0.9f).fillMaxWidth().background(color = Color.Blue)
            ) {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(list) { task ->
                        ItemRow(
                            task = task,
                            imgTask = imgTask,
                            imgDelete = imgDelete,
                            imgDoCloud = imgDoCloud,
                            imgNotCloud = imgNotCloud,
                        )
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxHeight().fillMaxWidth().background(color = Color.Green),
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth(0.5f).fillMaxHeight(), contentAlignment = Alignment.CenterStart
                ) {
                    Button(onClick = {
                        //addTask(uid,)
                    }) {
                        Text(text = "Add")
                    }
                }
                Box(
                    modifier = Modifier.fillMaxWidth().fillMaxHeight(), contentAlignment = Alignment.CenterEnd
                ) {
                    Button(onClick = {
                        Log.d("TASK", "List size = ${taskList.size}")
                    }) {
                        Text(text = "Refresh")
                    }
                }

            }
        }
    }


    @Composable
    fun ItemRow(
        task: Task,
        imgTask: Int,
        imgDelete: Int,
        imgDoCloud: Int,
        imgNotCloud: Int,
    ) {
        var isExpander by remember { mutableStateOf(false) }
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                painter = painterResource(imgTask),
                contentDescription = "Task",
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(64.dp).clip(CircleShape)
            )
            Image(
                painter = painterResource(imgDoCloud),
                contentDescription = "isOnCloud",
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(24.dp).clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Box(
                modifier = Modifier.fillMaxWidth(0.8f).padding(3.dp), contentAlignment = Alignment.BottomStart
            ) {
                Column {
                    task.title?.let { Text(text = it) }
                    task.description?.let {
                        Text(text = it, maxLines = if (isExpander) Int.MAX_VALUE else 1, modifier = Modifier.clickable {
                            isExpander = !isExpander
                        })
                    }
                }
            }
            Image(painter = painterResource(imgDelete),
                contentDescription = "delete",
                contentScale = ContentScale.Crop,
                modifier = Modifier.padding(3.dp).size(64.dp).clip(CircleShape).clickable {
                    firebaseDAO?.removeTask(task)
                })
        }
    }
}
