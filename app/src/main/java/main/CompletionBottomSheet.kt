package com.universitaspertamina.reflow.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import com.universitaspertamina.reflow.R

class CompletionBottomSheet : BottomSheetDialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_completion_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Tombol Kembali ke Beranda
        val btnBackToHome = view.findViewById<View>(R.id.tvBackToHome)
        btnBackToHome?.setOnClickListener {
            findNavController().navigate(R.id.homeFragment)
            dismiss()
        }

        // Tombol Bagikan Pencapaian
        val btnShare = view.findViewById<MaterialButton>(R.id.btnShare)
        btnShare?.setOnClickListener {
            shareAchievement()
        }
    }

    private fun shareAchievement() {
        // Teks yang akan dibagikan ke pengguna lain
        val shareText = "Yay! Saya baru saja menyelesaikan langkah fokus saya di aplikasi Reflow. Perjalanan yang indah dimulai dari satu langkah kecil!"

        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, shareText)
            type = "text/plain"
        }
        startActivity(Intent.createChooser(shareIntent, "Bagikan pencapaian via"))
    }
}