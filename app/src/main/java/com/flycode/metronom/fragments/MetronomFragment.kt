package com.flycode.metronom.fragments

import android.content.res.ColorStateList
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import android.view.View
import android.widget.*
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import com.flycode.metronom.R
import com.flycode.metronom.fragments.MetronomFragment.const.MIN_BPM
import com.flycode.metronom.fragments.SheetDrawingFragment.Companion.drawingKey
import com.flycode.metronom.fragments.SheetSizeFragment.Companion.sizeKey
import com.flycode.metronom.presenters.MetronomPresenter
import com.flycode.metronom.views.MetronomView
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter

class MetronomFragment: MvpAppCompatFragment(R.layout.fragment_metronom), MetronomView {

    private val presenter by moxyPresenter { MetronomPresenter() } // переменная для презентера

    object const{
        const val MIN_BPM: Int = 20 // минимальное bpm
        const val MAX_BPM: Int = 240 // максимальное bpm
    }

    private var autoUp: Boolean = false // переменные для автоматического увеличения bpm при долгом нажатии на кнопки увеличить и уменьшить
    private var autoDown: Boolean = false
    private var isTapping: Boolean = false // переменная обозначает, нажимаем ли мы на сикбар или кнопки увеличить bpm и уменьшить bpm

    private var kBpm = 1
    private var selectNote1: Boolean = true // переменная true, когда нажимаем на выбранный рисунок с нотой
    private var selectNote2: Boolean = false
    private var selectNote3: Boolean = false

    lateinit var imageViewPlay: ImageView // кнопка play
    lateinit var imageViewPause: ImageView // кнопка pause
    lateinit var editTextBpm: EditText // поле для ввода bpm
    lateinit var buttonUp: ImageButton // кнопка увеличить bpm
    lateinit var buttonDown: ImageButton // кнопка уменьшить bpm
    lateinit var textViewMetronome: TextView // текст названия приложения
    lateinit var seekBarBpm: SeekBar // сикбар
    lateinit var imageButtonSize: ImageButton // кнопка размер
    lateinit var imageButtonDrawing: ImageButton // кнопка рисунок
    lateinit var textViewFractionSize: TextView // текст кнопки размер
    lateinit var imageViewDrawingNotes: ImageView // картинка с нотой в кнопке рисунок
    lateinit var imageButtonFraction1: ImageButton // кнопки для 4-х долей
    lateinit var imageButtonFraction2: ImageButton
    lateinit var imageButtonFraction3: ImageButton
    lateinit var imageButtonFraction4: ImageButton

    val handlerTimeout = Handler(Looper.getMainLooper()) // Handler для задержки после переключения рисунка
    lateinit var handlerLongTap: Handler // Handler для долгого нажатия долгого нажатия по кнопкам Увеличить bpm и Уменьшить bpm


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        imageViewPlay = view.findViewById(R.id.imageViewPlay)
        imageViewPause = view.findViewById(R.id.imageViewPause)
        editTextBpm = view.findViewById(R.id.editTextBpm)
        buttonUp = view.findViewById(R.id.buttonUp)
        buttonDown = view.findViewById(R.id.buttonDown)
        textViewMetronome = view.findViewById(R.id.textViewMetronome)
        seekBarBpm = view.findViewById(R.id.seekBarBpm)
        imageButtonSize = view.findViewById(R.id.imageButtonSize)
        imageButtonDrawing = view.findViewById(R.id.imageButtonDrawing)
        imageButtonFraction1 = view.findViewById(R.id.imageButtonFraction1)
        imageButtonFraction2 = view.findViewById(R.id.imageButtonFraction2)
        imageButtonFraction3 = view.findViewById(R.id.imageButtonFraction3)
        imageButtonFraction4 = view.findViewById(R.id.imageButtonFraction4)
        textViewFractionSize = view.findViewById(R.id.textViewFractionSize)
        imageViewDrawingNotes = view.findViewById(R.id.imageViewDrawingNotes)

