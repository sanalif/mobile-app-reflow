package com.ikhsan.reflow

import android.animation.ObjectAnimator
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.sqrt

class MindfulRecoveryActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null
    private var isFinished = false
    private var canTrigger = false // Flag untuk jeda aktivasi
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mindful_recovery)

        val phoneIcon = findViewById<ImageView>(R.id.ivPhoneShake)
        ObjectAnimator.ofFloat(phoneIcon, "rotation", -10f, 10f).apply {
            duration = 500
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
            start()
        }

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    }

    override fun onResume() {
        super.onResume()
        isFinished = false
        canTrigger = false

        // Jeda aktivasi 1 detik: Sensor tidak akan merespon goyangan selama 1 detik pertama
        // Ini mencegah "ter-trigger" secara tidak sengaja saat layar baru muncul
        handler.postDelayed({ canTrigger = true }, 1000)

        // Gunakan SENSOR_DELAY_NORMAL agar tidak terlalu liar/sensitif
        accelerometer?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onDestroy() {
        SessionManager.isRecoveryOpen = false
        super.onDestroy()
    }

    override fun onSensorChanged(event: SensorEvent?) {
        // Cek canTrigger agar tidak merespon sebelum 1 detik
        if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER && !isFinished && canTrigger) {
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]

            val acc = sqrt((x * x + y * y + z * z).toDouble()) - SensorManager.GRAVITY_EARTH

            // Threshold dinaikkan ke 14.0 agar harus lebih niat menggoyangnya
            if (acc > 14.0) {
                isFinished = true

                // Tambahkan delay sedikit agar transisi ke FocusSession tidak patah-patah
                handler.postDelayed({
                    finish()
                }, 200)
            }
        }
    }

    override fun onAccuracyChanged(s: Sensor?, a: Int) {}
}