package com.universitaspertamina.reflow.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButton
import com.universitaspertamina.reflow.R

class TimerFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_timer, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Jika tombol daun (recovery) diklik
        view.findViewById<View>(R.id.btnRecovery).setOnClickListener {
            findNavController().navigate(R.id.action_timerFragment_to_recoveryFragment)
        }

        // 2. Jika tombol Selesai diklik
        view.findViewById<MaterialButton>(R.id.btnFinish).setOnClickListener {
            findNavController().navigate(R.id.action_timerFragment_to_completionBottomSheet)
        }
    }
}