package com.universitaspertamina.reflow.ui.auth

import android.os.Bundle
import android.os.Handler
import android.os.Looper
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

class RegisterFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val etName = view.findViewById<TextInputEditText>(R.id.etName)
        val etEmail = view.findViewById<TextInputEditText>(R.id.etEmail)
        val etPassword = view.findViewById<TextInputEditText>(R.id.etPassword)
        val btnRegister = view.findViewById<MaterialButton>(R.id.btnRegister)
        val tvBackToLogin = view.findViewById<TextView>(R.id.tvBackToLogin)

        btnRegister.setOnClickListener {
            val name = etName.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "Lengkapi semua data dulu ya!", Toast.LENGTH_SHORT).show()
            } else {
                // MENGHIDUPKAN FITUR FIGMA: Mengubah teks tombol saat sukses
                btnRegister.text = "Akun berhasil dibuat, selamat yaaa!"

                // Opsional: Kembali ke halaman login setelah 2 detik
                Handler(Looper.getMainLooper()).postDelayed({
                    view.findNavController().navigateUp()
                }, 2000)
            }
        }

        // Kembali ke layar Login
        tvBackToLogin.setOnClickListener { v ->
            v.findNavController().navigateUp()
        }
    }
}