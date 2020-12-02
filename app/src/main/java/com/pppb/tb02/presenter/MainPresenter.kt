package com.pppb.tb02.presenter

import com.pppb.tb02.model.Piano
import com.pppb.tb02.util.PianoGenerator
import com.pppb.tb02.view.IMainActivity
import kotlin.random.Random

class MainPresenter(private val ui: IMainActivity): IMainPresenter {
    private var score: Int = 0
    private var piano: Piano = Piano()
    private var level: Int = 1
    //Thread States
    private var isThreadHasInitiated: Boolean = false
    private var isThreadHasBlocked: Boolean = false
    private var isThreadHasRunning: Boolean = false

    override fun getPiano() = this.piano

    override fun getLevel() = this.level

    override fun getScore() = this.score

    override fun isThreadHasRunning() = this.isThreadHasRunning

    override fun isThreadHasBlocked() = this.isThreadHasBlocked

    override fun threadIsRunning(state: Boolean) {
        this.isThreadHasRunning = state
    }

    override fun threadIsBlocked(state: Boolean) {
        this.isThreadHasBlocked = state
    }

    override fun setPiano(piano: Piano) {
        this.piano = piano
        this.ui.updatePiano(piano)
    }

    override fun resetGame() {
        this.setLevel(1)
        this.setScore(0)
        this.piano = PianoGenerator.createPiano(20, 500, Random.nextBoolean())
    }

    override fun addScore(customScore: Int) {
        this.score += customScore
        this.setScore(this.score)
    }

    private fun setScore(score: Int) {
        this.ui.updateUIScore(score)
    }

    override fun setLevel(level: Int) {
        this.level = level
        this.ui.setGameLevel(level)
    }

    override fun setThreadBlocked() {
        this.isThreadHasRunning = false
        this.isThreadHasBlocked = true
        this.isThreadHasInitiated = false
    }

    override fun setGameLost() {
        this.isThreadHasRunning = false
        this.isThreadHasBlocked = false
        this.isThreadHasInitiated = false
        this.ui.setGameLost()
    }
}