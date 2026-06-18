package com.universitaspertamina.reflow.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController // Penting untuk navigasi
import com.universitaspertamina.reflow.R

class ProfileFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Mencari kartu pencapaian berdasarkan ID yang ada di fragment_profile.xml
        val cvStats = view.findViewById<View>(R.id.cvStats)

        // Memberikan listener agar bisa diklik
        cvStats.setOnClickListener { v ->
            // Melakukan navigasi ke HistoryFragment menggunakan ID action yang sudah didaftarkan di nav_graph.xml
            v.findNavController().navigate(R.id.action_profileFragment_to_historyFragment)
        }
    }
}