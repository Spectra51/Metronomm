package com.flycode.metronom.presenters

import android.content.Context
import android.graphics.Color
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.flycode.metronom.R
import com.flycode.metronom.fragments.MetronomFragment
import com.flycode.metronom.model.Metronom
import com.flycode.metronom.presenters.MetronomPresenter.const.MILLISECONDS_IN_SECOND
import com.flycode.metronom.presenters.MetronomPresenter.const.SECONDS_IN_MINUTE
import com.flycode.metronom.views.MetronomView
import moxy.MvpPresenter
import kotlin.concurrent.timerTask

class MetronomPresenter: MvpPresenter<MetronomView>() {

    private val model = Metronom()

    var nextQ: Int = 0 // номер доли (от 1 до 4), также это счётчик при 1-ой ноте
    var doubleQ: Int = 0 // счётчик при 2-ой ноте
    var quadQ: Int = 0 // счётчик при 3-ой ноте
    private val handlerNoLight = Handler(Looper.getMainLooper()) // Handler для того, чтобы убирать подсветку долей
    private val handlerTimeout = Handler(Looper.getMainLooper()) // Handler для задержки при запуске метронома
    private var buttonPicture: Int = 2
    private var buttonPicture2: Int = 1
    var buttonPicture3: Int = 1
    var buttonPicture4: Int = 1
    private var strongShare: Boolean = true // Вкл./Выкл. сильной доли в зависимости от выбранной кнопки
    private var strongShare2: Boolean = false
    private var strongShare3: Boolean = false
    private var strongShare4: Boolean = false
    var soundPoolPause: Boolean = false // Play/Pause SoundPool,в зависимости от выбранной слабой/пустой доли
    var soundPoolPause2: Boolean = false
    var soundPoolPause3: Boolean = false
    var soundPoolPause4: Boolean = false
    var soundPlay: Int = 0


    object const{
        const val MILLISECONDS_IN_SECOND: Int = 1000
        const val SECONDS_IN_MINUTE: Int = 60
    }

    // Создание звука по таймеру
    fun createSoundTimer(context: Context){
        model.createSoundPool(context)
        model.createTimer()
    }

    // Запуск метронома
    fun playMetronom(bpm: Int, selectNote1: Boolean, selectNote2: Boolean, selectNote3: Boolean, kBpm: Int, tripleSize: Boolean, fractions: Int){
        // Запустим таймер
        model.metronomTimer.schedule(
            timerTask {
                nextQ(selectNote1, tripleSize)
                nextQ2(selectNote2, tripleSize)
                nextQ3(selectNote3, tripleSize)
                illuminationFractions(fractions)
                playStrongShare(selectNote1,selectNote2,fractions)
                soundPoolPlay(fractions,tripleSize)
                //pulsationCircle() // Из-за пульсации на высокой скорости крашится приложение
            }, 0L, calculateSleepDuration(bpm*kBpm))
        viewState.isPlaying()
    }

    // Метод для подсчёта перерыва в секундах между ударами - считается из BPM
    fun calculateSleepDuration(bpm: Int): Long {
        return (MILLISECONDS_IN_SECOND * (SECONDS_IN_MINUTE / bpm.toFloat())).toLong()
    }

    // Метод для запуска/остановки звука по счету, в зависимости от выбора пустой/слабой доли
    private fun soundPoolPlay (fractions: Int,tripleSize: Boolean){
        Log.i("My","fraction"+fractions +soundPoolPause +soundPoolPause2 + soundPoolPause3 + soundPoolPause4 + soundPlay)
        soundPlay +=1
        when(tripleSize) {
            true -> if(soundPlay == 4) soundPlay =1
            else -> if(soundPlay == 5) soundPlay =1}
        when(fractions){
            2 -> when(soundPlay){
                    1 -> if(soundPoolPause) model.soundPool.pause(1) else model.soundPool.play(1,1f, 1f, 1, 0, 1f)
                    2 -> if(soundPoolPause2) model.soundPool.pause(1) else model.soundPool.play(1,1f, 1f, 1, 0, 1f)
                    3 -> if(soundPoolPause) model.soundPool.pause(1) else model.soundPool.play(1,1f, 1f, 1, 0, 1f)
                    4 -> if(soundPoolPause2) model.soundPool.pause(1) else model.soundPool.play(1,1f, 1f, 1, 0, 1f)}
            3 -> when(soundPlay){
                1 -> if(soundPoolPause) model.soundPool.pause(1) else model.soundPool.play(1,1f, 1f, 1, 0, 1f)
                2 -> if(soundPoolPause2) model.soundPool.pause(1) else model.soundPool.play(1,1f, 1f, 1, 0, 1f)
                3 -> if(soundPoolPause3) model.soundPool.pause(1) else model.soundPool.play(1,1f, 1f, 1, 0, 1f)
                }
            4 -> when(soundPlay){
                1 -> if(soundPoolPause) model.soundPool.pause(1) else model.soundPool.play(1,1f, 1f, 1, 0, 1f)
                2 -> if(soundPoolPause2) model.soundPool.pause(1) else model.soundPool.play(1,1f, 1f, 1, 0, 1f)
                3 -> if(soundPoolPause3) {model.soundPool.pause(1)} else model.soundPool.play(1,1f, 1f, 1, 0, 1f)
                4 -> if(soundPoolPause4) model.soundPool.pause(1) else model.soundPool.play(1,1f, 1f, 1, 0, 1f) } }
        }



