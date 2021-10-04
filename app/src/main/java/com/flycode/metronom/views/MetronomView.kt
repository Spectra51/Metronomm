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
}