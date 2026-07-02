package com.ikhsan.reflow

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import de.hdodenhof.circleimageview.CircleImageView

class ProfileUserActivity : AppCompatActivity() {

    private lateinit var ivAvatar: CircleImageView
    private lateinit var tvName: TextView

    private val CAMERA_PERMISSION_CODE = 100



    // Launcher untuk menangani hasil gambar
    private val imagePickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val data = result.data
            if (data?.data != null) {
                ivAvatar.setImageURI(data.data) // Galeri
            } else {
                val imageBitmap = data?.extras?.get("data") as? android.graphics.Bitmap
                ivAvatar.setImageBitmap(imageBitmap) // Kamera
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        NavbarHelper.setupNavbar(this, "profile")

        setupBottomNavigation()

        // Inisialisasi View
        ivAvatar = findViewById(R.id.ivAvatar)
        tvName = findViewById(R.id.tvName)
        val tvEmail = findViewById<TextView>(R.id.tvEmail)
        val avatarContainer = findViewById<FrameLayout>(R.id.avatarContainer)
        val layoutNameEdit = findViewById<LinearLayout>(R.id.layoutNameEdit)
        val menuEditPassword = findViewById<RelativeLayout>(R.id.menuEditPassword)
        val menuPusatBantuan = findViewById<RelativeLayout>(R.id.menuPusatBantuan)
        val btnLogout = findViewById<MaterialButton>(R.id.btnLogout)

        val user = FirebaseAuth.getInstance().currentUser
        tvName.text = user?.displayName ?: "Nama"
        tvEmail.text = user?.email ?: "email@kampus.ac.id"

        // 1. Aksi Foto (Cek Izin Kamera)
        avatarContainer.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_CODE)
            } else {
                showImagePickerOptions()
            }
        }

        // 2. Edit Profil
        layoutNameEdit.setOnClickListener { showEditProfileDialog() }

        // 3. Edit Password
        menuEditPassword.setOnClickListener {
            startActivity(Intent(this, ResetPasswordActivity::class.java))
        }

        // 4. Pusat Bantuan (WhatsApp)
        menuPusatBantuan.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://wa.me/6281919007044"))
            startActivity(intent)
        }

        // 5. Logout
        btnLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }

    private fun showImagePickerOptions() {
        val options = arrayOf("Ambil Foto", "Pilih dari Galeri", "Hapus Foto Profil")

        AlertDialog.Builder(this)
            .setTitle("Ubah Foto Profil")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> { // Kamera
                        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_CODE)
                        } else {
                            imagePickerLauncher.launch(Intent(MediaStore.ACTION_IMAGE_CAPTURE))
                        }
                    }
                    1 -> { // Galeri
                        imagePickerLauncher.launch(Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI))
                    }
                    2 -> { // Hapus Foto
                        // Kembalikan ke gambar default
                        ivAvatar.setImageResource(R.drawable.ic_profile) // Sesuaikan dengan drawable default kamu
                        Toast.makeText(this, "Foto profil dihapus", Toast.LENGTH_SHORT).show()
                    }
                }
            }.show()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_CODE && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            showImagePickerOptions()
        } else {
            Toast.makeText(this, "Izin kamera diperlukan", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showEditProfileDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_edit_profile, null)
        val etName = dialogView.findViewById<EditText>(R.id.etDialogName)
        val etEmail = dialogView.findViewById<EditText>(R.id.etDialogEmail)

        etName.setText(tvName.text)
        etEmail.setText(FirebaseAuth.getInstance().currentUser?.email)

        AlertDialog.Builder(this)
            .setTitle("Edit Profil")
            .setView(dialogView)
            .setPositiveButton("Simpan") { _, _ ->
                val newName = etName.text.toString()
                FirebaseAuth.getInstance().currentUser?.updateProfile(
                    UserProfileChangeRequest.Builder().setDisplayName(newName).build()
                )?.addOnCompleteListener { if (it.isSuccessful) tvName.text = newName }
                FirebaseAuth.getInstance().currentUser?.updateEmail(etEmail.text.toString())
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    private fun setupBottomNavigation() {
        val ivHome = findViewById<ImageView>(R.id.icHome)
        val ivList = findViewById<ImageView>(R.id.icList)
        val ivProfile = findViewById<ImageView>(R.id.icProfile)

        ivHome.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0)
            finish()
        }

        ivList.setOnClickListener {
            val intent = Intent(this, ListTaskActivity::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0)
            finish()
        }

        ivProfile.setOnClickListener {
            Toast.makeText(this, "Anda sudah di Profile", Toast.LENGTH_SHORT).show()
        }
    }
}