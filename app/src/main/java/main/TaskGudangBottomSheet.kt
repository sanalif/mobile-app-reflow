package com.universitaspertamina.reflow.ui.main

import android.app.DatePickerDialog
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import com.universitaspertamina.reflow.R
import java.util.Calendar
import java.util.Locale

class TaskGudangBottomSheet : BottomSheetDialogFragment() {

    private val tasks = mutableListOf<String>()
    private lateinit var taskAdapter: TaskGudangAdapter

    // Variabel pelacak status pilihan user
    private var isPenting = true
    private var tanggalTerpilih = "Hari Ini"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_task_gudang_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Inisialisasi Adapter & RecyclerView
        taskAdapter = TaskGudangAdapter(tasks)
        val rvGudangTugas = view.findViewById<RecyclerView>(R.id.rvGudangTugas)
        rvGudangTugas.layoutManager = LinearLayoutManager(requireContext())
        rvGudangTugas.adapter = taskAdapter

        // 2. Hubungkan ID Elemen UI dari XML
        val btnTambah = view.findViewById<Button>(R.id.btnTambahTugas)
        val etInputTugas = view.findViewById<EditText>(R.id.etInputTugas)

        val btnTenggatWaktu = view.findViewById<MaterialButton>(R.id.btnTenggatWaktu)
        val btnKategoriPenting = view.findViewById<MaterialButton>(R.id.btnKategoriPenting)
        val btnKategoriSantai = view.findViewById<MaterialButton>(R.id.btnKategoriSantai)

        // Menyiapkan Amunisi Warna agar transisi tombol mulus & tidak bug
        val warnaAktifBg = ColorStateList.valueOf(Color.parseColor("#E3F2FD"))   // Biru muda soft
        val warnaAktifTeks = Color.parseColor("#1E88E5")                         // Biru kontras tebal
        val warnaMatiBg = ColorStateList.valueOf(Color.TRANSPARENT)              // Transparan
        val warnaMatiTeks = Color.parseColor("#424242")                         // Abu-abu gelap netral

        // === 3. ATUR TAMPILAN AWAL (Default: Tombol Penting Menyala) ===
        btnKategoriPenting.backgroundTintList = warnaAktifBg
        btnKategoriPenting.setTextColor(warnaAktifTeks)
        btnKategoriSantai.backgroundTintList = warnaMatiBg
        btnKategoriSantai.setTextColor(warnaMatiTeks)


        // === 4. LOGIKA INTERAKTIF KLIK KATEGORI ===
        btnKategoriPenting.setOnClickListener {
            isPenting = true
            // Nyalakan tombol Penting
            btnKategoriPenting.backgroundTintList = warnaAktifBg
            btnKategoriPenting.setTextColor(warnaAktifTeks)
            // Matikan tombol Santai
            btnKategoriSantai.backgroundTintList = warnaMatiBg
            btnKategoriSantai.setTextColor(warnaMatiTeks)
        }

        btnKategoriSantai.setOnClickListener {
            isPenting = false
            // Nyalakan tombol Santai
            btnKategoriSantai.backgroundTintList = warnaAktifBg
            btnKategoriSantai.setTextColor(warnaAktifTeks)
            // Matikan tombol Penting
            btnKategoriPenting.backgroundTintList = warnaMatiBg
            btnKategoriPenting.setTextColor(warnaMatiTeks)
        }


        // === 5. LOGIKA KLIK TENGGAT WAKTU (Membuka Kalender Asli HP) ===
        val kalenderBawaan = Calendar.getInstance()

        btnTenggatWaktu.setOnClickListener {
            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { _, tahun, bulan, tanggal ->
                    // Mengubah format tanggal menjadi angka rapi (contoh: 24/07/2026)
                    tanggalTerpilih = String.format(Locale.getDefault(), "%02d/%02d/%d", tanggal, bulan + 1, tahun)

                    // Ganti teks tombol "Hari Ini" menjadi tanggal pilihan user secara real-time
                    btnTenggatWaktu.text = tanggalTerpilih
                },
                kalenderBawaan.get(Calendar.YEAR),
                kalenderBawaan.get(Calendar.MONTH),
                kalenderBawaan.get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.show()
        }


        // === 6. LOGIKA TOMBOL TAMBAH TUGAS ===
        btnTambah.setOnClickListener {
            val taskText = etInputTugas.text.toString()
            if (taskText.isNotEmpty()) {
                // Di sini kamu sudah berhasil menggenggam data lengkap:
                // 1. Nama Tugas = taskText
                // 2. Skala Prioritas = isPenting (true jika penting, false jika santai)
                // 3. Batas Waktu = tanggalTerpilih

                tasks.add(taskText)
                taskAdapter.notifyDataSetChanged()
                rvGudangTugas.scrollToPosition(tasks.size - 1)

                // Bersihkan form kembali ke pengaturan awal setelah menambah data
                etInputTugas.text.clear()
                tanggalTerpilih = "Hari Ini"
                btnTenggatWaktu.text = "Hari Ini"
            }
        }
    }
}