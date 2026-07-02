package com.ikhsan.reflow

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

class ResetPasswordActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

        val etNewPassword = findViewById<TextInputEditText>(R.id.etNewPassword)
        val etConfirmPassword = findViewById<TextInputEditText>(R.id.etConfirmPassword)
        val btnSavePassword = findViewById<MaterialButton>(R.id.btnSavePassword)
        val tvCancelReset = findViewById<TextView>(R.id.tvCancelReset)

        val viewStrength1 = findViewById<View>(R.id.viewStrength1)
        val viewStrength2 = findViewById<View>(R.id.viewStrength2)
        val viewStrength3 = findViewById<View>(R.id.viewStrength3)
        val tvStrengthText = findViewById<TextView>(R.id.tvStrengthText)

        etNewPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                checkPasswordStrength(s.toString(), viewStrength1, viewStrength2, viewStrength3, tvStrengthText)
            }
        })

        btnSavePassword.setOnClickListener {
            val newPass = etNewPassword.text.toString().trim()
            val confPass = etConfirmPassword.text.toString().trim()

            if (newPass.length < 6) {
                Toast.makeText(this, "Password minimal 6 karakter!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (newPass != confPass) {
                Toast.makeText(this, "Konfirmasi password tidak cocok!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // MENGGANTI PASSWORD DI FIREBASE
            val user = FirebaseAuth.getInstance().currentUser
            if (user != null) {
                btnSavePassword.isEnabled = false
                btnSavePassword.text = "Menyimpan..."

                user.updatePassword(newPass)
                    .addOnCompleteListener { task ->
                        btnSavePassword.isEnabled = true
                        btnSavePassword.text = "Simpan Password"

                        if (task.isSuccessful) {
                            Toast.makeText(this, "Password berhasil diubah!", Toast.LENGTH_LONG).show()
                            finish()
                        } else {
                            Toast.makeText(this, "Gagal: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                        }
                    }
            }
        }

        tvCancelReset.setOnClickListener { finish() }
    }

    // Tambahkan fungsi ini ke dalam class ResetPasswordActivity
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