package com.flycode.metronom.model

import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.SoundPool
import android.os.IBinder
import android.util.Log
import com.flycode.metronom.R
import com.flycode.metronom.fragments.MetronomFragment
import com.flycode.metronom.model.Metronom.const.MILLISECONDS_IN_SECOND
import com.flycode.metronom.model.Metronom.const.SECONDS_IN_MINUTE
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.concurrent.timerTask

class Metronom {

    private lateinit var soundPool: SoundPool
    lateinit var metronomTimer: Timer

    object const{
        const val MILLISECONDS_IN_SECOND: Int = 1000
        const val SECONDS_IN_MINUTE: Int = 60
    }

    // Метод для создания таймера и soundPool
    fun createSoundTimer(context: Context){
        // Создадим таймер
        metronomTimer = Timer("timer", true)
        // Создадим SoundPool
        soundPool = SoundPool.Builder()
            .setMaxStreams(1)
            .setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build()
            )
            .build()
        // Положим в SoundPool наш звук
        soundPool.load(context, R.raw.metronom, 1)
    }

    // Метод для запуска метронома
    fun playMetronom(bpm: Int){
        // Запустим таймер
        metronomTimer.schedule(
            timerTask {
                soundPool.play(1,1f, 1f, 1, 0, 1f)
            }, 0L, calculateSleepDuration(bpm))
    }

    // Метод для паузы метронома
    fun pauseMetronom(){
        metronomTimer.cancel()
        metronomTimer = Timer("timer", false)
    }


    // Метод для подсчёта перерыва в секундах между ударами - считается из BPM
    fun calculateSleepDuration(bpm: Int): Long {
        return (MILLISECONDS_IN_SECOND * (SECONDS_IN_MINUTE / bpm.toFloat())).toLong()
    }


}