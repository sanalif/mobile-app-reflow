package com.universitaspertamina.reflow.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.universitaspertamina.reflow.R

class RecoveryFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_recovery, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Jika tombol centang di bawah ditekan, kembali ke layar Timer
        view.findViewById<View>(R.id.btnResume).setOnClickListener {
            findNavController().navigateUp() // Ini otomatis menutup layar Recovery dan kembali ke Timer
        }
    }
}