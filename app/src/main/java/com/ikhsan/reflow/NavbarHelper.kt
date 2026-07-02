package com.ikhsan.reflow

import android.graphics.Color
import android.graphics.PorterDuff
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

object NavbarHelper {
    fun setupNavbar(activity: AppCompatActivity, activePage: String) {
        val icHome = activity.findViewById<ImageView>(R.id.icHome)
        val icList = activity.findViewById<ImageView>(R.id.icList)
        val icProfile = activity.findViewById<ImageView>(R.id.icProfile)

        val activeColor = Color.parseColor("#A3B18A")
        val inactiveColor = Color.parseColor("#D6D3D1")

        // Reset semua ke tidak aktif
        icHome.setColorFilter(inactiveColor, PorterDuff.Mode.SRC_IN)
        icList.setColorFilter(inactiveColor, PorterDuff.Mode.SRC_IN)
        icProfile.setColorFilter(inactiveColor, PorterDuff.Mode.SRC_IN)

        // Set warna ikon yang aktif
        when (activePage) {
            "home" -> icHome.setColorFilter(activeColor, PorterDuff.Mode.SRC_IN)
            "list" -> icList.setColorFilter(activeColor, PorterDuff.Mode.SRC_IN)
            "profile" -> icProfile.setColorFilter(activeColor, PorterDuff.Mode.SRC_IN)
        }
    }
}