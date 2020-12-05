package com.pppb.tb02.view

import android.os.Handler
import android.os.Message
import com.pppb.tb02.model.Piano
import com.pppb.tb02.presenter.IMainPresenter

class PianoThreadHandler(private val presenter: IMainPresenter): Handler() {
    private val msgTilesPosition = 0
    private val msgThreadBlocked = 1
    private val msgGameLevel = 2
    private val msgGameLost = 3
    private val msgBonusLevel = 4

    override fun handleMessage(msg: Message) {
        when(msg.what) {
            msgTilesPosition -> {
                val piano: Piano = msg.obj as Piano
                this.presenter.setPiano(piano)
            }
            msgThreadBlocked -> {
                this.presenter.setThreadBlocked()
            }
            msgGameLevel -> {
                val level: String = msg.obj as String
                this.presenter.setLevel(level)
            }
            msgGameLost -> {
                this.presenter.setGameLost()
            }
            msgBonusLevel -> {
                this.presenter.toggleBonusLevel()
            }
        }
    }

    fun sendBonusLevel() {
        val msg = Message()
        msg.what = msgBonusLevel

        this.sendMessage(msg)
    }

    fun sendGameLevel(level: String) {
        val msg = Message()
        msg.what = msgGameLevel
        msg.obj = level

        this.sendMessage(msg)
    }

    fun gameLost() {
        val msg = Message()
        msg.what = msgGameLost

        this.sendMessage(msg)
    }

    fun sendTilesLocation(piano: Piano) {
        val msg = Message()
        msg.what = msgTilesPosition
        msg.obj = piano

        this.sendMessage(msg)
    }

    fun threadHasBlocked() {
        val msg = Message()
        msg.what = msgThreadBlocked

        this.sendMessage(msg)
    }
}