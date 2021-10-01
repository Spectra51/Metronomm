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

    @SuppressLint("ClickableViewAccessibility")
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
            seekBarBpm.setProgress(getCurrentBpm()- MIN_BPM)
            if ((!imageViewPlay.isVisible && !isTapping) || (!imageViewPlay.isVisible && getCurrentBpm()==240) || (!imageViewPlay.isVisible && getCurrentBpm()==20)){
                presenter.pauseMetronom()
                val bpm = getCurrentBpm()
                presenter.playMetronom(bpm)
            }


        }

        // Ставим прослушиватель на кнопку Play для запуска метронома
        imageViewPlay.setOnClickListener {
            if (editTextBpm.text.isNotEmpty()) {
                val bpm = editTextBpm.text.toString().toInt()
                if(bpm < MIN_BPM){
                    Toast.makeText(requireContext(), "Значение не может быть меньше 20", Toast.LENGTH_SHORT).show()
                    editTextBpm.setText(MIN_BPM.toString())
                } else if( bpm > MAX_BPM){
                    Toast.makeText(requireContext(), "Значение не может быть больше 240", Toast.LENGTH_SHORT).show()
                    editTextBpm.setText(MAX_BPM.toString())
                } else{
                    presenter.playMetronom(bpm)
                }
            } else {
                Toast.makeText(requireContext(), "Значение не может быть пустым", Toast.LENGTH_SHORT).show()
            }

        }
        // Ставим прослушиватель на кнопку Pause для паузы метронома
        imageViewPause.setOnClickListener {
            presenter.pauseMetronom()
        }

        // Слушатели для длительного нажатия на кнопку Увеличить bpm
        buttonUp.setOnClickListener { updateBpm(true) }
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
        buttonDown.setOnClickListener{updateBpm(false)}
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
                updateBpm(true)
                handler.postDelayed(bpmUpdater(), 50L)
            } else if (autoDown) {
                updateBpm(false)
                handler.postDelayed(bpmUpdater(), 50L)
            }
        }
    }


    override fun isPlaying() {
        imageViewPlay.isVisible = false
        imageViewPause.isVisible = true
    }

    override fun isPausing() {
        imageViewPlay.isVisible = true
        imageViewPause.isVisible = false
    }

    // Метод для обновления значений в поле editTextBpm по нажатию на кнопки Увеличить bpm и Уменьшить bpm
    private fun updateBpm (increase: Boolean){

        val currentBpm = getCurrentBpm()
        val newBpm = if (increase) currentBpm + 1 else currentBpm - 1
        val allowUpdate = if (increase) newBpm <= MAX_BPM else newBpm >= MIN_BPM
        if (allowUpdate) {
            editTextBpm.setText(newBpm.toString())
        }

    }

    // Метод для получения bpm из поля editTextBpm
    private fun getCurrentBpm(): Int {
        return if (editTextBpm.text.isNotEmpty()) {
            editTextBpm.text.toString().toInt()
        } else {
            MIN_BPM
        }
    }



}


