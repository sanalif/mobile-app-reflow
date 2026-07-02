package com.ikhsan.reflow

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore // Pastikan import ini ada

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()

        val etName = findViewById<TextInputEditText>(R.id.etName)
        val etEmail = findViewById<TextInputEditText>(R.id.etEmail)
        val etPassword = findViewById<TextInputEditText>(R.id.etPassword)
        val etConfirmPassword = findViewById<TextInputEditText>(R.id.etConfirmPassword)
        val btnRegister = findViewById<MaterialButton>(R.id.btnRegister)
        val tvLoginLink = findViewById<TextView>(R.id.tvLoginLink)

        // Indikator kekuatan password
        val viewStrength1 = findViewById<View>(R.id.viewStrength1)
        val viewStrength2 = findViewById<View>(R.id.viewStrength2)
        val viewStrength3 = findViewById<View>(R.id.viewStrength3)
        val tvStrengthText = findViewById<TextView>(R.id.tvStrengthText)

        etPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                checkPasswordStrength(s.toString(), viewStrength1, viewStrength2, viewStrength3, tvStrengthText)
            }
        })

        btnRegister.setOnClickListener {
            val name = etName.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()
            val confirmPassword = etConfirmPassword.text.toString().trim()

            // Validasi Input
            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Semua kolom wajib diisi!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                etEmail.error = "Format email tidak valid"
                return@setOnClickListener
            }

            if (password.length < 6) {
                etPassword.error = "Password minimal 6 karakter"
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                etConfirmPassword.error = "Password tidak cocok!"
                return@setOnClickListener
            }

            // Loading State
            btnRegister.isEnabled = false
            btnRegister.text = "Memproses..."

            // Registrasi Firebase
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        saveUserToFirestore(name, email)
                    } else {
                        btnRegister.isEnabled = true
                        btnRegister.text = "Buat Akun"
                        Toast.makeText(this, "Gagal: ${task.exception?.localizedMessage}", Toast.LENGTH_LONG).show()
                    }
                }
        }

        tvLoginLink.setOnClickListener { finish() }
    }

    private fun saveUserToFirestore(name: String, email: String) {
        val userId = auth.currentUser?.uid ?: return
        val db = FirebaseFirestore.getInstance()
        val userMap = hashMapOf(
            "name" to name,
            "email" to email,
            "createdAt" to System.currentTimeMillis()
        )

        db.collection("users").document(userId).set(userMap)
            .addOnSuccessListener {
                Toast.makeText(this, "Registrasi berhasil, $name!", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, LoginActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                })
                finish()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Gagal simpan profil: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }

    private fun checkPasswordStrength(password: String, v1: View, v2: View, v3: View, tvText: TextView) {
        if (password.isEmpty()) {
            resetStrengthIndicators(v1, v2, v3, tvText)
            return
        }
        var score = 0
        if (password.length >= 8) score++
        if (password.any { it.isDigit() } && password.any { it.isLetter() }) score++
        if (password.any { !it.isLetterOrDigit() }) score++

        when {
            score <= 1 -> {
                v1.setBackgroundColor(Color.parseColor("#FF7675"))
                v2.setBackgroundColor(Color.parseColor("#E0E0E0"))
                v3.setBackgroundColor(Color.parseColor("#E0E0E0"))
                tvText.text = "Kekuatan: Lemah"
                tvText.setTextColor(Color.parseColor("#FF7675"))
            }
            score == 2 -> {
                v1.setBackgroundColor(Color.parseColor("#FDCB6E"))
                v2.setBackgroundColor(Color.parseColor("#FDCB6E"))
                v3.setBackgroundColor(Color.parseColor("#E0E0E0"))
                tvText.text = "Kekuatan: Sedang"
                tvText.setTextColor(Color.parseColor("#FDCB6E"))
            }
            else -> {
                v1.setBackgroundColor(Color.parseColor("#A3B18A"))
                v2.setBackgroundColor(Color.parseColor("#A3B18A"))
                v3.setBackgroundColor(Color.parseColor("#A3B18A"))
                tvText.text = "Kekuatan: Kuat"
                tvText.setTextColor(Color.parseColor("#A3B18A"))
            }
        }
    }

    private fun resetStrengthIndicators(v1: View, v2: View, v3: View, tvText: TextView) {
        v1.setBackgroundColor(Color.parseColor("#E0E0E0"))
        v2.setBackgroundColor(Color.parseColor("#E0E0E0"))
        v3.setBackgroundColor(Color.parseColor("#E0E0E0"))
        tvText.text = "Kekuatan: -"
        tvText.setTextColor(Color.parseColor("#718093"))
    }
}