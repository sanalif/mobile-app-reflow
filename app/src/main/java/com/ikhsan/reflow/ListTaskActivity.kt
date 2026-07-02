package com.ikhsan.reflow

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ListTaskActivity : AppCompatActivity() {

    private lateinit var rv: RecyclerView
    private lateinit var adapter: TaskAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_task)

        NavbarHelper.setupNavbar(this, "list")
        setupBottomNavigation()

        rv = findViewById(R.id.rvTasks)
        rv.layoutManager = LinearLayoutManager(this)

        adapter = TaskAdapter(TaskRepository.taskList) { task, action ->
            when (action) {
                "mulai" -> {
                    val breakdownSheet = TaskBreakdownFragment()
                    // Jika perlu mengirim ID task ke breakdown:
                    // val bundle = Bundle().apply { putString("taskId", task.id) }
                    // breakdownSheet.arguments = bundle
                    breakdownSheet.show(supportFragmentManager, "BreakdownTag")
                }
                "edit" -> {
                    val editSheet = AddTaskFragment()
                    // PENTING: Panggil setTask agar AddTaskFragment tahu ini mode EDIT
                    editSheet.setTask(task)
                    editSheet.show(supportFragmentManager, "EditTag")
                }
                "hapus" -> {
                    AlertDialog.Builder(this)
                        .setTitle("Hapus Tugas")
                        .setMessage("Yakin ingin menghapus '${task.title}'?")
                        .setPositiveButton("Hapus") { _, _ ->
                            TaskRepository.deleteTask(task)
                            adapter.updateData(TaskRepository.taskList)
                        }
                        .setNegativeButton("Batal", null)
                        .show()
                }
            }
        }
        rv.adapter = adapter

        supportFragmentManager.setFragmentResultListener("task_request", this) { _, _ ->
            TaskRepository.taskList.sortBy { it.deadline.toDate() }
            adapter.updateData(TaskRepository.taskList)
        }

        val fabAddTask = findViewById<FloatingActionButton>(R.id.fabAddTask)
        fabAddTask.setOnClickListener {
            val addTaskSheet = AddTaskFragment()
            addTaskSheet.show(supportFragmentManager, "AddTaskTag")
        }
    }

    override fun onResume() {
        super.onResume()
        TaskRepository.taskList.sortBy { it.deadline.toDate() }
        adapter.updateData(TaskRepository.taskList)
    }

    private fun setupBottomNavigation() {
        val ivHome = findViewById<ImageView>(R.id.icHome)
        val ivList = findViewById<ImageView>(R.id.icList)
        val ivProfile = findViewById<ImageView>(R.id.icProfile)

        ivHome.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0)
            finish()
        }

        ivList.setOnClickListener {
            Toast.makeText(this, "Anda sudah di List", Toast.LENGTH_SHORT).show()
        }

        ivProfile.setOnClickListener {
            val intent = Intent(this, ProfileUserActivity::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0)
            finish()
        }
    }
}