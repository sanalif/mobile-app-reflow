package com.ikhsan.reflow

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton
import com.google.firebase.Timestamp
import java.util.Date

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        NavbarHelper.setupNavbar(this, "home")

        val tvTaskTitle = findViewById<TextView>(R.id.tvTaskTitle)
        val btnStart = findViewById<MaterialButton>(R.id.btnMulaiMengerjakan)

        // Memuat data dengan logika deadline terdekat
        loadDataDinamis(tvTaskTitle)

        setupBottomNavigation()

        btnStart.setOnClickListener {
            val bottomSheet = TaskBreakdownFragment()
            bottomSheet.show(supportFragmentManager, "TaskBreakdown")
        }
    }

    private fun loadDataDinamis(textView: TextView) {
        // Ambil data dari Repository, bukan buat list baru
        val closestTask = TaskRepository.getClosestTask()

        if (closestTask != null) {
            textView.text = closestTask.title
        } else {
            textView.text = "Tidak ada tugas"
        }
    }

    private fun setupBottomNavigation() {
        val ivHome = findViewById<ImageView>(R.id.icHome)
        val ivList = findViewById<ImageView>(R.id.icList)
        val ivProfile = findViewById<ImageView>(R.id.icProfile)

        ivHome.setOnClickListener {
            Toast.makeText(this, "Anda sudah di Home", Toast.LENGTH_SHORT).show()
        }

        ivList.setOnClickListener {
            val intent = Intent(this, ListTaskActivity::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0)
            finish()
        }

        ivProfile.setOnClickListener {
            val intent = Intent(this, ProfileUserActivity::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0)
            finish()
        }
    }
}

//zakha