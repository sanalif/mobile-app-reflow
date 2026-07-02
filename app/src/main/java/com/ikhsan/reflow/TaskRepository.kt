package com.ikhsan.reflow

import com.google.firebase.Timestamp
import java.util.Date

object TaskRepository {
    // Diubah menjadi mutableListOf agar bisa ditambah datanya secara dinamis
    val taskList = mutableListOf(
        Task(
            id = "1",
            title = "Review research documents",
            createdAt = Timestamp.now(),
            deadline = Timestamp(Date(System.currentTimeMillis() + 86400000)), // Besok
            isImportant = true,
            isCompleted = false
        ),
        Task(
            id = "2",
            title = "Weekly team reflection",
            createdAt = Timestamp.now(),
            deadline = Timestamp(Date(System.currentTimeMillis() + 172800000)), // Lusa
            isImportant = false,
            isCompleted = false
        )
    )

    // Fungsi untuk menambah tugas baru dan langsung mengurutkannya
    // Menambahkan parameter onTaskAdded sebagai callback agar UI segera diperbarui
    fun addTask(task: Task, onTaskAdded: () -> Unit) {
        taskList.add(task)
        // Mengurutkan berdasarkan deadline terdekat (ascending)
        taskList.sortBy { it.deadline.toDate() }

        // Callback dipanggil segera setelah data masuk dan diurutkan
        onTaskAdded()
    }

    fun deleteTask(task: Task) {
        taskList.remove(task)
    }

    // Fungsi untuk mendapatkan tugas dengan deadline terdekat
    fun getClosestTask(): Task? {
        return taskList.minByOrNull { it.deadline.toDate() }
    }
}