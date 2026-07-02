package com.ikhsan.reflow

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Locale

class TaskAdapter(
    private var taskList: List<Task>,
    private val onTaskAction: (Task, String) -> Unit
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    fun updateData(newList: List<Task>) {
        this.taskList = newList
        notifyDataSetChanged()
    }

    class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.tvTaskTitle)
        val deadline: TextView = view.findViewById(R.id.tvDeadline)
        val createdAt: TextView = view.findViewById(R.id.tvTaskDate)

        // Menambahkan referensi ikon baru
        val btnMulai: ImageView = view.findViewById(R.id.btnMulai)
        val btnEdit: ImageView = view.findViewById(R.id.btnEdit)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = taskList[position]
        val formatter = SimpleDateFormat("dd MMM, HH:mm", Locale.getDefault())

        holder.title.text = task.title
        holder.deadline.text = "Deadline: ${formatter.format(task.deadline.toDate())}"
        holder.createdAt.text = "Dibuat: ${formatter.format(task.createdAt.toDate())}"

        // Listener untuk tombol Play/Mulai
        holder.btnMulai.setOnClickListener {
            onTaskAction(task, "mulai")
        }

        // Listener untuk tombol Edit
        holder.btnEdit.setOnClickListener {
            onTaskAction(task, "edit")
        }

        // Listener untuk Hapus (via Long Click)
        holder.itemView.setOnLongClickListener {
            onTaskAction(task, "hapus")
            true // Mengembalikan true berarti event sudah ditangani
        }
    }

    override fun getItemCount() = taskList.size
}