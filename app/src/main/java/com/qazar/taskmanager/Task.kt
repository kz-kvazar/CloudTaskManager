package com.qazar.taskmanager

data class Task(
    var time: Long? = null, var title: String? = null,
    var description: String? = null, var isDone: Boolean? = null,
    var key: String? = null
)