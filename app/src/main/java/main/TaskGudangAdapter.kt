package com.universitaspertamina.reflow.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.universitaspertamina.reflow.R // <-- Jembatan ajaib untuk menyambungkan ke XML

class TaskGudangAdapter(private val tasks: MutableList<String>) :
    RecyclerView.Adapter<TaskGudangAdapter.TaskViewHolder>() {

    class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTaskName: TextView = view.findViewById(R.id.tvTaskName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_task_gudang, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val taskText = tasks[position]
        holder.tvTaskName.text = taskText
    }

    override fun getItemCount(): Int {
        return tasks.size
    }
}