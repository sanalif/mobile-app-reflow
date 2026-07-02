package com.ikhsan.reflow

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream

class CompletionFragment : BottomSheetDialogFragment() {

    // Menggunakan tema kustom untuk sudut melengkung
    override fun getTheme(): Int = R.style.CustomBottomSheetDialogTheme

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate layout fragment_completion
        return inflater.inflate(R.layout.fragment_completion, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inisialisasi View
        val btnShare = view.findViewById<MaterialButton>(R.id.btnShare)
        val tvBack = view.findViewById<TextView>(R.id.tvBackToHome)

        // 1. Aksi Bagikan Pencapaian
        btnShare.setOnClickListener {
            // 1. Ambil referensi ke layout kartu pencapaian (misalnya root view dari dialog)
            val viewToShare = view ?: return@setOnClickListener

            // 2. Buat Bitmap dari View
            val bitmap = createBitmapFromView(viewToShare)

            // 3. Simpan ke file sementara agar bisa dibagikan
            val file = File(requireContext().cacheDir, "pencapaian.png")
            val fOut = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut)
            fOut.flush()
            fOut.close()

            // 4. Dapatkan URI menggunakan FileProvider (wajib untuk Android 7+)
            val uri = FileProvider.getUriForFile(requireContext(), "${requireContext().packageName}.provider", file)

            // 5. Buat Intent Share
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "image/png"
                putExtra(Intent.EXTRA_STREAM, uri)
                putExtra(Intent.EXTRA_TEXT, "Lihat pencapaian fokus saya di Reflow!")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            startActivity(Intent.createChooser(shareIntent, "Bagikan via"))
        }

        // 2. Aksi Kembali ke Beranda
        tvBack.setOnClickListener {
            val intent = Intent(activity, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            dismiss() // Menutup fragment
        }
    }

    private fun createBitmapFromView(view: View): Bitmap {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }
}