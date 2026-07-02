package com.ikhsan.reflow

import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        auth = FirebaseAuth.getInstance()

        val etContactForgot = findViewById<EditText>(R.id.etContactForgot)
        val btnSubmitReset = findViewById<MaterialButton>(R.id.btnSubmitReset)
        val tvBackToLogin = findViewById<TextView>(R.id.tvBackToLogin)

        btnSubmitReset.setOnClickListener {
            val email = etContactForgot.text.toString().trim()

            if (email.isEmpty()) {
                Toast.makeText(this, "Masukkan email Anda!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Fungsi bawaan Firebase untuk kirim email reset password
            auth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Email reset password telah dikirim ke $email", Toast.LENGTH_LONG).show()
                        finish() // Kembali ke Login setelah sukses
                    } else {
                        Toast.makeText(this, "Gagal: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                    }
                }
        }

        tvBackToLogin.setOnClickListener { finish() }
    }
}