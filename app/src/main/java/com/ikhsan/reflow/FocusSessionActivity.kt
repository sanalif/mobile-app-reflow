package com.ikhsan.reflow

import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import java.util.Locale
import kotlin.math.sqrt

class FocusSessionActivity : AppCompatActivity(), SensorEventListener {

    private var seconds = 0
    private var isRunning = true
    private var isMuted = false
    private val handler = Handler(Looper.getMainLooper())

    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null
    private var mediaPlayer: MediaPlayer? = null
    private var currentSoundRes = R.raw.hujan

    private var isCoolingDown = false

    private val recoveryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        SessionManager.isRecoveryOpen = false
        isCoolingDown = true
        handler.postDelayed({ isCoolingDown = false }, 2000)

        if (!isMuted) mediaPlayer?.start()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_focus_session)

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        val taskName = intent.getStringExtra("SMALL_STEP") ?: "Fokus"
        findViewById<TextView>(R.id.tvTaskName).text = taskName

        runTimer()
        playSound(currentSoundRes)

        // Tombol Mute
        findViewById<ImageView>(R.id.btnMute).setOnClickListener {
            isMuted = !isMuted
            findViewById<ImageView>(R.id.btnMute).setImageResource(if (isMuted) R.drawable.ic_volume_off else R.drawable.ic_volume)
            if (isMuted) mediaPlayer?.pause() else mediaPlayer?.start()
        }

        // Tombol Ganti Suara
        findViewById<ImageView>(R.id.btnSoundMenu).setOnClickListener {
            val bottomSheet = SoundSelectionBottomSheet { selectedName ->
                changeSound(selectedName)
            }
            bottomSheet.show(supportFragmentManager, "SoundSelector")
        }

        findViewById<MaterialButton>(R.id.btnSelesai).setOnClickListener {
            isRunning = false

            // Matikan suara
            mediaPlayer?.let {
                if (it.isPlaying) {
                    it.pause()
                    // it.release() // Opsional: jika ingin membebaskan memori
                }
            }

            // 1. Panggil Fragment Completion
            val bottomSheet = CompletionFragment()

            // 2. Munculkan Bottom Sheet
            bottomSheet.show(supportFragmentManager, "CompletionFragment")
            mediaPlayer?.pause()


        }
    }

    private fun changeSound(soundName: String) {
        val newSoundRes = when (soundName) {
            "Hujan Hutan" -> R.raw.hujan
            "Nature Birds" -> R.raw.kicau
            else -> R.raw.hujan
        }
        playSound(newSoundRes)
    }

    private fun playSound(soundRes: Int) {
        if (mediaPlayer != null && currentSoundRes == soundRes) return

        currentSoundRes = soundRes
        mediaPlayer?.apply {
            if (isPlaying) stop()
            release()
        }

        mediaPlayer = MediaPlayer.create(this, soundRes)
        mediaPlayer?.isLooping = true

        if (!isMuted) mediaPlayer?.start()
    }

    private fun triggerRecovery() {
        if (SessionManager.isRecoveryOpen || isCoolingDown) return

        SessionManager.isRecoveryOpen = true
        mediaPlayer?.pause()

        val intent = Intent(this, MindfulRecoveryActivity::class.java)
        recoveryLauncher.launch(intent)
    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onStop() {
        super.onStop()
        if (!SessionManager.isRecoveryOpen && isRunning) {
            triggerRecovery()
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER && !SessionManager.isRecoveryOpen && !isCoolingDown) {
            val x = event.values[0]; val y = event.values[1]; val z = event.values[2]
            val acc = sqrt((x * x + y * y + z * z).toDouble()) - SensorManager.GRAVITY_EARTH

            if (acc > 12.0) {
                triggerRecovery()
            }
        }
    }

    private fun runTimer() {
        val tvTimer = findViewById<TextView>(R.id.tvTimer)
        val runnable = object : Runnable {
            override fun run() {
                if (isRunning) {
                    val m = (seconds % 3600) / 60; val s = seconds % 60
                    tvTimer.text = String.format(Locale.getDefault(), "%02d:%02d", m, s)
                    seconds++
                    handler.postDelayed(this, 1000)
                }
            }
        }
        handler.post(runnable)
    }

    override fun onAccuracyChanged(s: Sensor?, a: Int) {}

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}

//zakha