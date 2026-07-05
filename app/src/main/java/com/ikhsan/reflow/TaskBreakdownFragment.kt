package com.ikhsan.reflow

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton

class TaskBreakdownFragment : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate layout yang sudah kita buat
        return inflater.inflate(R.layout.fragment_task_breakdown, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inisialisasi komponen UI
        val etSmallStep = view.findViewById<EditText>(R.id.etSmallStep)
        val btnMulaiFokus = view.findViewById<MaterialButton>(R.id.btnMulaiFokus)

        // Aksi ketika tombol diklik
        btnMulaiFokus.setOnClickListener {
            val smallStep = etSmallStep.text.toString()

            if (smallStep.isNotEmpty()) {
                // Pindah ke FocusSessionActivity
                val intent = Intent(activity, FocusSessionActivity::class.java)
                intent.putExtra("SMALL_STEP", smallStep)
                startActivity(intent)

                // Tutup bottom sheet
                dismiss()
            } else {
                etSmallStep.error = "Langkah kecil harus diisi!"
            }
        }
    }
}

//zakkha