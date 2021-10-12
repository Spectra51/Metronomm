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
    fun setColorFraction1(resId: Int)
    @AddToEndSingle
    fun setColorFraction2(resId: Int)
    @AddToEndSingle
    fun setColorFraction3(resId: Int)
    @AddToEndSingle
    fun setColorFraction4(resId: Int)
    @AddToEndSingle
    fun setButtonPicture1(resId: Int)
    @AddToEndSingle
    fun setButtonPicture2(resId: Int)
    @AddToEndSingle
    fun setButtonPicture3(resId: Int)
    @AddToEndSingle
    fun setButtonPicture4(resId: Int)
}