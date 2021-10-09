package com.flycode.metronom.views

import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle

interface MetronomView : MvpView {
    @AddToEndSingle
    fun isPlaying()
    @AddToEndSingle
    fun isPausing()
    @AddToEndSingle
    fun showToast(toast: String)
    @AddToEndSingle
    fun setEditTextBpm(bpm: Int)
    @AddToEndSingle
    fun setColorFraction1(color: Int)
    @AddToEndSingle
    fun setColorFraction2(color: Int)
    @AddToEndSingle
    fun setColorFraction3(color: Int)
    @AddToEndSingle
    fun setColorFraction4(color: Int)
    //@AddToEndSingle // Для пульсации - 4 строки
    //fun setCirclePadding25()
    //@AddToEndSingle
    //fun setCirclePadding30()
}