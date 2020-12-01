package com.pppb.tb02.presenter

import com.pppb.tb02.model.Piano
import com.pppb.tb02.view.IMainActivity

class MainPresenter(private val ui: IMainActivity): IMainPresenter {
    private var score: Int = 0
    private var piano: Piano = Piano()

    var isThreadHasInitiated: Boolean = false
    var isThreadHasBlocked: Boolean = false
    var isThreadHasRunning: Boolean = false

    fun getPiano() = this.piano

    fun getScore() = this.score

    fun setPiano(piano: Piano) {
        this.piano = piano
    }

    fun addScore() {
        this.score += 10
        this.ui.updateUIScore(this.score)
    }

    fun addScore(customScore: Int) {
        this.score += customScore
        this.ui.updateUIScore(this.score)
    }

    fun setThreadBlocked() {
        this.isThreadHasRunning = false
        this.isThreadHasBlocked = true
        this.isThreadHasInitiated = false
    }
}