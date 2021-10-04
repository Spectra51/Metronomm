package com.flycode.metronom.fragments

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.*
import androidx.core.view.isVisible
import androidx.core.view.postDelayed
import androidx.core.widget.addTextChangedListener
import com.flycode.metronom.R
import com.flycode.metronom.fragments.MetronomFragment.const.MAX_BPM
import com.flycode.metronom.fragments.MetronomFragment.const.MIN_BPM
import com.flycode.metronom.model.Metronom
import com.flycode.metronom.presenters.MetronomPresenter
import com.flycode.metronom.views.MetronomView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.concurrent.schedule
import kotlin.concurrent.timerTask

class MetronomFragment: MvpAppCompatFragment(R.layout.fragment_metronom), MetronomView {

    private val presenter by moxyPresenter { MetronomPresenter() }

    object const{
        const val MIN_BPM: Int = 20
        const val MAX_BPM: Int = 240
    }

    private var autoUp: Boolean = false
    private var autoDown: Boolean = false
    private var isTapping: Boolean = false

    lateinit var imageViewPlay: ImageView
    lateinit var imageViewPause: ImageView
    lateinit var editTextBpm: EditText
    lateinit var buttonUp: ImageButton
    lateinit var buttonDown: ImageButton
    lateinit var textViewMetronome: TextView
    lateinit var seekBarBpm: SeekBar
    lateinit var handler: Handler


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        imageViewPlay = view.findViewById(R.id.imageViewPlay)
        imageViewPause = view.findViewById(R.id.imageViewPause)
        editTextBpm = view.findViewById(R.id.editTextBpm)
        buttonUp = view.findViewById(R.id.buttonUp)
        buttonDown = view.findViewById(R.id.buttonDown)
        textViewMetronome = view.findViewById(R.id.textViewMetronome)
        seekBarBpm = view.findViewById(R.id.seekBarBpm)

        // Создаём таймер и soundPool
        presenter.createSoundTimer(requireContext())
        // Создаём Handler
        handler = Handler(Looper.getMainLooper())

        // Ставим прослушиватель на editTextBpm, чтобы bpm изменялся при изменении поля editTextBpm
        editTextBpm.addTextChangedListener {
            seekBarBpm.setProgress(presenter.getCurrentBpm(editTextBpm)- MIN_BPM)
            presenter.metronomDependEditText(imageViewPlay.isVisible, editTextBpm, isTapping)
        }

        // Ставим прослушиватель на кнопку Play для запуска метронома
        imageViewPlay.setOnClickListener {
            presenter.tapPlayButton(editTextBpm)
        }
        // Ставим прослушиватель на кнопку Pause для паузы метронома
        imageViewPause.setOnClickListener {
            presenter.pauseMetronom()
        }

        // Слушатели для длительного нажатия на кнопку Увеличить bpm
        buttonUp.setOnClickListener { presenter.updateBpm(true, editTextBpm) }
        buttonUp.setOnLongClickListener {
            isTapping = true
            autoUp = true
            handler.post{
                handler.postDelayed(bpmUpdater(), 50L)
            }
            false
        }
        buttonUp.setOnTouchListener { v, motionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_UP && autoUp) {
                autoUp= false
                isTapping = false
            }
            false
        }

        // Слушатели для длительного нажатия на кнопку Уменьшить bpm
        buttonDown.setOnClickListener{presenter.updateBpm(false, editTextBpm)}
        buttonDown.setOnLongClickListener {
            isTapping = true
            autoDown = true
            handler.post{
                handler.postDelayed(bpmUpdater(), 50L)
            }
            false
        }
        buttonDown.setOnTouchListener { v, motionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_UP && autoDown) {
                autoDown = false
                isTapping = false
            }
            false
        }


        // Слушатель для seekbar
        seekBarBpm.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser){
                    editTextBpm.setText((progress+ MIN_BPM).toString())
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                isTapping = true
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                isTapping = false
                if (seekBar != null) {
                    editTextBpm.setText((seekBar.progress+ MIN_BPM).toString())
                }
            }

        })

    }

    // Внутренний класс, который нужен для долгого нажатия по кнопкам
    inner class bpmUpdater : Runnable {
        override fun run() {
            if (autoUp) {
                presenter.updateBpm(true, editTextBpm)
                handler.postDelayed(bpmUpdater(), 50L)
            } else if (autoDown) {
                presenter.updateBpm(false, editTextBpm)
                handler.postDelayed(bpmUpdater(), 50L)
            }
        }
    }


    // МЕТОДЫ VIEW
    override fun isPlaying() {
        imageViewPlay.isVisible = false
        imageViewPause.isVisible = true
    }

    override fun isPausing() {
        imageViewPlay.isVisible = true
        imageViewPause.isVisible = false
    }

    override fun setEditTextBpm(bpm: Int) {
        editTextBpm.setText(bpm.toString())
    }

    override fun showToast(toast: String) {
        Toast.makeText(requireContext(), toast, Toast.LENGTH_SHORT).show()
    }
}


