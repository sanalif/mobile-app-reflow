package com.universitaspertamina.reflow.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController // Tambahkan ini
import com.google.android.material.floatingactionbutton.FloatingActionButton // Tambahkan ini
import com.universitaspertamina.reflow.R

class ListFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    // Tambahkan fungsi onViewCreated di bawah ini
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Mencari tombol fabAdd berdasarkan ID di layout
        val fabAdd = view.findViewById<FloatingActionButton>(R.id.fabAdd)

        // Memberikan aksi klik untuk navigasi
        fabAdd.setOnClickListener { v ->
            v.findNavController().navigate(R.id.action_listFragment_to_addTaskFragment)
        }
    }
}