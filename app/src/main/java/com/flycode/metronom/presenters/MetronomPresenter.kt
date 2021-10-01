package com.flycode.metronom.presenters

import android.content.Context
import com.flycode.metronom.model.Metronom
import com.flycode.metronom.views.MetronomView
import moxy.MvpPresenter

class MetronomPresenter: MvpPresenter<MetronomView>() {

    val model = Metronom()

    fun createSoundTimer(context: Context){
        model.createSoundTimer(context)
    }

    fun playMetronom(bpm: Int){
        model.playMetronom(bpm)
        viewState.isPlaying()
    }

    fun pauseMetronom(){
        model.pauseMetronom()
        viewState.isPausing()
    }





}