    // Пауза метронома
    fun pauseMetronom(){
        nextQ = 0 // сделаем все переменные, отвечающие за счётчики, равными 0
        doubleQ = 0
        quadQ = 0
        soundPlay = 0
        illuminationButtonsOff() // отключим подсветку всех долей
        model.pauseTimer() // пересоздадим таймер
        viewState.isPausing()
    }


    // Метод при нажатии кнопки Play
    fun tapPlayButton(textBpm: String, selectNote1: Boolean, selectNote2: Boolean, selectNote3: Boolean, kBpm: Int, tripleSize: Boolean, fractions: Int){
        if (textBpm.isNotEmpty()) {
            val bpm = textBpm.toInt()
            if(bpm < MetronomFragment.const.MIN_BPM){
                viewState.showToast("Значение не может быть меньше 20")
                viewState.setEditTextBpm(MetronomFragment.const.MIN_BPM)
            } else if( bpm > MetronomFragment.const.MAX_BPM){
                viewState.showToast("Значение не может быть больше 240")
                viewState.setEditTextBpm(MetronomFragment.const.MAX_BPM)
            } else{
                playMetronom(bpm, selectNote1, selectNote2, selectNote3, kBpm, tripleSize, fractions)
            }
        } else {
            viewState.showToast("Значение не может быть пустым")
        }
    }

    // Метод для получения bpm из поля editTextBpm
    fun getCurrentBpm(textBpm: String): Int {
        if (textBpm.isNotEmpty()) {
            val bpm = textBpm.toInt()
            if (bpm > MetronomFragment.const.MAX_BPM){
                return MetronomFragment.const.MAX_BPM
            } else if(bpm < MetronomFragment.const.MIN_BPM){
                return MetronomFragment.const.MIN_BPM
            } else{
                return bpm
            }
        } else {
            return MetronomFragment.const.MIN_BPM
        }
    }

    // Метод для обновления значений в поле editTextBpm по нажатию на кнопки Увеличить bpm и Уменьшить bpm
    fun updateBpm (increase: Boolean, textBpm: String){
        val currentBpm = getCurrentBpm(textBpm)
        val newBpm = if (increase) currentBpm + 1 else currentBpm - 1
        val allowUpdate = if (increase) newBpm <= MetronomFragment.const.MAX_BPM else newBpm >= MetronomFragment.const.MIN_BPM
        if (allowUpdate) {
            viewState.setEditTextBpm(newBpm)
        }
    }

    fun metronomDependEditText(imageViewPlayVisible: Boolean, textBpm: String, isTapping: Boolean, selectNote1: Boolean, selectNote2: Boolean
                               , selectNote3: Boolean, kBpm: Int, tripleSize: Boolean, fractions: Int){
        if ((!imageViewPlayVisible && !isTapping) || (!isTapping && !imageViewPlayVisible && getCurrentBpm(textBpm)== MetronomFragment.const.MAX_BPM) ||
            (!isTapping && !imageViewPlayVisible && getCurrentBpm(textBpm)== MetronomFragment.const.MIN_BPM)){
            pauseMetronom()
            viewState.isPlaying()
            handlerTimeout.postDelayed({
                val bpm = getCurrentBpm(textBpm)
                playMetronom(bpm, selectNote1, selectNote2, selectNote3, kBpm, tripleSize, fractions)
            }, 300)

        }
    }

