package com.universitaspertamina.reflow.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.universitaspertamina.reflow.R

class ForgotPasswordFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_forgot_password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val etEmail = view.findViewById<TextInputEditText>(R.id.etEmail)
        val btnSubmit = view.findViewById<MaterialButton>(R.id.btnSubmit)
        val tvBackToLogin = view.findViewById<TextView>(R.id.tvBackToLogin)

        // Tombol Kirim Instruksi
        btnSubmit.setOnClickListener {
            val email = etEmail.text.toString().trim()
            if (email.isEmpty()) {
                Toast.makeText(requireContext(), "Masukkan email kamu dulu ya!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Instruksi pemulihan dikirim ke $email", Toast.LENGTH_LONG).show()
            }
        }

        // Teks "Kembali ke Login" (Fungsinya persis seperti menekan tombol Back di HP)
        tvBackToLogin.setOnClickListener { v ->
            v.findNavController().navigateUp()
        }
    }
}