        // currentBackStackEntry - получаем текущий фрагмент в стеке, чтобы изменять элементы в данном фрагменте
        // savedStateHandle - берем обработчик состояния фрагмента
        // Используем liveData, чтобы подписаться на изменения аргумента во фрагменте-BottomSheetDialog
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Int>(drawingKey)
            ?.observe(viewLifecycleOwner) {
                // действие при изменение аргумента
                imageViewDrawingNotes.setImageResource(it)
                val bpm = editTextBpm.text.toString().toInt()

                // В зависимости от выбранной ноты будут меняться selectNote1, selectNote2, selectNote3, kBpm
                if (it == R.drawable.note11){
                    selectNote1 = true
                    selectNote2 = false
                    selectNote3 = false
                    kBpm = 1
                } else if(it == R.drawable.note22){
                    selectNote1 = false
                    selectNote2 = true
                    selectNote3 = false
                    kBpm = 2
                } else {
                    selectNote1 = false
                    selectNote2 = false
                    selectNote3 = true
                    kBpm = 4
                }

                if (!imageViewPlay.isVisible){
                    presenter.pauseMetronom()
                    isPlaying()
                    handlerTimeout.postDelayed({
                        presenter.playMetronom(bpm, selectNote1, selectNote2, selectNote3, kBpm)
                    }, 300)
                }
            }

        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<String>(sizeKey)
            ?.observe(viewLifecycleOwner){
                textViewFractionSize.text = it
            }
        //


        // В самом начале создаём таймер и soundPool
        presenter.createSoundTimer(requireContext())

        handlerLongTap = Handler(Looper.getMainLooper())

        // Ставим прослушиватель на editTextBpm, чтобы bpm изменялся при изменении поля editTextBpm
        editTextBpm.addTextChangedListener {
            val textBpm = editTextBpm.text.toString()
            presenter.metronomDependEditText(imageViewPlay.isVisible, textBpm, isTapping, selectNote1, selectNote2, selectNote3, kBpm)
            seekBarBpm.setProgress(presenter.getCurrentBpm(textBpm)- MIN_BPM)
        }

        // Ставим прослушиватель на кнопку Play для запуска метронома
        imageViewPlay.setOnClickListener {
            val textBpm=  editTextBpm.text.toString()
            presenter.tapPlayButton(textBpm, selectNote1, selectNote2, selectNote3, kBpm)
        }
        // Ставим прослушиватель на кнопку Pause для паузы метронома
        imageViewPause.setOnClickListener {
            presenter.pauseMetronom()
        }

        // Слушатели для длительного нажатия на кнопку Увеличить bpm
        buttonUp.setOnClickListener {
            presenter.updateBpm(true, editTextBpm.text.toString())
        }
        buttonUp.setOnLongClickListener {
            isTapping = true
            autoUp = true
            handlerLongTap.post{
                handlerLongTap.postDelayed(bpmUpdater(), 50L)
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
        buttonDown.setOnClickListener{
            presenter.updateBpm(false, editTextBpm.text.toString())
        }
        buttonDown.setOnLongClickListener {
            isTapping = true
            autoDown = true
            handlerLongTap.post{
                handlerLongTap.postDelayed(bpmUpdater(), 50L)
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

        // Вызов всплывающего экрана с размером
        imageButtonSize.setOnClickListener {
            findNavController().navigate(R.id.action_metronomFragment_to_sheetSizeFragment)
        }

        //вызов всплывающего экрана с нотами
        imageButtonDrawing.setOnClickListener {
            findNavController().navigate(R.id.action_metronomFragment_to_sheetDrawingFragment)
        }

    }


    // Внутренний класс, который нужен для долгого нажатия по кнопкам
    inner class bpmUpdater : Runnable {
        override fun run() {
            if (autoUp) {
                presenter.updateBpm(true, editTextBpm.text.toString())
                handlerLongTap.postDelayed(bpmUpdater(), 50L)
            } else if (autoDown) {
                presenter.updateBpm(false, editTextBpm.text.toString())
                handlerLongTap.postDelayed(bpmUpdater(), 50L)
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

    override fun setColorFraction1(color: Int) {
        imageButtonFraction1.backgroundTintList = ColorStateList.valueOf(color)
    }
    override fun setColorFraction2(color: Int) {
        imageButtonFraction2.backgroundTintList = ColorStateList.valueOf(color)
    }
    override fun setColorFraction3(color: Int) {
        imageButtonFraction3.backgroundTintList = ColorStateList.valueOf(color)
    }
    override fun setColorFraction4(color: Int) {
        imageButtonFraction4.backgroundTintList = ColorStateList.valueOf(color)
    }
}


