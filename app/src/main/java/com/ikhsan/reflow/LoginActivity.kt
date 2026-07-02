package com.ikhsan.reflow

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException


class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()

        if (auth.currentUser != null) {
            startIntent()
            return
        }

        setContentView(R.layout.activity_login)

        val etContact = findViewById<EditText>(R.id.etContact)
        val etPassword = findViewById<TextInputEditText>(R.id.etPassword)
        val btnLogin = findViewById<MaterialButton>(R.id.btnLogin)
        val tvForgotPassword = findViewById<TextView>(R.id.tvForgotPassword)
        val tvRegister = findViewById<TextView>(R.id.tvRegister)

        btnLogin.setOnClickListener {
            val email = etContact.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Email dan Password wajib diisi!", Toast.LENGTH_SHORT).show()
            } else {
                btnLogin.isEnabled = false // Mencegah double klik

                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        btnLogin.isEnabled = true
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Selamat Datang di Mindful App!", Toast.LENGTH_SHORT).show()
                            startIntent()
                        } else {
                            // Menggunakan fungsi friendly error message
                            val errorMessage = getFriendlyErrorMessage(task.exception)
                            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
                        }
                    }
            }
        }

        tvForgotPassword.setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }

        tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    // Fungsi helper
    private fun getFriendlyErrorMessage(exception: Exception?): String {
        val message = exception?.message ?: ""
        return when {
            message.contains("network", ignoreCase = true) ->
                "Koneksi internet bermasalah. Coba lagi nanti."
            message.contains("invalid-credential", ignoreCase = true) || message.contains("wrong-password", ignoreCase = true) ->
                "Email atau password salah."
            message.contains("user-not-found", ignoreCase = true) ->
                "Akun tidak ditemukan. Pastikan email Anda benar."
            else ->
                "Terjadi kesalahan: ${exception?.localizedMessage}"
        }
    }

    private fun startIntent() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}