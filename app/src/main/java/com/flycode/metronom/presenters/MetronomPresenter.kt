package com.flycode.metronom.presenters

import android.content.Context
import android.text.Editable
import android.widget.EditText
import android.widget.Toast
import androidx.core.view.isVisible
import com.flycode.metronom.fragments.MetronomFragment
import com.flycode.metronom.model.Metronom
import com.flycode.metronom.views.MetronomView
import moxy.MvpPresenter

class MetronomPresenter: MvpPresenter<MetronomView>() {

    val model = Metronom()

    // Создание звука по таймеру
    fun createSoundTimer(context: Context){
        model.createSoundTimer(context)
    }

    // Запуск метронома
    fun playMetronom(bpm: Int){
        model.playMetronom(bpm)
        viewState.isPlaying()
    }

    // Пауза метронома
    fun pauseMetronom(){
        model.pauseMetronom()
        viewState.isPausing()
    }

    // Метод при нажатии кнопки Play
    fun tapPlayButton(editTextBpm: EditText){
        if (editTextBpm.text.isNotEmpty()) {
            val bpm = editTextBpm.text.toString().toInt()
            if(bpm < MetronomFragment.const.MIN_BPM){
                viewState.showToast("Значение не может быть меньше 20")
                viewState.setEditTextBpm(MetronomFragment.const.MIN_BPM)
            } else if( bpm > MetronomFragment.const.MAX_BPM){
                viewState.showToast("Значение не может быть больше 240")
                viewState.setEditTextBpm(MetronomFragment.const.MAX_BPM)
            } else{
                playMetronom(bpm)
            }
        } else {
            viewState.showToast("Значение не может быть пустым")
        }
    }

    // Метод для получения bpm из поля editTextBpm
    fun getCurrentBpm(editTextBpm: EditText): Int {
        if (editTextBpm.text.isNotEmpty()) {
            val bpm = editTextBpm.text.toString().toInt()
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
    fun updateBpm (increase: Boolean, editTextBpm: EditText){
        val currentBpm = getCurrentBpm(editTextBpm)
        val newBpm = if (increase) currentBpm + 1 else currentBpm - 1
        val allowUpdate = if (increase) newBpm <= MetronomFragment.const.MAX_BPM else newBpm >= MetronomFragment.const.MIN_BPM
        if (allowUpdate) {
            viewState.setEditTextBpm(newBpm)
        }
    }

    fun metronomDependEditText(imageViewPlayVisible: Boolean, editTextBpm: EditText, isTapping: Boolean){
        if ((!imageViewPlayVisible && !isTapping) || (!imageViewPlayVisible && getCurrentBpm(editTextBpm)== MetronomFragment.const.MAX_BPM) ||
            (!imageViewPlayVisible && getCurrentBpm(editTextBpm)== MetronomFragment.const.MIN_BPM)){
            pauseMetronom()
            val bpm = getCurrentBpm(editTextBpm)
            playMetronom(bpm)
        }
    }

}