    // Функция проигрывающая сильную ноту, в зависимости от выбранной доли и ноты
     private fun playStrongShare (selectNote1: Boolean,selectNote2: Boolean,fractions: Int) {
        if (strongShare) {
            if (selectNote1){
                when (fractions) {
                    2 -> { when (nextQ) {1 -> model.soundPool.play(2, 1f, 1f, 2, 0, 1f)
                                         3 -> model.soundPool.play(2, 1f, 1f, 2, 0, 1f) } }
                    3 -> when (nextQ) {1 -> model.soundPool.play(2, 1f, 1f, 2, 0, 1f) }
                    4 -> when (nextQ) {1 -> model.soundPool.play(2, 1f, 1f, 2, 0, 1f) }
                }
            } else if (selectNote2){
                when (fractions) {
                    2 -> { when (doubleQ) {1 -> model.soundPool.play(2, 1f, 1f, 2, 0, 1f)
                                           5 -> model.soundPool.play(2, 1f, 1f, 2, 0, 1f) } }
                    3 -> when (doubleQ){ 1 -> model.soundPool.play(2, 1f, 1f, 2, 0, 1f)}
                    4 -> when (doubleQ){ 1 -> model.soundPool.play(2, 1f, 1f, 2, 0, 1f)} }
            } else {
                when(fractions){
                    2 ->{when(quadQ){1 -> model.soundPool.play(2, 1f, 1f, 2, 0, 1f)
                                     9 -> model.soundPool.play(2, 1f, 1f, 2, 0, 1f) }}
                    3 -> when(quadQ){1 -> model.soundPool.play(2, 1f, 1f, 2, 0, 1f)}
                    4 -> when(quadQ){1 -> model.soundPool.play(2, 1f, 1f, 2, 0, 1f)}
                }
            }
        }
        if (strongShare2) {
            if (selectNote1){
                when (fractions) {
                    2 -> { when (nextQ) {
                        2 -> model.soundPool.play(2, 1f, 1f, 2, 0, 1f)
                        4 -> model.soundPool.play(2, 1f, 1f, 2, 0, 1f) } }
                    3 -> when (nextQ) {2 -> model.soundPool.play(2, 1f, 1f, 2, 0, 1f) }
                    4 -> when (nextQ) {2 -> model.soundPool.play(2, 1f, 1f, 2, 0, 1f) }
                }
            } else if (selectNote2){
                when (fractions) {
                    2 -> { when (doubleQ) {3 -> model.soundPool.play(2, 1f, 1f, 2, 0, 1f)
                                           7 -> model.soundPool.play(2, 1f, 1f, 2, 0, 1f) } }
                    3 -> when (doubleQ){ 3 -> model.soundPool.play(2, 1f, 1f, 2, 0, 1f)}
                    4 -> when (doubleQ){ 3 -> model.soundPool.play(2, 1f, 1f, 2, 0, 1f)} }
            } else {
                when(fractions){
                    2 ->{when(quadQ){5 -> model.soundPool.play(2, 1f, 1f, 2, 0, 1f)
                                     13 -> model.soundPool.play(2, 1f, 1f, 2, 0, 1f) }}
                    3 -> when(quadQ){5 -> model.soundPool.play(2, 1f, 1f, 2, 0, 1f)}
                    4 -> when(quadQ){5 -> model.soundPool.play(2, 1f, 1f, 2, 0, 1f)}
                }
            }
        }
        if (strongShare3) {
            if (selectNote1){
                when (fractions) {
                    3 -> when (nextQ) {3 -> model.soundPool.play(2, 1f, 1f, 2, 0, 1f) }
                    4 -> when (nextQ) {3 -> model.soundPool.play(2, 1f, 1f, 2, 0, 1f) }
                }
            } else if (selectNote2){
                when (fractions) {
                    3 -> when (doubleQ){ 5 -> model.soundPool.play(2, 1f, 1f, 2, 0, 1f)}
                    4 -> when (doubleQ){ 5 -> model.soundPool.play(2, 1f, 1f, 2, 0, 1f)} }
            } else {
                when(fractions){
                    3 -> when(quadQ){9 -> model.soundPool.play(2, 1f, 1f, 2, 0, 1f)}
                    4 -> when(quadQ){9 -> model.soundPool.play(2, 1f, 1f, 2, 0, 1f)}
                }
            }
        }
        if (strongShare4) {
            if (selectNote1){
                when (fractions) {
                    3 -> when (nextQ) {4 -> model.soundPool.play(2, 1f, 1f, 2, 0, 1f) }
                    4 -> when (nextQ) {4 -> model.soundPool.play(2, 1f, 1f, 2, 0, 1f) }
                }
            } else if (selectNote2){
                when (fractions) {
                    3 -> when (doubleQ){ 7 -> model.soundPool.play(2, 1f, 1f, 2, 0, 1f)}
                    4 -> when (doubleQ){ 7 -> model.soundPool.play(2, 1f, 1f, 2, 0, 1f)} }
            } else {
                when(fractions){
                    3 -> when(quadQ){13 -> model.soundPool.play(2, 1f, 1f, 2, 0, 1f)}
                    4 -> when(quadQ){13 -> model.soundPool.play(2, 1f, 1f, 2, 0, 1f)}
                }
            }
        }


    }


