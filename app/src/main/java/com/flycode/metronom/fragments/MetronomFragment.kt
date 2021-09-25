package com.flycode.metronom.fragments

import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.flycode.metronom.R
import moxy.MvpAppCompatFragment
import java.util.*

// Классы Timer и TimerTask работают в связке. Класс Timer используется для планирования выполнения задачи. Запланированная к выполнению задача
// должна быть экземпляром класса TimerTask. Вы сначала создаёте объект класса TimerTask, а затем планируете его запуск с помощью класса Timer.

class MetronomFragment: MvpAppCompatFragment(R.layout.fragment_metronom) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}

