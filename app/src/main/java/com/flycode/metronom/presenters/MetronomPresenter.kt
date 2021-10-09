package com.flycode.metronom.presenters

import android.content.Context
import android.graphics.Color
import android.os.Handler
import android.os.Looper
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
    private val handlerCircle = Handler(Looper.getMainLooper()) // Handler для пульсации круга вокруг Play

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
                //pulsationCircle() // Из-за пульсации на высокой скорости крашится приложение
                model.soundPool.play(1,1f, 1f, 1, 0, 1f)
            }, 0L, calculateSleepDuration(bpm*kBpm))
        viewState.isPlaying()
    }

    // Метод для подсчёта перерыва в секундах между ударами - считается из BPM
    fun calculateSleepDuration(bpm: Int): Long {
        return (MILLISECONDS_IN_SECOND * (SECONDS_IN_MINUTE / bpm.toFloat())).toLong()
    }

    // Пауза метронома
    fun pauseMetronom(){
        nextQ = 0 // сделаем все переменные, отвечающие за счётчики, равными 0
        doubleQ = 0
        quadQ = 0
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
        val allowUpdate = if (increase) {newBpm <= MetronomFragment.const.MAX_BPM} else {newBpm >= MetronomFragment.const.MIN_BPM}
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

        // Подсветим наши доли белым цветом
        if (fractions == 2){
            if (nextQ == 1) viewState.setColorFraction1(Color.parseColor("#FFFFFF"))
            if (nextQ == 2) viewState.setColorFraction2(Color.parseColor("#FFFFFF"))
            if (nextQ == 3) viewState.setColorFraction1(Color.parseColor("#FFFFFF"))
            if (nextQ == 4) viewState.setColorFraction2(Color.parseColor("#FFFFFF"))

            // Уберём подсветку, изменив цвет на серый
            handlerNoLight.postDelayed({
                if (nextQ == 1) viewState.setColorFraction1(Color.parseColor("#424242"))
                if (nextQ == 2) viewState.setColorFraction2(Color.parseColor("#424242"))
                if (nextQ == 3) viewState.setColorFraction1(Color.parseColor("#424242"))
                if (nextQ == 4) viewState.setColorFraction2(Color.parseColor("#424242"))
            }, 50)

        } else if (fractions == 3){
            if (nextQ == 1) viewState.setColorFraction1(Color.parseColor("#FFFFFF"))
            if (nextQ == 2) viewState.setColorFraction2(Color.parseColor("#FFFFFF"))
            if (nextQ == 3) viewState.setColorFraction3(Color.parseColor("#FFFFFF"))

            handlerNoLight.postDelayed({
                if (nextQ == 1) viewState.setColorFraction1(Color.parseColor("#424242"))
                if (nextQ == 2) viewState.setColorFraction2(Color.parseColor("#424242"))
                if (nextQ == 3) viewState.setColorFraction3(Color.parseColor("#424242"))
            }, 50)

        } else {
            if (nextQ == 1) viewState.setColorFraction1(Color.parseColor("#FFFFFF"))
            if (nextQ == 2) viewState.setColorFraction2(Color.parseColor("#FFFFFF"))
            if (nextQ == 3) viewState.setColorFraction3(Color.parseColor("#FFFFFF"))
            if (nextQ == 4) viewState.setColorFraction4(Color.parseColor("#FFFFFF"))

            // Уберём подсветку, изменив цвет на серый
            handlerNoLight.postDelayed({
                if (nextQ == 1) viewState.setColorFraction1(Color.parseColor("#424242"))
                if (nextQ == 2) viewState.setColorFraction2(Color.parseColor("#424242"))
                if (nextQ == 3) viewState.setColorFraction3(Color.parseColor("#424242"))
                if (nextQ == 4) viewState.setColorFraction4(Color.parseColor("#424242"))
            }, 50)
        }


    }

    // Метод для сброса подсветки всех долей
    private fun  illuminationButtonsOff () {
        viewState.setColorFraction1(Color.parseColor("#424242"))
        viewState.setColorFraction2(Color.parseColor("#424242"))
        viewState.setColorFraction3(Color.parseColor("#424242"))
        viewState.setColorFraction4(Color.parseColor("#424242"))
    }

    /*
    // Метод для пульсации кнопки. Он будет использовать Handler, чтобы мы могли изменять элементы интерфейса из главного потока.
    // Ведь метод pulsationCircle() находится внутри Таймера.
    private fun pulsationCircle(){
        handlerCircle.post {
            viewState.setCirclePadding25()
            handlerCircle.postDelayed({
                viewState.setCirclePadding30()
            }, 50)
        }
    }
     */

}

