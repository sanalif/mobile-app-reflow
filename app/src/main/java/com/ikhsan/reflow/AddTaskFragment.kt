package com.ikhsan.reflow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class AddTaskFragment : BottomSheetDialogFragment() {

    private var selectedDeadline: Date = Date()
    private val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    private var taskToEdit: Task? = null

    fun setTask(task: Task) {
        this.taskToEdit = task
        this.selectedDeadline = task.deadline.toDate()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_task, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val etTaskTitle = view.findViewById<EditText>(R.id.etTaskTitle)
        val tvSelectedDateInfo = view.findViewById<TextView>(R.id.tvSelectedDateInfo)
        val btnSimpan = view.findViewById<MaterialButton>(R.id.btnSimpan)
        val tvBatal = view.findViewById<TextView>(R.id.tvBatal)

        // 1. Setup UI awal (label HARI INI, BESOK, dll)
        setupInitialUI(view)

        // 2. Jika mode Edit, isi data lama
        taskToEdit?.let { task ->
            etTaskTitle.setText(task.title)
            tvSelectedDateInfo.text = "Deadline: ${dateFormat.format(task.deadline.toDate())}"
        } ?: run {
            tvSelectedDateInfo.text = ""
        }

        // 3. Setup Listener untuk memilih tanggal
        setupDeadlineOptions(view) { date ->
            selectedDeadline = date
            tvSelectedDateInfo.text = "Deadline: ${dateFormat.format(selectedDeadline)}"
        }

        btnSimpan.setOnClickListener {
            val title = etTaskTitle.text.toString()
            if (title.isNotEmpty()) {
                if (taskToEdit != null) {
                    taskToEdit?.let {
                        it.title = title
                        it.deadline = Timestamp(selectedDeadline)
                    }
                    Toast.makeText(context, "Tugas berhasil diupdate!", Toast.LENGTH_SHORT).show()
                } else {
                    val newTask = Task(
                        id = System.currentTimeMillis().toString(),
                        title = title,
                        createdAt = Timestamp.now(),
                        deadline = Timestamp(selectedDeadline),
                        isImportant = false,
                        isCompleted = false
                    )
                    TaskRepository.addTask(newTask) {
                        Toast.makeText(context, "Tugas berhasil ditambahkan!", Toast.LENGTH_SHORT).show()
                    }
                }
                parentFragmentManager.setFragmentResult("task_request", Bundle())
                dismiss()
            } else {
                etTaskTitle.error = "Tugas tidak boleh kosong"
            }
        }
        tvBatal.setOnClickListener { dismiss() }
    }

    private fun setupInitialUI(view: View) {
        val cal = Calendar.getInstance()
        fun setBox(containerId: Int, label: String, value: String, isIcon: Boolean = false) {
            val v = view.findViewById<View>(containerId)
            v.findViewById<TextView>(R.id.tvOptionLabel).text = label
            val tvValue = v.findViewById<TextView>(R.id.tvOptionValue)
            val ivIcon = v.findViewById<ImageView>(R.id.ivOptionIcon)
            if (isIcon) {
                tvValue.visibility = View.GONE
                ivIcon.visibility = View.VISIBLE
            } else {
                tvValue.text = value
                tvValue.visibility = View.VISIBLE
                ivIcon.visibility = View.GONE
            }
        }
        setBox(R.id.containerHariIni, "HARI INI", cal.get(Calendar.DAY_OF_MONTH).toString())
        cal.add(Calendar.DAY_OF_YEAR, 1)
        setBox(R.id.containerBesok, "BESOK", cal.get(Calendar.DAY_OF_MONTH).toString())
        setBox(R.id.containerPilih, "PILIH", "", isIcon = true)
    }

    private fun setupDeadlineOptions(view: View, onDateSelected: (Date) -> Unit) {
        val containerHariIni = view.findViewById<View>(R.id.containerHariIni)
        val containerBesok = view.findViewById<View>(R.id.containerBesok)
        val containerPilih = view.findViewById<View>(R.id.containerPilih)

        fun updateUI(v: View, label: String, value: String, isIcon: Boolean = false) {
            listOf(containerHariIni, containerBesok, containerPilih).forEach {
                it.setBackgroundResource(R.drawable.bg_border_option)
            }
            v.setBackgroundResource(R.drawable.bg_selected_option)
            v.findViewById<TextView>(R.id.tvOptionLabel).text = label
            val tvValue = v.findViewById<TextView>(R.id.tvOptionValue)
            val ivIcon = v.findViewById<ImageView>(R.id.ivOptionIcon)
            if (isIcon) {
                tvValue.visibility = View.GONE
                ivIcon.visibility = View.VISIBLE
            } else {
                tvValue.text = value
                tvValue.visibility = View.VISIBLE
                ivIcon.visibility = View.GONE
            }
        }

        containerHariIni.setOnClickListener {
            val date = Date()
            updateUI(it, "HARI INI", SimpleDateFormat("dd", Locale.getDefault()).format(date))
            onDateSelected(date)
        }

        containerBesok.setOnClickListener {
            val cal = Calendar.getInstance()
            cal.add(Calendar.DAY_OF_YEAR, 1)
            updateUI(it, "BESOK", SimpleDateFormat("dd", Locale.getDefault()).format(cal.time))
            onDateSelected(cal.time)
        }

        containerPilih.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker().build()
            datePicker.addOnPositiveButtonClickListener { selection ->
                val date = Date(selection)
                updateUI(it, "TANGGAL", SimpleDateFormat("dd MMM", Locale.getDefault()).format(date))
                onDateSelected(date)
            }
            datePicker.show(parentFragmentManager, "DATE_PICKER")
        }
    }
}