    // МЕТОДЫ ДЛЯ ПОДСВЕТКИ ДОЛЕЙ. Они установят nextQ, которая будет в методе illuminationButtons() менять цвет нужной доли
    // Метод для выполнения тиков - 1 нота
    private fun nextQ (selectNote1: Boolean, tripleSize: Boolean) {
        if(selectNote1 && !tripleSize) {
            nextQ += 1
            if (nextQ == 5) nextQ = 1
        }else if (selectNote1 && tripleSize){
            nextQ += 1
            if (nextQ == 4) nextQ = 1
        }
        else{
            nextQ = 0
        }
    }

    // Метод для выполнения тиков - 2 нота
    private fun nextQ2 (selectNote2: Boolean, tripleSize: Boolean) {
        if(selectNote2 && !tripleSize){
            doubleQ += 1
            if (doubleQ == 9) doubleQ = 1
            when(doubleQ){
                1 -> nextQ = 1
                2 -> nextQ = 1
                3 -> nextQ = 2
                4 -> nextQ = 2
                5 -> nextQ = 3
                6 -> nextQ = 3
                7 -> nextQ = 4
                8 -> nextQ = 4
            }
        } else if (selectNote2 && tripleSize){
            doubleQ += 1
            if (doubleQ == 7) doubleQ = 1
            when(doubleQ){
                1 -> nextQ = 1
                2 -> nextQ = 1
                3 -> nextQ = 2
                4 -> nextQ = 2
                5 -> nextQ = 3
                6 -> nextQ = 3
            }
        }
        else{
            doubleQ = 0
        }

    }

    // Метод для выполнения тиков - 3 нота
    private fun nextQ3 (selectNote3: Boolean, tripleSize: Boolean){
        if (selectNote3 && !tripleSize){
            quadQ +=1
            if(quadQ == 17) quadQ = 1
            when(quadQ){
                1 -> nextQ = 1
                2 -> nextQ = 1
                3 -> nextQ = 1
                4 -> nextQ = 1
                5 -> nextQ = 2
                6 -> nextQ = 2
                7 -> nextQ = 2
                8 -> nextQ = 2
                9 -> nextQ = 3
                10 -> nextQ = 3
                11 -> nextQ = 3
                12 -> nextQ = 3
                13 -> nextQ = 4
                14 -> nextQ = 4
                15 -> nextQ = 4
                16 -> nextQ = 4
            }
        }else if (selectNote3 && tripleSize){
            quadQ +=1
            if(quadQ == 13) quadQ = 1
            when(quadQ){
                1 -> nextQ = 1
                2 -> nextQ = 1
                3 -> nextQ = 1
                4 -> nextQ = 1
                5 -> nextQ = 2
                6 -> nextQ = 2
                7 -> nextQ = 2
                8 -> nextQ = 2
                9 -> nextQ = 3
                10 -> nextQ = 3
                11 -> nextQ = 3
                12 -> nextQ = 3
            }
        }
        else{
            quadQ = 0
        }
    }



