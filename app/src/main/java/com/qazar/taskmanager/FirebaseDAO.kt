package com.qazar.taskmanager

import android.content.Context
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class FirebaseDAO(val uid: String) {
    lateinit var database: DatabaseReference
    private var scope: CoroutineScope = CoroutineScope(Dispatchers.IO)

    fun connect(context: Context) {
        //Firebase.database.setPersistenceEnabled(false)
        FirebaseApp.initializeApp(context)
        database = Firebase.database.reference.child("users").child(uid)
    }

    fun addTask(task: Task) {
        scope.launch {
            val taskRef = database.push()
            val key = taskRef.key// Генерировать уникальный ключ
            task.key = key
            task.key?.let {
                taskRef.setValue(task).await()
            }
        }
    }

    fun removeTask(task: Task) {
        scope.launch {
            task.key?.let { database.child(it).removeValue().await() }
        }
    }

    suspend fun getAllTask(): List<Task> = withContext(Dispatchers.IO) {
        val result = mutableListOf<Task>()
        val dataSnapshotTask = database.get().await()
        for (taskSnapshot in dataSnapshotTask.children) {
            val task = taskSnapshot.getValue(Task::class.java)
            task?.let { result.add(it) }
        }
        result
    }
}
