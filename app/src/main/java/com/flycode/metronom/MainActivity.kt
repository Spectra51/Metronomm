package com.flycode.metronom

import android.media.AudioManager
import android.media.MediaPlayer
import android.media.ToneGenerator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.flycode.metronom.MainActivity.const.MILLISECONDS_IN_SECOND
import com.flycode.metronom.MainActivity.const.SECONDS_IN_MINUTE
import java.util.*
import kotlin.concurrent.timerTask

typealias BPM = Long

class MainActivity : AppCompatActivity() {

    object   const  {


         const val MILLISECONDS_IN_SECOND: Int = 1000
         const val SECONDS_IN_MINUTE: Int = 60
    }

    lateinit var metronomTimer: Timer


    lateinit var vw :Button
    lateinit var vwStop :Button
    lateinit var vwx2 :Button
    lateinit var vwx4 :Button
    lateinit var media : MediaPlayer





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        vw = findViewById(R.id.buttonStart)
        media = MediaPlayer.create(this,R.raw.metronom)

        metronomTimer = Timer("timer", true)

        vwx2 = findViewById(R.id.button4)
        vwx4 = findViewById(R.id.button8)

        vwStop = findViewById(R.id.buttonStop)

        vw.setOnClickListener {
            val correntBpm = getCurrentBpm()
           start(correntBpm!!)

        }
        vwStop.setOnClickListener {
            stop()

        }
    }
    private fun createNewTimer() {
        metronomTimer
    }

     private fun calculateSleepDuration(bpm: BPM): Long {
        return (MILLISECONDS_IN_SECOND * (SECONDS_IN_MINUTE / bpm.toDouble())).toLong()
    }


    private fun start(bpm: BPM): Boolean {



        this.metronomTimer.schedule(
            timerTask {
                media.start()

            }, 0L, calculateSleepDuration(bpm)
        )
        return true
    }

    private fun stop() {
        this.metronomTimer.cancel()
        createNewTimer()
    }




    private fun getCurrentBpm(): BPM? {
        val currentBpmEditText = findViewById<EditText>(R.id.editTextText)

        return if (currentBpmEditText.text.isNotEmpty()) {
            currentBpmEditText.text.toString().toLong()
        } else {
            null
        }
    }
}

