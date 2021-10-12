package com.flycode.metronom.fragments

import android.content.res.ColorStateList
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.widget.*
import androidx.core.view.isVisible
import androidx.core.view.setPadding
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

    private var autoUp: Boolean = false // переменные для автоматического увеличения bpm при долгом нажатии на кнопки Увеличить bpm и Уменьшить bpm
    private var autoDown: Boolean = false
    private var isTapping: Boolean = false // переменная обозначает, нажимаем ли мы на сикбар или кнопки Увеличить bpm и Уменьшить bpm

    private var kBpm = 1 // коэффициент для bpm - нужен для зависимости bpm от выбранной ноты
    private var selectNote1: Boolean = true // переменная true, когда нажимаем на выбранный рисунок с нотой
    private var selectNote2: Boolean = false
    private var selectNote3: Boolean = false

    private var fractions: Int = 2
    private var tripleSize: Boolean = false

    lateinit var imageViewPlay: ImageView // кнопка Play
    lateinit var imageViewPause: ImageView // кнопка Pause
    lateinit var editTextBpm: EditText // поле для ввода bpm
    lateinit var buttonUp: ImageButton // кнопка Увеличить bpm
    lateinit var buttonDown: ImageButton // кнопка Уменьшить bpm
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
    lateinit var imageButtonCircle: ImageButton // круг вокруг кнопки Play и Pause
    lateinit var imageButtonSetting: ImageButton // кнопка Настройки

    val handlerTimeout = Handler(Looper.getMainLooper()) // Handler для задержки после переключения рисунка
    val handlerLongTap = Handler(Looper.getMainLooper()) // Handler для долгого нажатия долгого нажатия по кнопкам Увеличить bpm и Уменьшить bpm


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
        imageButtonCircle = view.findViewById(R.id.imageButtonCircle)
        imageButtonSetting = view.findViewById(R.id.imageButtonSetting)

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
                        presenter.playMetronom(bpm, selectNote1, selectNote2, selectNote3, kBpm, tripleSize, fractions)
                    }, 300)
                }
            }

        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<String>(sizeKey)
            ?.observe(viewLifecycleOwner){
                textViewFractionSize.text = it
                val bpm = editTextBpm.text.toString().toInt()

                if (it == "2/4"){
                    presenter.soundPlay = 0
                    tripleSize = false
                    fractions = 2
                    imageButtonFraction1.visibility = View.VISIBLE
                    imageButtonFraction2.visibility = View.VISIBLE
                    imageButtonFraction3.visibility = View.GONE
                    imageButtonFraction4.visibility = View.GONE

                }else if (it == "3/4"){
                    presenter.soundPlay = 0
                    tripleSize = true
                    fractions = 3
                    imageButtonFraction1.visibility = View.VISIBLE
                    imageButtonFraction2.visibility = View.VISIBLE
                    imageButtonFraction3.visibility = View.VISIBLE
                    imageButtonFraction4.visibility = View.GONE
                } else{
                    presenter.soundPlay = 0
                    tripleSize = false
                    fractions = 4
                    imageButtonFraction1.visibility = View.VISIBLE
                    imageButtonFraction2.visibility = View.VISIBLE
                    imageButtonFraction3.visibility = View.VISIBLE
                    imageButtonFraction4.visibility = View.VISIBLE
                }

                if (!imageViewPlay.isVisible){
                    presenter.pauseMetronom()
                    isPlaying()
                    handlerTimeout.postDelayed({
                        presenter.playMetronom(bpm, selectNote1, selectNote2, selectNote3, kBpm, tripleSize, fractions)
                    }, 300)
                }

            }

        // Делаем из dp пиксели
        //pxSizePadding25 = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 25F, context?.getResources()?.getDisplayMetrics()).toInt()
        //pxSizePadding30 = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30F, context?.getResources()?.getDisplayMetrics()).toInt()

        // В самом начале создаём таймер и soundPool
        presenter.createSoundTimer(requireContext())


        imageButtonFraction1.setOnClickListener {
            presenter.funSetButtonPicture() }

        imageButtonFraction2.setOnClickListener {
            presenter.funSetButtonPicture2()}

        imageButtonFraction3.setOnClickListener {
            presenter.funSetButtonPicture3()}

        imageButtonFraction4.setOnClickListener {
            presenter.funSetButtonPicture4()}


        // Ставим прослушиватель на editTextBpm, чтобы bpm изменялся при изменении поля editTextBpm
        editTextBpm.addTextChangedListener {
            val textBpm = editTextBpm.text.toString()
            presenter.metronomDependEditText(imageViewPlay.isVisible, textBpm, isTapping, selectNote1, selectNote2, selectNote3, kBpm,
                tripleSize, fractions)
            seekBarBpm.setProgress(presenter.getCurrentBpm(textBpm)- MIN_BPM)
        }

        // Ставим прослушиватель на кнопку Play для запуска метронома
        imageViewPlay.setOnClickListener {
            val textBpm=  editTextBpm.text.toString()
            presenter.tapPlayButton(textBpm, selectNote1, selectNote2, selectNote3, kBpm, tripleSize, fractions)
        }
        // Ставим прослушиватель на кнопку Pause для паузы метронома
        imageViewPause.setOnClickListener {
            presenter.pauseMetronom()
        }

        // Слушатели для длительного нажатия на кнопку Увеличить bpm
        buttonUp.setOnClickListener {
            if (imageViewPause.isVisible){
                presenter.pauseMetronom()
                presenter.updateBpm(true, editTextBpm.text.toString())
                val bpm = editTextBpm.text.toString().toInt()
                presenter.playMetronom(bpm, selectNote1, selectNote2,selectNote3,kBpm,tripleSize,fractions)
            }else{
                presenter.updateBpm(true, editTextBpm.text.toString())
            }
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
            if (imageViewPause.isVisible){
                presenter.pauseMetronom()
                presenter.updateBpm(false, editTextBpm.text.toString())
                val bpm = editTextBpm.text.toString().toInt()
                presenter.playMetronom(bpm, selectNote1, selectNote2,selectNote3,kBpm,tripleSize,fractions)
            } else {
                presenter.updateBpm(false, editTextBpm.text.toString())
            }
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

        // Вызов всплывающего экрана с нотами
        imageButtonDrawing.setOnClickListener {
            findNavController().navigate(R.id.action_metronomFragment_to_sheetDrawingFragment)
        }

        // Вызов настроек
        imageButtonSetting.setOnClickListener {
            findNavController().navigate(R.id.action_metronomFragment_to_settingsFragment)
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

    override fun setColorFraction1(resId: Int) {
        imageButtonFraction1.setBackgroundResource(resId)
    }
    override fun setColorFraction2(resId: Int) {
        imageButtonFraction2.setBackgroundResource(resId)
    }
    override fun setColorFraction3(resId: Int) {
        imageButtonFraction3.setBackgroundResource(resId)
    }
    override fun setColorFraction4(resId: Int) {
        imageButtonFraction4.setBackgroundResource(resId)
    }

    override fun setButtonPicture1(resId: Int) {
        imageButtonFraction1.setImageResource(resId)
    }
    override fun setButtonPicture2(resId: Int) {
        imageButtonFraction2.setImageResource(resId)
    }
    override fun setButtonPicture3(resId: Int) {
        imageButtonFraction3.setImageResource(resId)
    }
    override fun setButtonPicture4(resId: Int) {
        imageButtonFraction4.setImageResource(resId)
    }


}


