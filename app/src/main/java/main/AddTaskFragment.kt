package com.universitaspertamina.reflow.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.google.android.material.button.MaterialButton
import com.universitaspertamina.reflow.R

class AddTaskFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_task, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnSave = view.findViewById<MaterialButton>(R.id.btnSave)
        val tvCancel = view.findViewById<TextView>(R.id.tvCancel)
        val etTaskInput = view.findViewById<EditText>(R.id.etTaskInput)

        // Tombol Simpan
        btnSave.setOnClickListener { v ->
            val task = etTaskInput.text.toString().trim()
            if (task.isEmpty()) {
                Toast.makeText(requireContext(), "Tugasnya diisi dulu ya!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Tugas '$task' berhasil disimpan", Toast.LENGTH_SHORT).show()
                v.findNavController().navigateUp() // Kembali ke daftar tugas
            }
        }

        // Tombol Batal
        tvCancel.setOnClickListener { v ->
            v.findNavController().navigateUp() // Kembali tanpa menyimpan
        }
    }
}