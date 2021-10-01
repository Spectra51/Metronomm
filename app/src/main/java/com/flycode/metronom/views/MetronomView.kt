package com.flycode.metronom.views

import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle

interface MetronomView : MvpView {
    @AddToEndSingle
    fun isPlaying()
    @AddToEndSingle
    fun isPausing()
}