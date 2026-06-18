package com.universitaspertamina.reflow.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import com.universitaspertamina.reflow.R

class FocusBottomSheet : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_focus_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Mencari tombol berdasarkan ID
        val btnStartFocus = view.findViewById<MaterialButton>(R.id.btnStartFocus)

        // 2. Memberikan aksi saat tombol diklik
        btnStartFocus?.setOnClickListener {
            // Pindah ke layar Timer (sesuai ID action di nav_graph.xml)
            findNavController().navigate(R.id.action_focusSheet_to_timerFragment)

            // Menutup layar Bottom Sheet yang sedang mengambang ini
            dismiss()
        }
    }
}