package com.flycode.metronom.fragments

import android.graphics.Color
import android.media.AudioManager
import android.media.AudioManager.STREAM_MUSIC
import android.media.MediaPlayer
import android.media.ToneGenerator
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.flycode.metronom.MainActivity
import com.flycode.metronom.R
import moxy.MvpAppCompatFragment
import java.util.*

// Классы Timer и TimerTask работают в связке. Класс Timer используется для планирования выполнения задачи. Запланированная к выполнению задача
// должна быть экземпляром класса TimerTask. Вы сначала создаёте объект класса TimerTask, а затем планируете его запуск с помощью класса Timer.

class MetronomFragment: MvpAppCompatFragment(R.layout.fragment_metronom) {

    lateinit var btnUp: ImageView
    lateinit var text: TextView
    lateinit var mPlayer: MediaPlayer

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnUp = view.findViewById(R.id.imageBtnUp)
        text = view.findViewById(R.id.textView)

        mPlayer = MediaPlayer.create(requireContext(), R.raw.zvuk_metro)

        btnUp.setOnClickListener {
            val myTimer = Timer()
            val myTimerTask = MyTimerTask(view)
            myTimer.schedule(myTimerTask, 0,1000)
        }

    }

    inner class MyTimerTask(val myView: View): TimerTask(){
        override fun run() {
            Log.i("My", "timer")
            mPlayer.start()
            text.setTextColor(Color.WHITE)
            myView.postDelayed({
                text.setTextColor(Color.BLACK)
            }, 100)
        }

    }

}

