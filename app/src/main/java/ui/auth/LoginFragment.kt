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

class LoginFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val etEmail = view.findViewById<TextInputEditText>(R.id.etEmail)
        val etPassword = view.findViewById<TextInputEditText>(R.id.etPassword)
        val btnLogin = view.findViewById<MaterialButton>(R.id.btnLogin)
        val tvRegister = view.findViewById<TextView>(R.id.tvRegister)
        val tvForgotPassword = view.findViewById<TextView>(R.id.tvForgotPassword)

        // FUNGSI UTAMA: Tombol "Mulai Perjalanan"
        btnLogin.setOnClickListener { v ->
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            // Cek apakah data sudah diisi
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "Email dan Password tidak boleh kosong ya!", Toast.LENGTH_SHORT).show()
            } else {
                // 1. Munculkan konfirmasi terlebih dahulu
                Toast.makeText(requireContext(), "Memulai perjalanan...", Toast.LENGTH_SHORT).show()

                // 2. Langsung pindahkan layar ke Menu Home
                v.findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
            }
        }

        // Pindah ke halaman Register
        tvRegister.setOnClickListener { v ->
            v.findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        // Pindah ke halaman Lupa Password
        tvForgotPassword.setOnClickListener { v ->
            v.findNavController().navigate(R.id.action_loginFragment_to_forgotPasswordFragment)
        }
    }
}