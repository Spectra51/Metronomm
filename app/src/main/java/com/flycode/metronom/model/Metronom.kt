package com.flycode.metronom.model

import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.media.AudioAttributes
import android.media.SoundPool
import android.os.IBinder
import android.util.Log
import com.flycode.metronom.R
import com.flycode.metronom.fragments.MetronomFragment
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.concurrent.timerTask

class Metronom {

    lateinit var soundPool: SoundPool
    lateinit var metronomTimer: Timer

    // Метод для создания soundPool
    fun createSoundPool(context: Context){
        // Создадим SoundPool
        soundPool = SoundPool.Builder()
            .setMaxStreams(1)
            .setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build()
            )
            .build()
        // Положим в SoundPool наши звуки (при одновременном плее из 2-х звуков воспроизводится тот, у которого выше приоритет)
        soundPool.load(context, R.raw.metronom, 1)
        soundPool.load(context,R.raw.metronom_strong,2)

    }

    // Метод для создания таймера
    fun createTimer() {
        metronomTimer = Timer("timer", true)
    }

    // Метод для паузы таймера
    fun pauseTimer(){
        metronomTimer.cancel() // отменим действующий таймер
        metronomTimer = Timer("timer", false) // создадим новый таймер
    }


}