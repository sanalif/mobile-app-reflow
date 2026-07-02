package com.ikhsan.reflow

import com.google.firebase.Timestamp

data class Task(
    val id: String = "",
    var title: String = "",
    var createdAt: Timestamp = Timestamp.now(), // taskDate
    var deadline: Timestamp = Timestamp.now(), // deadline
    var isImportant: Boolean = false, // Untuk label "Penting" atau "Santai"
    var isCompleted: Boolean = false
)