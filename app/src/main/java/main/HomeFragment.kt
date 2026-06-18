package com.universitaspertamina.reflow.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController // Penting: untuk navigasi
import com.google.android.material.button.MaterialButton // Penting: untuk tombol
import com.universitaspertamina.reflow.R

class HomeFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Menghubungkan tombol dari XML ke kode
        val btnStart = view.findViewById<MaterialButton>(R.id.btnStart)

        // 2. Menambahkan aksi klik
        btnStart.setOnClickListener { v ->
            // Pastikan ID action ini sudah ada di nav_graph.xml kamu
            v.findNavController().navigate(R.id.action_homeFragment_to_focusBottomSheet)
        }
    }
}