    // Метод для управления подсветкой.
    private  fun illuminationFractions (fractions: Int) {

        // ставим картинку рамки
        if (fractions == 2){
            if (nextQ == 1) viewState.setColorFraction1(R.drawable.frame)
            if (nextQ == 2) viewState.setColorFraction2(R.drawable.frame)
            if (nextQ == 3) viewState.setColorFraction1(R.drawable.frame)
            if (nextQ == 4) viewState.setColorFraction2(R.drawable.frame)

            //Уберём подсветку, изменив картинку в цвет  фона
            handlerNoLight.postDelayed({
                if (nextQ == 1) viewState.setColorFraction1(R.drawable.no_frame)
                if (nextQ == 2) viewState.setColorFraction2(R.drawable.no_frame)
                if (nextQ == 3) viewState.setColorFraction1(R.drawable.no_frame)
                if (nextQ == 4) viewState.setColorFraction2(R.drawable.no_frame)
            }, 50)

        } else if (fractions == 3){
            if (nextQ == 1) viewState.setColorFraction1(R.drawable.frame)
            if (nextQ == 2) viewState.setColorFraction2(R.drawable.frame)
            if (nextQ == 3) viewState.setColorFraction3(R.drawable.frame)

            handlerNoLight.postDelayed({
                if (nextQ == 1) viewState.setColorFraction1(R.drawable.no_frame)
                if (nextQ == 2) viewState.setColorFraction2(R.drawable.no_frame)
                if (nextQ == 3) viewState.setColorFraction3(R.drawable.no_frame)
            }, 50)

        } else {
            if (nextQ == 1) viewState.setColorFraction1(R.drawable.frame)
            if (nextQ == 2) viewState.setColorFraction2(R.drawable.frame)
            if (nextQ == 3) viewState.setColorFraction3(R.drawable.frame)
            if (nextQ == 4) viewState.setColorFraction4(R.drawable.frame)

            // Уберём подсветку, изменив картинку в цвет  фона
            handlerNoLight.postDelayed({
                if (nextQ == 1) viewState.setColorFraction1(R.drawable.no_frame)
                if (nextQ == 2) viewState.setColorFraction2(R.drawable.no_frame)
                if (nextQ == 3) viewState.setColorFraction3(R.drawable.no_frame)
                if (nextQ == 4) viewState.setColorFraction4(R.drawable.no_frame)
            }, 50)
        }


    }

    // Метод для сброса подсветки всех долей
    private fun  illuminationButtonsOff () {
        viewState.setColorFraction1(R.drawable.no_frame)
        viewState.setColorFraction2(R.drawable.no_frame)
        viewState.setColorFraction3(R.drawable.no_frame)
        viewState.setColorFraction4(R.drawable.no_frame)
    }

    // Метод для изменения картинки imageButtonFraction1
     fun funSetButtonPicture () {
        buttonPicture +=1
         if(buttonPicture == 4) buttonPicture =1

         when (buttonPicture) {
               1 ->  {viewState.setButtonPicture1(R.drawable.weakshare)
                      soundPoolPause = false}
               2 ->  {viewState.setButtonPicture1(R.drawable.strongshare)
                      strongShare = true }
               3 ->  {viewState.setButtonPicture1(R.drawable.emptyshare)
                      strongShare = false
                      soundPoolPause = true}
           }
     }
    // Метод для изменения картинки imageButtonFraction2
    fun funSetButtonPicture2 (){
        buttonPicture2 +=1
        if(buttonPicture2 == 4) buttonPicture2 =1
        when (buttonPicture2) {
            1 ->  {viewState.setButtonPicture2(R.drawable.weakshare)
                   soundPoolPause2 = false}
            2 ->  {viewState.setButtonPicture2(R.drawable.strongshare)
                   strongShare2 = true}
            3 ->  {viewState.setButtonPicture2(R.drawable.emptyshare)
                   strongShare2 = false
                  soundPoolPause2 = true}
        }
    }
    // Метод для изменения картинки imageButtonFraction3
    fun funSetButtonPicture3 (){
        buttonPicture3 +=1
        if(buttonPicture3 == 4) buttonPicture3 =1
        when (buttonPicture3) {
            1 -> {viewState.setButtonPicture3(R.drawable.weakshare)
                 soundPoolPause3 = false }
            2 -> {viewState.setButtonPicture3(R.drawable.strongshare)
                  strongShare3 = true}
            3 -> {viewState.setButtonPicture3(R.drawable.emptyshare)
                  strongShare3 = false
                  soundPoolPause3 =true}
        }
    }
    // Метод для изменения картинки imageButtonFraction4
    fun funSetButtonPicture4 (){
        buttonPicture4 +=1
        if(buttonPicture4 == 4) buttonPicture4 =1
        when (buttonPicture4) {
            1 ->  {viewState.setButtonPicture4(R.drawable.weakshare)
                   soundPoolPause4 = false}
            2 ->  {viewState.setButtonPicture4(R.drawable.strongshare)
                   strongShare4 = true}
            3 ->  {viewState.setButtonPicture4(R.drawable.emptyshare)
                   strongShare4 =false
                   soundPoolPause4 = true}
        }
    }


}

